package com.example.thiretrofit.screen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thiretrofit.BaseURL;
import com.example.thiretrofit.model.Moto;
import com.example.thiretrofit.adapter.MotoAdapter;
import com.example.thiretrofit.R;
import com.example.thiretrofit.MotoInterface.ResponseApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private EditText edName, edPrice, edColor, edImage;
    private Button btnAdd, btnLoad, btnBackup;
    private RecyclerView recycler;
    private MotoAdapter adapter;
    private List<Moto> listMoto;
    String urlFake = "https://picsum.photos/200";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edName = findViewById(R.id.ed_ten);
        edPrice = findViewById(R.id.ed_price);
        edColor = findViewById(R.id.ed_color);
        edImage = findViewById(R.id.ed_img);
        btnAdd = findViewById(R.id.btn_add);
        btnLoad = findViewById(R.id.btn_load);
        btnBackup = findViewById(R.id.btn_backup);
        recycler = findViewById(R.id.rcv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager);
        adapter = new MotoAdapter(listMoto, this);
        getMoto();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Moto moto = new Moto(edName.getText().toString(), edImage.getText().toString(), Integer.parseInt(edPrice.getText().toString()), edColor.getText().toString());
                postMoto(moto);
                edName.setText("");
                edColor.setText("");
                edPrice.setText("");
                edImage.setText("");
            }
        });

        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMoto();
            }
        });

        btnBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BackupActivity.class));
            }
        });
    }

    private void getMoto(){
        Gson gson =new GsonBuilder().setLenient().create();
        Retrofit retrofit =new Retrofit.Builder()
                .baseUrl(BaseURL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        ResponseApi responseApi = retrofit.create(ResponseApi.class);
        Call<List<Moto>> motos = responseApi.getMotos();
        motos.enqueue(new Callback<List<Moto>>() {
            @Override
            public void onResponse(Call<List<Moto>> call, Response<List<Moto>> response) {
                listMoto = response.body();
                adapter.setData(listMoto);
                recycler.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Moto>> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    private void postMoto(Moto moto) {
        Gson gson =new GsonBuilder().setLenient().create();
        Retrofit retrofit =new Retrofit.Builder()
                .baseUrl(BaseURL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        ResponseApi responseApi = retrofit.create(ResponseApi.class);
        Call<Moto> postMoto = responseApi.postMoto(moto);
        postMoto.enqueue(new Callback<Moto>() {
            @Override
            public void onResponse(Call<Moto> call, Response<Moto> response) {
                Log.d("TAG", "post: " + response.body().toString());
                getMoto();
            }

            @Override
            public void onFailure(Call<Moto> call, Throwable t) {
                Log.d("TAG", "Failed: " + t.getMessage());
            }
        });
    }
}