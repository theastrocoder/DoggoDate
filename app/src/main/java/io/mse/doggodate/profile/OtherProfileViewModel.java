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


    private MutableLiveData<Doggo> selectedFirebaseDoggo;
    private MutableLiveData<ArrayList<Doggo>> selectedDoggosFollowers = new MutableLiveData<>();
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
   public void getSelectedDoggosFollowers(){

   }
}
