package io.mse.doggodate.search;

import java.util.ArrayList;
import java.util.List;

import io.mse.doggodate.entity.Doggo;

public interface FirestoreCallback {

    void onDataRetrieved(ArrayList<Doggo> doggos);

}
