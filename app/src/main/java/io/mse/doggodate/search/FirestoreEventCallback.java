package io.mse.doggodate.search;

import java.util.ArrayList;

import io.mse.doggodate.entity.Doggo;
import io.mse.doggodate.entity.DoggoEvent;

public interface FirestoreEventCallback {

    void onDataRetrieved(ArrayList<DoggoEvent> events);

}
