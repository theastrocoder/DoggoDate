package io.mse.doggodate.rest;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DogZoneAPI  {

    @GET("/daten/geo")
    Call<JsonObject> getAll(@Query("service") String service,
                            @Query("request") String request,
                            @Query("version") String version,
                            @Query("typeName") String typeName,
                            @Query("srsName") String srsName,
                            @Query("outputFormat") String outputFormat
                            );
}
