package io.mse.doggodate.home;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import io.mse.doggodate.entity.Doggo;
import io.mse.doggodate.entity.DoggoEvent;
import io.mse.doggodate.entity.DoggoZone;

public class HomeRepository {

    private static final String TAG = "HomeRepository";
    private MutableLiveData<List<DoggoZone>> doggoZoneList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public HomeRepository(){
        doggoZoneList = new MutableLiveData<>();
    }

    public MutableLiveData<List<DoggoZone>> getDoggoZoneList() {
        return doggoZoneList;
    }

    public void retrieveDoggoEvents(){
        final List<DoggoEvent> tempDoggoEvents = new ArrayList<>();
        final List<DoggoZone> tempDoggoZone = new ArrayList<>();
         db.collection("DoggoZone")
                .whereEqualTo("favorite",true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    if(!task.getResult().isEmpty()){
                        for(QueryDocumentSnapshot doc : task.getResult()){
                            final DoggoZone doggoZone = doc.toObject(DoggoZone.class);
                            long e = LocalDateTime.now().minus(20, ChronoUnit.MINUTES)
                                    .atZone(ZoneId.systemDefault())
                                    .toEpochSecond();
                            Timestamp now = new Timestamp(e,0);
                            DocumentReference zoneID = db.collection("DoggoZone").document(doc.getId());
                            db.collection("Event")
                                    .orderBy("time", Query.Direction.ASCENDING)
                                    .whereGreaterThanOrEqualTo("time", now)
                                    .whereEqualTo("zone",zoneID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        if(!task.getResult().isEmpty()){
                                            for(QueryDocumentSnapshot document : task.getResult()){
                                                //create new empty instance to perform custom mapping
                                                final DoggoEvent event = new DoggoEvent();
                                                event.setZone(doggoZone);
                                                //set date and time
                                                Timestamp time = document.getTimestamp("time");
                                                LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(time.getSeconds() * 1000 + time.getNanoseconds() / 1000000), ZoneId.systemDefault());
                                                event.setTime(localDateTime);

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
                                                            }

                                                            tempDoggoEvents.add(event);

                                                            doggoZone.setEventList(tempDoggoEvents);

                                                            tempDoggoZone.add(doggoZone);
                                                            
                                                            doggoZoneList.setValue(tempDoggoZone);
                                                        } else {
                                                            Log.w(TAG, "Error getting documents.", task.getException());
                                                        }

                                                    }
                                                });

                                            }

                                        }
                                    }else {
                                        Log.d(TAG,"Retrieving events for favorite DoggoZones failed");
                                    }
                                }
                            });

                        }

                    }
                }else {
                    Log.d(TAG,"Retrieving Favorite DoggoZones failed");
                }


            }
        });

    }
}
