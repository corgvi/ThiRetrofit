package com.example.thiretrofit.screen;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thiretrofit.adapter.BackupAdapter;
import com.example.thiretrofit.BaseURL;
import com.example.thiretrofit.model.Moto;
import com.example.thiretrofit.R;
import com.example.thiretrofit.MotoInterface.ResponseApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BackupActivity extends AppCompatActivity {

    private Button btnBackup;
    private RecyclerView rcv;
    private BackupAdapter adapter;
    private List<Moto> listMoto = new ArrayList<Moto>();
    FirebaseFirestore db ; ;
    String TAG = "zzzzzzz";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);
        FirebaseApp.initializeApp(BackupActivity.this);

        db  = FirebaseFirestore.getInstance();


        btnBackup = findViewById(R.id.btn_backup);
        rcv = findViewById(R.id.rcv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rcv.setLayoutManager(layoutManager);
        adapter = new BackupAdapter(listMoto, this);
        rcv.setAdapter(adapter);
        getMotos();
        btnBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFirestore(listMoto);
                Log.d(TAG, "onClick: " + listMoto.size());
            }
        });
    }

    private void getMotos(){
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
                listMoto.addAll(response.body());
                adapter.setData(listMoto);
            }

            @Override
            public void onFailure(Call<List<Moto>> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    private void addToFirestore(List<Moto> list){
        for (Moto m : list){
            db.collection("Moto123")
                    .add( m )
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("zzzz", "onSuccess: Thêm moto thành công");
                            Log.d(TAG, "onSuccess: " + documentReference.get());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("zzzz", "onFailure: lỗi thêm moto");
                            e.printStackTrace();
                        }
                    });
        }

    }
}