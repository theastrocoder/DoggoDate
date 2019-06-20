package io.mse.doggodate.doggozone;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONObject;

import java.util.ArrayList;

import io.mse.doggodate.entity.Doggo;
import io.mse.doggodate.entity.DoggoEvent;
import io.mse.doggodate.entity.DoggoZone;
import io.mse.doggodate.map.MapFirestoreCallback;

public class DoggoZoneViewModel extends ViewModel implements DoggoZoneFirestoreCallback{

    private DoggoZonesRepository doggoZonesRepository;
    private MutableLiveData<DoggoZone> selectedDoggoZone;
    private LiveData<DoggoZone> selectedDoggoZoneWithID;
    private MutableLiveData<ArrayList<DoggoEvent>> listEvents;
    private String TAG = "DZViewModel";


    public DoggoZoneViewModel(){
        doggoZonesRepository = new DoggoZonesRepository();
        doggoZonesRepository.setDoggoZoneFirestoreCallback(this);
        listEvents = doggoZonesRepository.getEventList();
        selectedDoggoZone = new MutableLiveData<>();
    }

    public void getListDoggosJoining(DoggoZone doggoZone) {
        doggoZonesRepository.getDoggoZoneEvents(doggoZone);
        Log.i("DZVIEV","do" + listEvents.getValue());
    }

    public void loadDoggoZoneFromFirestore(DoggoZone doggoZone){
        Log.i(TAG,"Loading DoggoZone from firestore");
        doggoZonesRepository.getSelectedDoggoZone(doggoZone);
    }

    public void createNewDoggoZone(DoggoZone doggoZone){
        doggoZonesRepository.createDoggoZone(doggoZone);
    }

    public LiveData<ArrayList<DoggoEvent>> getListEvents(){
        return listEvents;
    }

    public void addEvent(DoggoEvent doggoEvent){
        doggoZonesRepository.addEvent(doggoEvent);
    }

    public LiveData<DoggoZone> getSelectedDoggoZone() {
        return selectedDoggoZone;
    }

    public void setSelectedDoggoZone(DoggoZone selectedDoggoZone) {
        Log.i(TAG,"On DoggoZone set " + selectedDoggoZone);
        this.selectedDoggoZone.setValue(selectedDoggoZone);
    }

    public void setDoggoZoneFirestoreCallback(DoggoZoneFirestoreCallback doggoZoneFirestoreCallback) {
        doggoZonesRepository.setDoggoZoneFirestoreCallback(doggoZoneFirestoreCallback);
    }

    public void setMapFirestoreCallback(MapFirestoreCallback mapFirestoreCallback){
        doggoZonesRepository.setMapFirestoreCallback(mapFirestoreCallback);
    }

    @Override
    public void onDoggoEventsRetrieved(ArrayList<DoggoEvent> doggoEvents) {

    }

    @Override
    public void onSelectedDoggoZoneRetrieved(DoggoZone doggoZone) {
        Log.i(TAG,"Selected DoggoZone retrieved " + doggoZone.getId());
        doggoZonesRepository.getDoggoZoneEvents(doggoZone);
    }

    public void updateDoggoZone(DoggoZone doggoZone,Doggo activeDoggo, JSONObject jsonFile) {
        doggoZonesRepository.updateDoggoZone(doggoZone,activeDoggo,jsonFile);
    }

    public LiveData<DoggoZone> getSelectedDoggoZoneWithID() {
        selectedDoggoZoneWithID = doggoZonesRepository.getSelectedDoggoZoneLiveData();
        return selectedDoggoZoneWithID;
    }
}
