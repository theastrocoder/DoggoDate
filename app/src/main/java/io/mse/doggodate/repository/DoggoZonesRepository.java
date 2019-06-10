package io.mse.doggodate.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.mse.doggodate.entity.DoggoZone;
import io.mse.doggodate.rest.DogZoneAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DoggoZonesRepository {

    private DogZoneAPI dogZoneAPI;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Retrofit retrofit;

    public DoggoZonesRepository(){
        retrofit = new Retrofit.Builder()
                .baseUrl("https://data.wien.gv.at")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        dogZoneAPI = retrofit.create(DogZoneAPI.class);
    }

    public LiveData<JSONObject> getFeatures(){
        final MutableLiveData<JSONObject> feature = new MutableLiveData<>();
        dogZoneAPI.getAll("WFS",
                "GetFeature",
                "1.1.0",
                "ogdwien:HUNDEZONEOGD",
                "EPSG:4326",
                "json").enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                assert response.body() != null;
                Gson gson = new Gson();
                try {
                    feature.setValue(new JSONObject(gson.toJson(response.body())));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("Map","Data " + response.body());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.i("Map","EROR " + t.getMessage());
            }
        });

        return feature;

    }

    public LiveData<List<DoggoZone>> getAllDoggoZones() {

        return null;
    }

}
