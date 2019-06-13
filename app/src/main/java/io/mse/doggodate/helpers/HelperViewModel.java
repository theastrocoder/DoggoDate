package io.mse.doggodate.helpers;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import io.mse.doggodate.entity.Doggo;
import io.mse.doggodate.entity.DoggoEvent;

public class HelperViewModel extends ViewModel {

    private MutableLiveData<Doggo> currentDoggo = new MutableLiveData<Doggo>();
    private MutableLiveData<ArrayList<Doggo>> currentDoggoFollowings = new MutableLiveData<>();
    private MutableLiveData<ArrayList<DoggoEvent>> currentDoggoEvents = new MutableLiveData<ArrayList<DoggoEvent>>();
    private MutableLiveData<ArrayList<Doggo>> currentDoggoFollowers = new MutableLiveData<>();

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

}
