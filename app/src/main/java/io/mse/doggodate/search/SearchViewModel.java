package io.mse.doggodate.search;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import io.mse.doggodate.entity.Doggo;

public class SearchViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Doggo>> doggos;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public MutableLiveData<ArrayList<Doggo>> getAllDoggos(FirestoreCallback searchFirestoreCallback) {
        if (doggos == null) {
            doggos = new MutableLiveData<ArrayList<Doggo>>();
            loadAllFirebaseDoggos(searchFirestoreCallback);
        }
       else {
            searchFirestoreCallback.onDataRetrieved(doggos.getValue());
        }
        return doggos;
    }

    private void loadAllFirebaseDoggos(final FirestoreCallback searchFirestoreCallback) {

        db.collection("Doggo").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<Doggo> d = new ArrayList<>();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Doggo doggo = document.toObject(Doggo.class);
                                d.add(doggo);
                            }
                            doggos.setValue(d);
                            //call the callback implementation from SearchFragment to bind data
                            searchFirestoreCallback.onDataRetrieved(doggos.getValue());

                        } else {
                            Log.w("ProfileViewModel", "Error getting documents.", task.getException());
                        }
                    }
                });

    }

}
