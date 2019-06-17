package io.mse.doggodate.doggozone;

import java.util.ArrayList;

import io.mse.doggodate.entity.DoggoEvent;
import io.mse.doggodate.entity.DoggoZone;

public interface DoggoZoneFirestoreCallback {

    void onDoggoEventsRetrieved(ArrayList<DoggoEvent> doggoEvents);
    void onSelectedDoggoZoneRetrieved(DoggoZone doggoZone);
}
