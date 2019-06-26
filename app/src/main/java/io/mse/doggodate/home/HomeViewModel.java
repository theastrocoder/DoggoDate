package io.mse.doggodate.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import io.mse.doggodate.entity.DoggoEvent;
import io.mse.doggodate.entity.DoggoZone;

public class HomeViewModel extends AndroidViewModel {

    HomeRepository homeRepository;

    LiveData<List<DoggoZone>> doggoZoneLiveData;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        homeRepository = new HomeRepository();
    }

    public LiveData<List<DoggoZone>> getDoggoZoneLiveData() {
        return homeRepository.getDoggoZoneList();
    }

    public void retrieveDoggoEvents(){
        homeRepository.retrieveDoggoEvents();
    }
    public void addToDoggoEvent() {

    }
}
