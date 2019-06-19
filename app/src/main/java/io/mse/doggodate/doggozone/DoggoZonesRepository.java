package io.mse.doggodate.doggozone;

import android.nfc.Tag;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.mse.doggodate.entity.Doggo;
import io.mse.doggodate.entity.DoggoEvent;
import io.mse.doggodate.entity.DoggoZone;
import io.mse.doggodate.main.MainActivity;
import io.mse.doggodate.map.MapFirestoreCallback;
import io.mse.doggodate.rest.DogZoneAPI;
import io.mse.doggodate.search.FirestoreCallback;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DoggoZonesRepository {

    private DogZoneAPI dogZoneAPI;
    private DoggoZoneFirestoreCallback doggoZoneFirestoreCallback;
    private MapFirestoreCallback mapFirestoreCallback;
    private MutableLiveData<DoggoZone> selectedDoggoZone;
    private MutableLiveData<JSONObject> featureCollection;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Retrofit retrofit;
    private MutableLiveData<ArrayList<DoggoEvent>> eventList;
    private String TAG = "DZRepository";

    public DoggoZonesRepository(){
        eventList = new MutableLiveData<>();
        selectedDoggoZone = new MutableLiveData<>();
        featureCollection = new MutableLiveData<>();
        retrofit = new Retrofit.Builder()
                .baseUrl("https://data.wien.gv.at")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        dogZoneAPI = retrofit.create(DogZoneAPI.class);
    }

    /**
     * Fetches custom Open Data Feature for active dog if exists, otherwise fetches original Open Data
     * @return Open Data Feature as LiveData Object
     */
    public LiveData<JSONObject> getFeatures(){
        db.collection("OpenDataFile")
                .document("DATA=97XuSnfcOmfW8pKF7B8y")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            if(task.getResult().exists()){
                                DocumentSnapshot document = task.getResult();
                                Object json = document.get("json");
                                try {
                                    JsonObject ob = new Gson().fromJson(json.toString(),JsonObject.class);
                                    featureCollection.setValue(new JSONObject(new Gson().toJson(ob)));
                                } catch (JSONException e) {
                                    Log.i(TAG,"Failed to create JSONObject with message " +
                                            e.getLocalizedMessage());
                                }
                            }else {
                                fetchFeature();
                            }
                        }else {
                            Log.i(TAG,"Failed to load current data JSON file");
                        }
                    }
                });
        return featureCollection;
    }

    /**
     * Fetch Open Data from Firestore if exists, if not fetch from "https://data.wien.gv.at"
     */
    private void fetchFeature(){
        db.collection("OpenDataFile")
                .document("OriginalOpenData")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            if(task.getResult().exists()){
                                DocumentSnapshot document = task.getResult();
                                Object json = document.get("json");
                                try {
                                    JsonObject ob = new Gson().fromJson(json.toString(),JsonObject.class);
                                    featureCollection.setValue(new JSONObject(new Gson().toJson(ob)));
                                } catch (JSONException e) {
                                    Log.i(TAG,"Failed to create JSONObject with message " +
                                            e.getLocalizedMessage());
                                }
                            }else {
                                fetchOpenDataFromWeb();
                            }
                        }else {
                            Log.i(TAG,"Failed to load original open data");
                        }
                    }
                });

    }

    /**
     * Fetches Open Data from "https://data.wien.gv.at" and saves it in Firestore
     */
    private void fetchOpenDataFromWeb(){
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
                    featureCollection.setValue(new JSONObject(gson.toJson(response.body())));

                    Map<String, Object> map = new HashMap<>();
                    map.put("json",response.body().toString());

                    //Saving data to Firestore
                    db.collection("OpenDataFile")
                            .document("OriginalOpenData")
                            .set(map)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Log.i(TAG,"Json data successfully saved");
                                    }else {
                                        Log.i(TAG,"Failed to save Json data");
                                    }
                                }
                            });
                } catch (JSONException e) {
                    Log.i(TAG,"Failed to fetch open data from wfs");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.i(TAG,"ERROR " + t.getMessage());
            }
        });
    }

    public void updateJSONData(Doggo activeDoggo,JSONObject jsonObject){
        Map<String,String> map = new HashMap<>();
        map.put("json",jsonObject.toString());
        db.collection("OpenDataFile")
                .document("DATA="+activeDoggo.getId())
                .set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.i(TAG,"JSON data successfully updated");
                        }else {
                            Log.i(TAG,"Failed to update JSON data");
                        }
                    }
                });
    }

    public void createDoggoZone(final DoggoZone doggoZone){
        Log.i(TAG,"Create DoggoZone if doesn't exist " + doggoZone.getName());

        db.collection("DoggoZone")
                .whereEqualTo("name",doggoZone.getName())
                .whereEqualTo("area",doggoZone.getArea())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            if(task.getResult().isEmpty()){
                                db.collection("DoggoZone")
                                        .document()
                                        .set(doggoZone)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Log.i(TAG,"DoggoZone successfully created");
                                                mapFirestoreCallback.onDoggoZoneCreated(doggoZone);
                                            }
                                        });
                            }else {
                                Log.i(TAG,"DoggoZone " + doggoZone.getName() + " already exists");
                                mapFirestoreCallback.onDoggoZoneCreated(doggoZone);
                            }
                        }
                    }
                });

    }

    public void getSelectedDoggoZone(final DoggoZone doggoZone) {
        Log.i("DoggoZoneRepository","Loading doggo zone from repog "+
                doggoZone.getName() + " " +doggoZone.getArea());
        db.collection("DoggoZone")
                .whereEqualTo("name",doggoZone.getName())
                .whereEqualTo("area",doggoZone.getArea())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            Log.i(TAG,"Task successful");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.i(TAG, document.getId() + " => " + document.getData());
                                DoggoZone active = document.toObject(DoggoZone.class);
                                active.setId(document.getId());
                                selectedDoggoZone.setValue(active);
                            }

                        }else {
                            Log.i("DoggoZonesRepository", "Failed to query Doggo Zones");
                        }
                    }
                });

    }

    public void getDoggoZoneEvents(DoggoZone doggoZone){
        Log.i("DZRepository","Retrieving doggos joining " + doggoZone.getName() +
                " ID: " + doggoZone.getId());
        eventList.setValue(new ArrayList<DoggoEvent>());
        DocumentReference zone = db.collection( "DoggoZone").document(doggoZone.getId());
        Timestamp now = Timestamp.now();
        Log.i(TAG,"TIMESTAMP NOW  " + now.getNanoseconds());
        db.collection("Event")
                .whereEqualTo("zone",zone)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            final ArrayList<DoggoEvent> doggoEvents = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //create new empty instance to perform custom mapping
                                final DoggoEvent event = new DoggoEvent();

                                //set date and time
                                Timestamp time = document.getTimestamp("time");
                                Log.i(TAG,"TIme " + time);
                                LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(time.getSeconds() * 1000 + time.getNanoseconds() / 1000000), ZoneId.systemDefault());
                                event.setTime(localDateTime);
                                Log.i("DZRepository", "time of event is: " + localDateTime.toString());

                                //get ready for retrieving custom objects
                                final DocumentReference doggoID =  document.getDocumentReference("creator");
                                doggoID.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            Log.i(TAG, "Task to retrieve doggo successful");
                                            if(task.getResult().exists()) {
                                                DocumentSnapshot document = task.getResult();

                                                Doggo doggo = document.toObject(Doggo.class);
                                                doggo.setId(document.getId());
                                                event.setCreator(doggo);
                                                Log.i(TAG, "name of creator is " + event.getCreator().getName());
                                            }
                                            doggoEvents.add(event);
                                            eventList.setValue(doggoEvents);

                                        } else {
                                            Log.w(TAG, "Error getting documents.", task.getException());
                                        }

                                    }
                                });

                            }

                        } else {
                            Log.w(TAG,"Error getting doggos.", task.getException());
                        }
                    }
                });

    }

    public ArrayList<Doggo> getAllDoggos(final FirestoreCallback firestoreCallback) {
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
                                firestoreCallback.onDataRetrieved(list);
                            }
                        } else {
                            Log.w("DZREPO", "Error getting doggos.", task.getException());
                        }
                    }
                });

        return list;
    }

    public void addEvent(final DoggoEvent doggoEvent) {
        final ArrayList<DoggoEvent> doggoEvents = new ArrayList<>();
        long s = doggoEvent.getTime().atZone( ZoneId.systemDefault()).toEpochSecond();
        Timestamp timestamp = new Timestamp( s,  0);
        Log.i("ProfileViewModel", "time that actually came to db of new event " + doggoEvent.getTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
        final Map<String, Object> eventFS = new HashMap<>();
        eventFS.put("time",timestamp);
        eventFS.put("creator", db.collection("Doggo").document(doggoEvent.getCreator().getId()));
        eventFS.put("zone", db.collection("DoggoZone").document(doggoEvent.getZone().getId()));


        db.collection("Event").add(eventFS).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()){
                    Log.i("DZRepository","Event added");
                    if(eventList.getValue() != null){
                        doggoEvents.addAll(eventList.getValue());
                    }
                    doggoEvents.add(doggoEvent);
                    eventList.setValue(doggoEvents);
                }
            }
        });
    }

    public MutableLiveData<ArrayList<DoggoEvent>> getEventList(){
        return eventList;
    }

    public MutableLiveData<DoggoZone> getSelectedDoggoZoneLiveData(){
        return selectedDoggoZone;
    }

    public void setDoggoZoneFirestoreCallback(DoggoZoneFirestoreCallback doggoZoneFirestoreCallback) {
        this.doggoZoneFirestoreCallback = doggoZoneFirestoreCallback;
    }

    public void setMapFirestoreCallback(MapFirestoreCallback mapFirestoreCallback) {
        this.mapFirestoreCallback = mapFirestoreCallback;
    }
}
