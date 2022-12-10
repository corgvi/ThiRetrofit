package com.example.thiretrofit.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.thiretrofit.BaseURL;
import com.example.thiretrofit.MotoInterface.OnItemClickInterface;
import com.example.thiretrofit.MotoInterface.ResponseApi;
import com.example.thiretrofit.R;
import com.example.thiretrofit.model.Moto;
import com.example.thiretrofit.screen.SuaActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MotoAdapter extends RecyclerView.Adapter<MotoAdapter.MotoViewHolder> {

    private List<Moto> listMoto;
    private Context mContext;

    public MotoAdapter(List<Moto> listMoto, Context mContext) {
        this.listMoto = listMoto;
        this.mContext = mContext;
    }

    public void setData(List<Moto> listMoto) {
        this.listMoto = listMoto;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MotoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_moto, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MotoViewHolder holder, int position) {
        Moto moto = listMoto.get(position);
        holder.tvcolor.setText(moto.getColor());
        ;
        holder.tvName.setText(moto.getName());
        holder.tvPrice.setText(moto.getPrice() + "");
        Glide.with(mContext)
                .load(moto.getImage())
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_file_download_off_24)
                .into(holder.img);

        holder.btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SuaActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("moto", moto);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

        holder.btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure to Exit")
                        .setMessage("Exiting will call finish() method")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Gson gson = new GsonBuilder().setLenient().create();
                                Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl(BaseURL.BASE_URL)
                                        .addConverterFactory(GsonConverterFactory.create(gson))
                                        .build();
                                ResponseApi responseApi = retrofit.create(ResponseApi.class);
                                Call<Moto> deleteMoto = responseApi.deleteMoto(Integer.parseInt(moto.getId()));
                                deleteMoto.enqueue(new Callback<Moto>() {
                                    @Override
                                    public void onResponse(Call<Moto> call, Response<Moto> response) {
                                        if (response.isSuccessful()) {
                                            Toast.makeText(mContext, "Delete " + response.body().getName() + " successful", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Moto> call, Throwable t) {

                                    }
                                });
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //set what should happen when negative button is clicked
                                Toast.makeText(mContext.getApplicationContext(), "OK", Toast.LENGTH_LONG).show();
                            }
                        })
                        .show();


            }
        });
    }

    @Override
    public int getItemCount() {
        return listMoto.size();
    }

    class MotoViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvcolor;
        ImageView img, btnSua, btnXoa;

        public MotoViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvcolor = itemView.findViewById(R.id.tv_color);
            img = itemView.findViewById(R.id.img);
            btnSua = itemView.findViewById(R.id.btn_sua);
            btnXoa = itemView.findViewById(R.id.btn_xoa);
        }
    }
}
