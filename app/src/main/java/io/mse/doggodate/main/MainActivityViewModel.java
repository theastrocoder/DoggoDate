package io.mse.doggodate.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import io.mse.doggodate.entity.Doggo;

public class MainActivityViewModel extends ViewModel {

    private DoggoRepository doggoRepository;
    private LiveData<Doggo> loggedInDoggo;

    public MainActivityViewModel(){
        doggoRepository = new DoggoRepository();
    }

    public LiveData<Doggo> getLoggedInDoggo() {
        loggedInDoggo = doggoRepository.getLoggedInDoggo();
        return loggedInDoggo;
    }

    public void loadLoggedInDogg(){
        doggoRepository.loadLoggedInDoggo();
    }
}
