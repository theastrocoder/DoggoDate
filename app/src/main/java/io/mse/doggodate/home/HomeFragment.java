package io.mse.doggodate.home;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.mse.doggodate.Entity.DoggoEvent;
import io.mse.doggodate.MainActivity;
import io.mse.doggodate.R;
import io.mse.doggodate.adapters.EventAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private MainActivity mainActivity;
    private HomeViewModel homeViewModel;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mainActivity = (MainActivity)getActivity();
        mainActivity.getSupportActionBar().setTitle("DoggoDate");

        homeViewModel = ViewModelProviders.of(getActivity()).get(HomeViewModel.class);


        RecyclerView recList = (RecyclerView) view.findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(RecyclerView.VERTICAL);
        recList.setLayoutManager(llm);

        EventAdapter ca = new EventAdapter(createEventList(15), new EventAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(DoggoEvent item) {
                areYouReallyWannaJoin(item);
            }});

        recList.setAdapter(ca);

        TextView weather = (TextView) view.findViewById(R.id.weather);
        weather.setText("Wieden 26°C");
        TextView gowalk = (TextView) view.findViewById(R.id.goforwalk);
        //gowalk.setText(((MainActivity)getActivity()).getActiveDog().getName()+ " wants to go for a walk. ");
        TextView lastwalk = (TextView) view.findViewById(R.id.lastwalk);
        lastwalk.setText("The last walk was 29 hours ago!");
       /* FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);

    // Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.i("HOME", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("HOME", "Error adding document", e);
                    }
                });*/
        return view;
    }

    private void areYouReallyWannaJoin(final DoggoEvent item) {
        // Setting Alert Dialog Title
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage("Your Doggo " + item.getCreator().getName() +" scheduled the walk for " + item.getDateTime().format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy")) +". " +
                "\nOthers are joining: \n" +item.joiningDoggosToString());
        alertDialogBuilder.setTitle("Wanna go for a walk? ");

        // Icon Of Alert Dialog
        //alertDialogBuilder.setIcon(R.drawable.question);
        // Setting Alert Dialog Message
        //alertDialogBuilder.setMessage("Are you sure,You want to exit");
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                ((MainActivity)getActivity()).getActiveDog().getEvents().add(item);
                Toast.makeText(getContext(), "You joined for the walk!", Toast.LENGTH_LONG).show();

            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(getContext(), "Walk rejected!", Toast.LENGTH_LONG).show();

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    private List<DoggoEvent> createEventList(int size) {

        List<DoggoEvent> result = new ArrayList<DoggoEvent>();
        for (int i=0; i < ((MainActivity)getActivity()).getActiveDoggoEvents().size(); i++) {

            DoggoEvent event = ((MainActivity)getActivity()).getActiveDoggoEvents().get(i);
            event.setDoggosJoining(((MainActivity)getActivity()).getDefaultSearch());
            result.add(event);

        }

        return result;
    }
}
