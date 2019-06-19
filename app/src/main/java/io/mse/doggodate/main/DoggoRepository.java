package io.mse.doggodate.main;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import io.mse.doggodate.entity.Doggo;

public class DoggoRepository {

    private static final String TAG = "DoggoRepository";
    private MutableLiveData<Doggo> loggedInDoggo;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public DoggoRepository(){
        loggedInDoggo = new MutableLiveData<>();
    }

    public MutableLiveData<Doggo> getLoggedInDoggo() {
        return loggedInDoggo;
    }

    public void loadLoggedInDoggo(){
        db.collection("Doggo").whereEqualTo("active", true).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Doggo active = document.toObject(Doggo.class);
                                active.setId(document.getId());
                                loggedInDoggo.setValue(active);

                            }

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
