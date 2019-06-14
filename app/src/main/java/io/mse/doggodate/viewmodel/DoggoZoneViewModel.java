package io.mse.doggodate.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import io.mse.doggodate.entity.Doggo;
import io.mse.doggodate.entity.DoggoEvent;
import io.mse.doggodate.entity.DoggoZone;
import io.mse.doggodate.map.MapFirestoreCallback;
import io.mse.doggodate.repository.DoggoZonesRepository;
import io.mse.doggodate.search.FirestoreCallback;
import io.mse.doggodate.search.FirestoreEventCallback;

public class DoggoZoneViewModel extends ViewModel {

    DoggoZonesRepository doggoZonesRepository = new DoggoZonesRepository();
    private LiveData<ArrayList<DoggoEvent>> listEvents = new MutableLiveData<>();
    private MutableLiveData<DoggoZone> doggoZoneMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Doggo>> list=new MutableLiveData<>();

    public LiveData<ArrayList<DoggoEvent>> getListDoggosJoining(FirestoreEventCallback firestoreCallback,
                                                                DoggoZone doggoZone) {
        listEvents = doggoZonesRepository.getDoggoZoneEvents(firestoreCallback,doggoZone);
        Log.i("DZVIEV","do" + listEvents.getValue());
        return listEvents;
    }

    public void setListEvents(MutableLiveData<ArrayList<DoggoEvent>> listEvents) {
        this.listEvents = listEvents;
    }

    public void addEvent(DoggoEvent doggoEvent){
        doggoZonesRepository.addEvent(doggoEvent);
    }

    public LiveData<DoggoZone> getSelecetedDoggoZone(DoggoZone doggoZone, MapFirestoreCallback mapFirestoreCallback){
        return doggoZonesRepository.getSelectedDoggoZone(doggoZone,mapFirestoreCallback);
    }

    public void getAllDoggos(FirestoreCallback firestoreCallback){
        list.setValue(doggoZonesRepository.getAllDoggos(firestoreCallback));

    }
    public LiveData<ArrayList<Doggo>> getAlld(){
        return list;
    }
}
