package io.mse.doggodate.profile;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import io.mse.doggodate.entity.Doggo;
import io.mse.doggodate.entity.DoggoEvent;

public class ProfileViewModel extends ViewModel {


    private MutableLiveData<Doggo> activeFirebaseDoggo;
    private MutableLiveData<ArrayList<DoggoEvent>> myEvents;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public MutableLiveData<Doggo> getActiveDoggo(ProfileFirestoreCallback profileFirestoreCallback) {
        if (activeFirebaseDoggo == null) {
            activeFirebaseDoggo = new MutableLiveData<Doggo>();
            loadActiveFirebaseDoggo(profileFirestoreCallback);
        } else {
            profileFirestoreCallback.onDataRetrieved(activeFirebaseDoggo.getValue());
        }
        return activeFirebaseDoggo;
    }

    public MutableLiveData<Doggo> getAMyEvents(ProfileFirestoreCallback profileFirestoreCallback) {
        if (activeFirebaseDoggo == null) {
            activeFirebaseDoggo = new MutableLiveData<Doggo>();
            loadActiveFirebaseDoggo(profileFirestoreCallback);
        } else {
            profileFirestoreCallback.onDataRetrieved(activeFirebaseDoggo.getValue());
        }
        return activeFirebaseDoggo;
    }


    private void loadActiveFirebaseDoggo(final ProfileFirestoreCallback profileFirestoreCallback) {

        db.collection("Doggo").whereEqualTo("active", true).get()
               .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Doggo active = document.toObject(Doggo.class);
                                    ArrayList<String> arrayList = (ArrayList<String>) document.get("photos");
                                    for (String s : arrayList) {
                                        active.getPhotos().add( s);
                                    }
                                activeFirebaseDoggo.setValue(active);
                                profileFirestoreCallback.onDataRetrieved(active);

                            }

                        } else {
                            Log.w("ProfileViewModel", "Error getting documents.", task.getException());
                        }
                    }
                });

    }
}

