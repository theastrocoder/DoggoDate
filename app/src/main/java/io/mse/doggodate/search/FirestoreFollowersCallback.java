package io.mse.doggodate.search;

import java.util.ArrayList;

import io.mse.doggodate.entity.Doggo;

public interface FirestoreFollowersCallback {

    void onDataRetrievedFollowers(ArrayList<Doggo> followers);
}
