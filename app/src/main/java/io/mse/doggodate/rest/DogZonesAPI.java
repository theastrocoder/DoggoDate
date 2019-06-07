package io.mse.doggodate.rest;

import java.util.List;

import io.mse.doggodate.Entity.DoggoZone;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;

public interface DogZonesAPI  {

    @GET("/daten/geo")
    Call<List<DoggoZone>> getall();
}
