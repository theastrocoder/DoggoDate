package io.mse.doggodate.viewmodel;

import android.graphics.LightingColorFilter;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import io.mse.doggodate.entity.Doggo;
import io.mse.doggodate.entity.DoggoZone;
import io.mse.doggodate.repository.DoggoZonesRepository;
import io.mse.doggodate.search.FirestoreCallback;

public class DoggoZoneViewModel extends ViewModel {

    DoggoZonesRepository doggoZonesRepository = new DoggoZonesRepository();
    private LiveData<ArrayList<Doggo>> listDoggosJoining= new MutableLiveData<>();

    public LiveData<ArrayList<Doggo>> getListDoggosJoining(FirestoreCallback firestoreCallback,
                                                           DoggoZone doggoZone) {
        listDoggosJoining = doggoZonesRepository.getDoggosJoining(firestoreCallback,doggoZone);
        Log.i("DZVIEV","do" + listDoggosJoining.getValue());
        return listDoggosJoining;
    }

    public void setListDoggosJoining(MutableLiveData<ArrayList<Doggo>> listDoggosJoining) {
        this.listDoggosJoining = listDoggosJoining;
    }
}
