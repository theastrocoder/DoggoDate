package io.mse.doggodate.rest;

import java.util.List;

import io.mse.doggodate.entity.DoggoZone;
import retrofit2.Call;
import retrofit2.http.GET;

public interface DogZonesAPI  {

    @GET("/daten/geo")
    Call<List<DoggoZone>> getall();
}
