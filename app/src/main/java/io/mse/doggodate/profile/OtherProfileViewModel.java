package io.mse.doggodate.profile;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import io.mse.doggodate.entity.Doggo;

public class OtherProfileViewModel extends ViewModel {

    private MutableLiveData<List<Doggo>> users;

    private MutableLiveData<Doggo> selectedFirebaseDoggo;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public MutableLiveData<Doggo> getSelectedDoggo() {
        if (selectedFirebaseDoggo == null) {
            selectedFirebaseDoggo = new MutableLiveData<Doggo>();
        }
        return selectedFirebaseDoggo;
    }

    public void setSelectedFirebaseDoggo(Doggo selectedFirebaseDoggo) {
        this.selectedFirebaseDoggo.setValue(selectedFirebaseDoggo);
    }
    private void loadActiveFirebaseDoggo() {

        db.collection("Doggo").whereEqualTo("active", true).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.i("ProfileViewModel", document.getId() + " => " + document.getData());
                                Doggo active = document.toObject(Doggo.class);
                                ArrayList<Long> arrayList = (ArrayList<Long>) document.get("photos");
                                //Do what you need to do with your ArrayList
                                for (Long s : arrayList) {
                                    Log.i("photoooooooooooooooooooooooooos", "photo"+s);
                                    active.getPhotos().add( s.intValue());
                                }
                                selectedFirebaseDoggo.setValue(active);

                            }

                        } else {
                            Log.w("ProfileViewModel", "Error getting documents.", task.getException());
                        }
                    }
                });

    }
}
