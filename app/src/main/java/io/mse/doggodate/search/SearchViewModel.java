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
    private MutableLiveData<ArrayList<Doggo>> allDoggos;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * gets all doggos from firestore
     * @param searchFirestoreCallback what should happen after doggos are loaded
     * @return doggos from fs
     */
    public MutableLiveData<ArrayList<Doggo>> getAllDoggos(FirestoreCallback searchFirestoreCallback) {
        if (allDoggos == null) {
            allDoggos = new MutableLiveData<ArrayList<Doggo>>();
            loadAllFirebaseDoggos(searchFirestoreCallback);
        }
       else {
            searchFirestoreCallback.onDataRetrieved(allDoggos.getValue());
        }
        return allDoggos;
    }

    public void setShownDoggos(ArrayList<Doggo> doggos) {
        allDoggos.setValue(doggos);
    }

    /**
     * search for doggos whose names correspond to param name
     * @param searchFirestoreCallback what should happen after search is done
     * @param name name of searched doggo
     * @return
     */
    public MutableLiveData<ArrayList<Doggo>> searchDoggos(FirestoreCallback searchFirestoreCallback, String name) {
        if (allDoggos == null) {
            allDoggos = new MutableLiveData<ArrayList<Doggo>>();
            loadAllFirebaseDoggosByName(searchFirestoreCallback, name);
        }
        else {
            loadAllFirebaseDoggosByName(searchFirestoreCallback, name);
            searchFirestoreCallback.onDataRetrieved(allDoggos.getValue());
        }
        return allDoggos;
    }

    /**
     * a user searches for doggos. The doggos that correspond the search query are shown. This method
     * however cleans the search and shows all doggos from firestore
     * @return
     */
    public MutableLiveData<ArrayList<Doggo>> resetShownDoggos() {
        loadAllFirebaseDoggos();

        return allDoggos;
    }

    /**
     * load all doggos
     * @param searchFirestoreCallback what should happen after the data is retrieved
     */
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
                            allDoggos.setValue(d);
                            //call the callback implementation from SearchFragment to bind data
                            searchFirestoreCallback.onDataRetrieved(allDoggos.getValue());

                        } else {
                            Log.w("ProfileViewModel", "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    /**
     * load all doggos
     */
    private void loadAllFirebaseDoggos() {

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
                            allDoggos.setValue(d);

                        } else {
                            Log.w("ProfileViewModel", "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    /**
     * load doggos from firestore that have the name
     * @param searchFirestoreCallback what should happen after its loaded
     * @param name name of the doggo
     */
    private void loadAllFirebaseDoggosByName(final FirestoreCallback searchFirestoreCallback, String name) {

        db.collection("Doggo").whereEqualTo("name", name).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<Doggo> d = new ArrayList<>();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Doggo doggo = document.toObject(Doggo.class);
                                d.add(doggo);
                            }
                            allDoggos.setValue(d);
                            //call the callback implementation from SearchFragment to bind data
                            searchFirestoreCallback.onDataRetrieved(allDoggos.getValue());

                        } else {
                            Log.w("ProfileViewModel", "Error getting documents.", task.getException());
                        }
                    }
                });

    }

}
