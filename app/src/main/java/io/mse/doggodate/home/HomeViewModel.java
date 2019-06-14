package io.mse.doggodate.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import io.mse.doggodate.entity.DoggoEvent;

public class HomeViewModel extends AndroidViewModel {

    LiveData<List<DoggoEvent>> doggoEventLiveData;
    LiveData<List<DoggoEvent>> eventsOfDoggosInFollowings;

    public HomeViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<DoggoEvent>> getDoggoEventLiveData() {
        return doggoEventLiveData;
    }

    public void addToDoggoEvent() {

    }
}
