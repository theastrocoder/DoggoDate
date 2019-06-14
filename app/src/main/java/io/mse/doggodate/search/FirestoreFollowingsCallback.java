package io.mse.doggodate.search;

import java.util.ArrayList;

import io.mse.doggodate.entity.Doggo;

public interface FirestoreFollowingsCallback {

    void onDataRetrievedFollowings(ArrayList<Doggo> myFollowings);

}
