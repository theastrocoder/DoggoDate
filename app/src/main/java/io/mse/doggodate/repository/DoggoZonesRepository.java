package io.mse.doggodate.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.mse.doggodate.entity.Doggo;
import io.mse.doggodate.entity.DoggoEvent;
import io.mse.doggodate.entity.DoggoZone;
import io.mse.doggodate.map.MapFirestoreCallback;
import io.mse.doggodate.profile.ProfileFirestoreCallback;
import io.mse.doggodate.rest.DogZoneAPI;
import io.mse.doggodate.search.FirestoreCallback;
import io.mse.doggodate.search.FirestoreEventCallback;
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

    public LiveData<DoggoZone> getSelectedDoggoZone(final DoggoZone doggoZone, final MapFirestoreCallback mapFirestoreCallback) {
        Log.i("DoggoZoneRepository","Loading doggo zone from repo");
        final MutableLiveData<DoggoZone> doggoZoneMutableLiveData=new MutableLiveData<>();

        db.collection("DoggoZone").whereEqualTo("name",doggoZone.getName()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            Log.i("DoggoZoneRepository","Task successful");
                            if(task.getResult().isEmpty()){

                                db.collection("DoggoZone").document().set(doggoZone)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Log.i("DoggoZoneRepository","new Zone added");
                                        }
                                    }
                                });
                            }else {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.i("DoggoZonesRepository", document.getId() + " => " + document.getData());
                                    DoggoZone active = document.toObject(DoggoZone.class);
                                    active.setId(document.getId());
                                    doggoZoneMutableLiveData.setValue(active);
                                    mapFirestoreCallback.onDataRetrieved(active);
                                }
                            }
                        }else {
                            Log.i("DoggoZonesRepository", "Failed to query Doggo Zones");
                        }
                    }
                });

        return doggoZoneMutableLiveData;
    }

    public LiveData<ArrayList<DoggoEvent>> getDoggoZoneEvents(final FirestoreEventCallback firestoreCallback, DoggoZone doggoZone){
        Log.i("DZRepository","Retrieving doggos joining park");
        final MutableLiveData<ArrayList<DoggoEvent>> events=new MutableLiveData<>();
        Log.i("DZREO",doggoZone.getName() + " " + doggoZone.getId());
        DocumentReference zone = db.collection( "DoggoZone").document(doggoZone.getId());
        db.collection("Events").whereEqualTo("zone",zone).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<DoggoEvent> eventList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                DoggoEvent event = document.toObject(DoggoEvent.class);
                                Log.i("DZRepository","doggos " + event.getTime());
                                eventList.add(event);
                            }
                            events.setValue(eventList);
                            firestoreCallback.onDataRetrieved(eventList);
                        } else {
                            Log.w("DZRepository", "Error getting doggos.", task.getException());
                        }
                    }
                });

        return events;
    }

    public ArrayList<Doggo> getAllDoggos(FirestoreCallback firestoreCallback) {
        final ArrayList<Doggo> list = new ArrayList<>();
        db.collection("Doggo").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Doggo doggo = document.toObject(Doggo.class);
                                doggo.setId(document.getId());
                                list.add(doggo);
                            }
                        } else {
                            Log.w("DZREPO", "Error getting doggos.", task.getException());
                        }
                    }
                });

        return list;
    }

    public void addEvent(final DoggoEvent doggoEvent) {
        long s = doggoEvent.getTime().atZone( ZoneId.systemDefault()).toEpochSecond();

        Timestamp timestamp = new Timestamp( s,  0);
        Log.i("ProfileViewModel", "time that actually came to db of new event " + doggoEvent.getTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
        Map<String, Object> eventFS = new HashMap<>();
        eventFS.put("time",timestamp);

        Log.d("DZREPO", "addEvent: " + doggoEvent.getCreator() + " " + doggoEvent.getZone().getId() );
        eventFS.put("creator", db.collection("Doggo").document(doggoEvent.getCreator().getId()));
        eventFS.put("zone", db.collection("DoggoZone").document(doggoEvent.getZone().getId()));

        db.collection("Event").add(eventFS).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                Log.i("DZRepository","Event added");
            }
        });

    }

}
