package io.mse.doggodate.map;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONObject;

import io.mse.doggodate.doggozone.DoggoZonesRepository;
import io.mse.doggodate.entity.DoggoZone;
public class MapViewModel extends ViewModel {

    private LiveData<JSONObject> doggoZoneMutableLiveData;
    private MutableLiveData<DoggoZone> selectedDoggoZone;
    private DoggoZonesRepository doggoZonesRepository = new DoggoZonesRepository();


    public LiveData<JSONObject> getFeatures() {
        doggoZoneMutableLiveData = doggoZonesRepository.getFeatures();
        selectedDoggoZone = doggoZonesRepository.getSelectedDoggoZoneLiveData();
        return doggoZoneMutableLiveData;
    }

    public void setSelectedDoggoZone(DoggoZone doggoZone){
        Log.i("MapViewModel","loading selected doggo zone");
        doggoZonesRepository.getSelectedDoggoZone(doggoZone);

    }

    public LiveData<DoggoZone> getDoggoZone(){
        return selectedDoggoZone;
    }


}
