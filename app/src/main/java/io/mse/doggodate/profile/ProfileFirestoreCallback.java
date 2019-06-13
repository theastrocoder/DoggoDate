package io.mse.doggodate.profile;

import java.util.ArrayList;

import io.mse.doggodate.entity.Doggo;
import io.mse.doggodate.entity.DoggoEvent;

public interface ProfileFirestoreCallback {

    void onDataRetrieved(Doggo doggo);
    void onDataRetrieved(ArrayList<DoggoEvent> events);
}
