package io.mse.doggodate.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONObject;

import io.mse.doggodate.entity.DoggoZone;
import io.mse.doggodate.repository.DoggoZonesRepository;

public class MapViewModel extends ViewModel {

    private LiveData<JSONObject> doggoZoneMutableLiveData;
    private LiveData<DoggoZone> selectedDoggoZone;
    private DoggoZonesRepository doggoZonesRepository = new DoggoZonesRepository();


    public LiveData<JSONObject> getFeatures() {
        doggoZoneMutableLiveData = doggoZonesRepository.getFeatures();
        return doggoZoneMutableLiveData;
    }

    public LiveData<DoggoZone> getSelectedDoggoZone(DoggoZone doggoZone){
        Log.i("MapViewModel","loading selected doggo zone");
        selectedDoggoZone = doggoZonesRepository.getSelectedDoggoZone(doggoZone);
        return selectedDoggoZone;
    }
}
