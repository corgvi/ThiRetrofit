package com.example.thiretrofit.screen;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.thiretrofit.BaseURL;
import com.example.thiretrofit.MotoInterface.ResponseApi;
import com.example.thiretrofit.R;
import com.example.thiretrofit.model.Moto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SuaActivity extends AppCompatActivity {

    private Button btnSua;
    private EditText edName, edPrice, edColor;
    private ImageView img;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua);

        btnSua = findViewById(R.id.btn_sua);
        edColor = findViewById(R.id.ed_color);
        edName = findViewById(R.id.ed_name);
        edPrice = findViewById(R.id.ed_price);
        img = findViewById(R.id.img);

        Moto moto = (Moto) getIntent().getSerializableExtra("moto");
        edColor.setText(moto.getColor());
        edName.setText(moto.getName());
        edPrice.setText(moto.getPrice()+"");

        Glide.with(this)
                .load(moto.getImage())
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_file_download_off_24)
                .into(img);

        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson =new GsonBuilder().setLenient().create();
                Retrofit retrofit =new Retrofit.Builder()
                        .baseUrl(BaseURL.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();
                ResponseApi responseApi = retrofit.create(ResponseApi.class);
                Call<Moto> updateMoto = responseApi.updateMoto(Integer.parseInt(moto.getId()), edName.getText().toString(), Integer.parseInt(edPrice.getText().toString()), edColor.getText().toString());
                updateMoto.enqueue(new Callback<Moto>() {
                    @Override
                    public void onResponse(Call<Moto> call, Response<Moto> response) {
                        Log.d("TAG", "update: " + response.body().getName());
                        Toast.makeText(SuaActivity.this, "Update successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SuaActivity.this, MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onFailure(Call<Moto> call, Throwable t) {
                        Log.d("TAG", "Failed: " + t.getMessage());
                    }
                });
            }
        });
    }
}