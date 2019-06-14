package io.mse.doggodate.helpers;


import androidx.lifecycle.LiveData;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import io.mse.doggodate.entity.Doggo;
import io.mse.doggodate.entity.DoggoEvent;
import io.mse.doggodate.entity.DoggoZone;

public class HelperViewModel extends ViewModel {

    private MutableLiveData<Doggo> currentDoggo = new MutableLiveData<Doggo>();
    private MutableLiveData<ArrayList<Doggo>> currentDoggoFollowings = new MutableLiveData<>();
    private MutableLiveData<ArrayList<DoggoEvent>> currentDoggoEvents = new MutableLiveData<ArrayList<DoggoEvent>>();
    private MutableLiveData<ArrayList<Doggo>> currentDoggoFollowers = new MutableLiveData<>();
    private MutableLiveData<DoggoZone> selectedDoggoZone = new MutableLiveData<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public MutableLiveData<Doggo> getCurrentDoggo() {
        if (currentDoggo == null) {
            currentDoggo = new MutableLiveData<Doggo>();
        }
        return currentDoggo;
    }

    public void setCurrentDoggoEvents(ArrayList<DoggoEvent> events) {
       currentDoggoEvents.setValue(events);
    }

    public void setCurrentDoggoFollowings(ArrayList<Doggo> followings) {
        currentDoggoFollowings.setValue(followings);
    }

    public void setCurrentDoggoFollowers(ArrayList<Doggo> followers) {
        currentDoggoFollowers.setValue(followers);
    }
    public MutableLiveData<ArrayList<DoggoEvent>> getCurrentDoggoEvents() {
        if (currentDoggoEvents == null) {
            currentDoggoEvents = new MutableLiveData<ArrayList<DoggoEvent>>();
        }
        return currentDoggoEvents;
    }

    public MutableLiveData<ArrayList<Doggo>> getCurrentDoggoFollowings() {
        if (currentDoggoFollowings == null) {
            currentDoggoFollowings = new MutableLiveData<ArrayList<Doggo>>();
        }
        return currentDoggoFollowings;
    }

    public MutableLiveData<ArrayList<Doggo>> getCurrentDoggoFollowers() {
        if (currentDoggoFollowers == null) {
            currentDoggoFollowers = new MutableLiveData<ArrayList<Doggo>>();
        }
        return currentDoggoFollowers;
    }

    public void setCurrentDoggo(Doggo doggo) {
        currentDoggo.setValue(doggo);
    }


    public LiveData<DoggoZone> getSelectedDoggoZone() {
        return selectedDoggoZone;
    }

    public void setSelectedDoggoZone(DoggoZone selectedDoggoZone) {
        this.selectedDoggoZone.setValue(selectedDoggoZone);
    }

    public void addFollower(final Doggo follower) {
        //current doggos followers
        final ArrayList<Doggo> followers = getCurrentDoggoFollowers().getValue();
        final DocumentReference dRef = db.collection("Doggo").document(follower.getId());
        final DocumentReference dRef2 = db.collection("Doggo").document("bpbSnLwYXpSk1do641fq");

        final DocumentReference followersOfDoggo =  db.collection("Followers").document(getCurrentDoggo().getValue().getId());
        followersOfDoggo.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {
                    followersOfDoggo.update("doggos", FieldValue.arrayUnion(dRef))
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.i("sdgfdfhhffghfghgfh", "FOLOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOW");
                                    followers.add(follower);
                                    getCurrentDoggoFollowers().setValue(followers);
                                }
                            });
                } else {
                    Map<String, Object> data = new HashMap<>();
                    ArrayList<DocumentReference> d = new ArrayList<DocumentReference>();
                    d.add(dRef);
                    d.add(dRef2);
                    data.put("doggos", Arrays.asList(dRef));
                    db.collection("Followers").document(getCurrentDoggo().getValue().getId()).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            followers.add(follower);
                            getCurrentDoggoFollowers().setValue(followers);
                        }
                    });
                }
            }
        });



    }

}
