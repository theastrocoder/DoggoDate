package io.mse.doggodate;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import io.mse.doggodate.Entity.DoggoEvent;
import io.mse.doggodate.Entity.DoggoZone;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("DoggoDate");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        RecyclerView recList = (RecyclerView) view.findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
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
        gowalk.setText(((MainActivity)getActivity()).getActiveDog().getName()+ " wants to go for a walk. ");
        TextView lastwalk = (TextView) view.findViewById(R.id.lastwalk);
        lastwalk.setText("The last walk was 29 hours ago!");
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
                ((MainActivity)getActivity()).updateMyProfile();
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
