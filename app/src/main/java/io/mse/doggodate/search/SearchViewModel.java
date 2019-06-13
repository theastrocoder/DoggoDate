package io.mse.doggodate.search;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

import io.mse.doggodate.entity.Doggo;
import io.mse.doggodate.entity.DoggoEvent;
import io.mse.doggodate.entity.DoggoZone;
import io.mse.doggodate.profile.ProfileFirestoreCallback;

public class SearchViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Doggo>> allDoggos;
    private MutableLiveData<ArrayList<DoggoEvent>> doggosEvents = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Doggo>> doggosFollowings = new MutableLiveData<>();


    private final String TAG = "SearchViewModel";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * gets all doggos from firestore
     * @param searchFirestoreCallback what should happen after doggos are loaded
     * @return doggos from fs
     */
    public MutableLiveData<ArrayList<Doggo>> getAllDoggos(FirestoreCallback searchFirestoreCallback) {
        if (allDoggos == null) {
            allDoggos = new MutableLiveData<ArrayList<Doggo>>();
            loadAllFirebaseDoggos(searchFirestoreCallback);
        }
       else {
            searchFirestoreCallback.onDataRetrieved(allDoggos.getValue());
        }
        return allDoggos;
    }

    public void setShownDoggos(ArrayList<Doggo> doggos) {
        allDoggos.setValue(doggos);
    }

    /**
     * search for doggos whose names correspond to param name
     * @param searchFirestoreCallback what should happen after search is done
     * @param name name of searched doggo
     * @return
     */
    public MutableLiveData<ArrayList<Doggo>> searchDoggos(FirestoreCallback searchFirestoreCallback, String name) {
        if (allDoggos == null) {
            allDoggos = new MutableLiveData<ArrayList<Doggo>>();
            loadAllFirebaseDoggosByName(searchFirestoreCallback, name);
        }
        else {
            loadAllFirebaseDoggosByName(searchFirestoreCallback, name);
            searchFirestoreCallback.onDataRetrieved(allDoggos.getValue());
        }
        return allDoggos;
    }

    /**
     * a user searches for doggos. The doggos that correspond the search query are shown. This method
     * however cleans the search and shows all doggos from firestore
     * @return
     */
    public MutableLiveData<ArrayList<Doggo>> resetShownDoggos() {
        loadAllFirebaseDoggos();

        return allDoggos;
    }

    /**
     * load all doggos
     * @param searchFirestoreCallback what should happen after the data is retrieved
     */
    private void loadAllFirebaseDoggos(final FirestoreCallback searchFirestoreCallback) {

        db.collection("Doggo").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<Doggo> d = new ArrayList<>();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Doggo doggo = document.toObject(Doggo.class);
                                doggo.setId(document.getId());
                                d.add(doggo);
                            }
                            allDoggos.setValue(d);
                            //call the callback implementation from SearchFragment to bind data
                            searchFirestoreCallback.onDataRetrieved(allDoggos.getValue());

                        } else {
                            Log.w("ProfileViewModel", "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    /**
     * load all doggos
     */
    private void loadAllFirebaseDoggos() {

        db.collection("Doggo").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<Doggo> d = new ArrayList<>();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Doggo doggo = document.toObject(Doggo.class);
                                d.add(doggo);
                            }
                            allDoggos.setValue(d);

                        } else {
                            Log.w("ProfileViewModel", "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    /**
     * load doggos from firestore that have the name
     * @param searchFirestoreCallback what should happen after its loaded
     * @param name name of the doggo
     */
    private void loadAllFirebaseDoggosByName(final FirestoreCallback searchFirestoreCallback, String name) {

        db.collection("Doggo").whereEqualTo("name", name).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<Doggo> d = new ArrayList<>();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Doggo doggo = document.toObject(Doggo.class);
                                d.add(doggo);
                            }
                            allDoggos.setValue(d);
                            //call the callback implementation from SearchFragment to bind data
                            searchFirestoreCallback.onDataRetrieved(allDoggos.getValue());

                        } else {
                            Log.w("ProfileViewModel", "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    public void loadMyFollowings(final FirestoreFollowingsCallback profileFirestoreCallback, String doggoID) {

        db.collection("Followings").document(doggoID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            final ArrayList<Doggo> myFollowingsTemp = new ArrayList<>();

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
                                doggosFollowings.setValue(myFollowingsTemp);
                                profileFirestoreCallback.onDataRetrievedFollowings(doggosFollowings.getValue());
                            }
                            } else {
                                Log.w(TAG, "Error getting documents.", task.getException());
                            }

                    }
                });


    }
    public void getEventForDoggo(String id, final FirestoreEventCallback firestoreEventCallback) {
        DocumentReference cRef = db.collection("Doggo").document(id);
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

                            doggosEvents.setValue(eventArrayList);
                            firestoreEventCallback.onDataRetrieved(eventArrayList);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

}
