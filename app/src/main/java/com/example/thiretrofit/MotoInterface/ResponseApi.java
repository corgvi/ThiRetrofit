package com.example.thiretrofit.MotoInterface;

import com.example.thiretrofit.model.Moto;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ResponseApi {

    @GET("moto")
    Call<List<Moto>> getMotos();

    @POST("moto")
    Call<Moto> postMoto(@Body Moto moto);

    @GET("moto/{id}")
    Call<Moto> getMoto(@Path("id") int id);

    @FormUrlEncoded
    @PUT("moto/{id}")
    Call<Moto> updateMoto(@Path("id") int id,
                          @Field("name") String name,
                          @Field("price") int price,
                          @Field("color") String color);

    @DELETE("moto/{id}")
    Call<Moto> deleteMoto(@Path("id") int id);
}
