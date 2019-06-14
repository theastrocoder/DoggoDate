package io.mse.doggodate.map;

import io.mse.doggodate.entity.DoggoZone;

public interface MapFirestoreCallback {

    void onDataRetrieved(DoggoZone doggoZone);
}
