package io.mse.doggodate.profile;

import io.mse.doggodate.entity.Doggo;

public interface ProfileFirestoreCallback {

    void onDataRetrieved(Doggo doggo);
}
