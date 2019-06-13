package io.mse.doggodate.profile;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import io.mse.doggodate.entity.Doggo;
import io.mse.doggodate.entity.DoggoEvent;
import io.mse.doggodate.entity.DoggoZone;

public class ProfileViewModel extends ViewModel {


    private MutableLiveData<Doggo> activeFirebaseDoggo;
    private MutableLiveData<ArrayList<DoggoEvent>> myEvents;
    private MutableLiveData<ArrayList<Doggo>> myFollowings;
    private MutableLiveData<ArrayList<Doggo>> myFollowers;

    private static final String TAG = "ProfileViewModel";

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public MutableLiveData<Doggo> getActiveDoggo(ProfileFirestoreCallback profileFirestoreCallback) {
        if (activeFirebaseDoggo == null) {
            activeFirebaseDoggo = new MutableLiveData<Doggo>();
            loadActiveFirebaseDoggo(profileFirestoreCallback);
        } else {
            profileFirestoreCallback.onDataRetrieved(activeFirebaseDoggo.getValue());
        }
        return activeFirebaseDoggo;
    }

    public MutableLiveData<ArrayList<DoggoEvent>> getMyEvents(ProfileFirestoreCallback profileFirestoreCallback, String doggoID) {
        if (myEvents == null) {
            myEvents = new MutableLiveData<ArrayList<DoggoEvent>>();
            loadMyEvents(profileFirestoreCallback, doggoID);
        } else {
            profileFirestoreCallback.onDataRetrieved(myEvents.getValue());
        }
        return myEvents;
    }

    public MutableLiveData<ArrayList<Doggo>> getMyFollowings(ProfileFirestoreCallback profileFirestoreCallback, String doggoID) {
        if (myFollowings == null) {
            myFollowings = new MutableLiveData<ArrayList<Doggo>>();
            loadMyFollowings(profileFirestoreCallback, doggoID);
        } else {
            profileFirestoreCallback.onDataRetrievedFollowings(myFollowings.getValue());
        }
        return myFollowings;
    }

    public MutableLiveData<ArrayList<Doggo>> getMyFollowers(ProfileFirestoreCallback profileFirestoreCallback, String doggoID) {
        if (myFollowers == null) {
            myFollowers = new MutableLiveData<ArrayList<Doggo>>();
            loadMyFollowers(profileFirestoreCallback, doggoID);
        } else {
            profileFirestoreCallback.onDataRetrievedFollowers(myFollowers.getValue());
        }
        return myFollowers;
    }


    private void loadMyEvents(final ProfileFirestoreCallback profileFirestoreCallback, String doggoID) {

        DocumentReference cRef = db.collection("Doggo").document(doggoID);
        db.collection("Event").whereEqualTo("creator", cRef).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                           final  ArrayList<DoggoEvent> eventArrayList = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //create new empty instance to perform custom mapping
                                final DoggoEvent event = new DoggoEvent();

                                //set date and time
                                Timestamp time = document.getTimestamp("time");
                                LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(time.getSeconds() * 1000 + time.getNanoseconds() / 1000000), ZoneId.systemDefault());
                                event.setTime(localDateTime);
                                 Log.i(TAG, "time of event is: " + localDateTime.toString());

                                 //get ready for retrieving custom objects
                                final DocumentReference doggoID =  document.getDocumentReference("creator");
                                final DocumentReference zoneID =  document.getDocumentReference("zone");

                                //get doggo java object
                                doggoID.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            Log.i(TAG, "Task to retrieve doggo successful");
                                            DocumentSnapshot document = task.getResult();

                                            Doggo doggo = document.toObject(Doggo.class);
                                            event.setCreator(doggo);
                                            Log.i(TAG, "name of creator is "+event.getCreator().getName());

                                            } else {
                                            Log.w(TAG, "Error getting documents.", task.getException());
                                        }

                                        //when doggo retrieved, retrieve zone
                                        zoneID.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    Log.i(TAG, "Task to retrieve zone successful");
                                                    DocumentSnapshot document = task.getResult();

                                                    DoggoZone zone = document.toObject(DoggoZone.class);

                                                    event.setZone(zone);
                                                    Log.i(TAG, "zone name is "+event.getZone().getName());
                                                    eventArrayList.add(event);

                                                }else {
                                                    Log.w(TAG, "Error getting documents.", task.getException());
                                                }
                                            }
                                        });
                                    }
                                });


                            }

                            myEvents.setValue(eventArrayList);
                            profileFirestoreCallback.onDataRetrieved(eventArrayList);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });


    }

    private void loadMyFollowings(final ProfileFirestoreCallback profileFirestoreCallback, String doggoID) {

        db.collection("Followings").document(doggoID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            final  ArrayList<Doggo> myFollowingsTemp = new ArrayList<>();

                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                ArrayList<DocumentReference> myFollowings2 = (ArrayList<DocumentReference>) document.get("doggos");
                                //create new empty instance to perform custom mapping

                                for (DocumentReference doggo : myFollowings2) {
                                    doggo.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            DocumentSnapshot document = task.getResult();

                                            Doggo doggo = document.toObject(Doggo.class);
                                            myFollowingsTemp.add(doggo);

                                        }
                                    });
                                }
                                myFollowings.setValue(myFollowingsTemp);
                                profileFirestoreCallback.onDataRetrievedFollowings(myFollowings.getValue());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });


    }

    private void loadMyFollowers(final ProfileFirestoreCallback profileFirestoreCallback, String doggoID) {

        db.collection("Followers").document(doggoID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            final ArrayList<Doggo> myFollowersTemp = new ArrayList<>();

                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                ArrayList<DocumentReference> myFollowers2 = (ArrayList<DocumentReference>) document.get("doggos");
                            //create new empty instance to perform custom mapping

                            for (DocumentReference doggo : myFollowers2) {
                                doggo.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        DocumentSnapshot document = task.getResult();

                                        Doggo doggo = document.toObject(Doggo.class);
                                        myFollowersTemp.add(doggo);

                                    }
                                });
                            }
                            myFollowers.setValue(myFollowersTemp);
                            profileFirestoreCallback.onDataRetrievedFollowers(myFollowers.getValue());
                        }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });


    }
    private void loadActiveFirebaseDoggo(final ProfileFirestoreCallback profileFirestoreCallback) {

        db.collection("Doggo").whereEqualTo("active", true).get()
               .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Doggo active = document.toObject(Doggo.class);
                                active.setId(document.getId());
                                activeFirebaseDoggo.setValue(active);
                                profileFirestoreCallback.onDataRetrieved(active);

                            }

                        } else {
                            Log.w("ProfileViewModel", "Error getting documents.", task.getException());
                        }
                    }
                });

    }
}

