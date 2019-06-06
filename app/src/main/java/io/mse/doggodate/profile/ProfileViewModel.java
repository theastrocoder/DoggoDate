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

import java.util.List;

import io.mse.doggodate.entity.Doggo;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<List<Doggo>> users;

    private MutableLiveData<Doggo> activeFirebaseDoggo;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public MutableLiveData<Doggo> getActiveDoggo() {
        if (activeFirebaseDoggo == null) {
            activeFirebaseDoggo = new MutableLiveData<Doggo>();
            loadActiveFirebaseDoggo();
        }
        return activeFirebaseDoggo;
    }

    public LiveData<List<Doggo>> getUsers() {
        if (users == null) {
            users = new MutableLiveData<List<Doggo>>();
            loadUsers();
        }
        return users;
    }

    private void loadUsers() {
        // Do an asynchronous operation to fetch users.
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
                                activeFirebaseDoggo.setValue(active);
                            }
                        } else {
                            Log.w("ProfileViewModel", "Error getting documents.", task.getException());
                        }
                    }
                });

    }
}

