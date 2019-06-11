package io.mse.doggodate.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONObject;

import io.mse.doggodate.repository.DoggoZonesRepository;

public class MapViewModel extends ViewModel {

    private LiveData<JSONObject> doggoZoneMutableLiveData;
    private DoggoZonesRepository doggoZonesRepository = new DoggoZonesRepository();


    public LiveData<JSONObject> getFeatures() {
        doggoZoneMutableLiveData = doggoZonesRepository.getFeatures();
        return doggoZoneMutableLiveData;
    }
}
