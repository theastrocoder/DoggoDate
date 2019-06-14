package io.mse.doggodate.map;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;

import javax.security.auth.callback.Callback;

import io.mse.doggodate.MainActivity;
import io.mse.doggodate.R;
import io.mse.doggodate.adapters.SearchImageAdapter;
import io.mse.doggodate.adapters.ViewPagerAdapter;
import io.mse.doggodate.databinding.DoggozoneFragmentBinding;
import io.mse.doggodate.entity.Doggo;
import io.mse.doggodate.entity.DoggoEvent;
import io.mse.doggodate.entity.DoggoZone;
import io.mse.doggodate.helpers.HelperViewModel;
import io.mse.doggodate.profile.ProfileFirestoreCallback;
import io.mse.doggodate.profile.ProfileViewModel;
import io.mse.doggodate.search.FirestoreCallback;
import io.mse.doggodate.search.FirestoreEventCallback;
import io.mse.doggodate.viewmodel.DoggoZoneViewModel;

public class DoggozoneFragment extends Fragment {

    private DoggoZone selectedDogoZone;
    private ArrayList<String> date = new ArrayList<>(Arrays.asList("Today", "Tomorrow", "Pick a date"));
    Spinner spinner;
    ArrayAdapter<String> arrayAdapter;
    private Observer<DoggoZone> doggoZoneObserver;
    TextView parkName;
    TextView parkArea;
    TextView type;
    TextView fence;
    GridView gridView;
    ArrayList<String> images;
    private DoggoZoneViewModel doggoZoneViewModel;
    private LocalDate selectedDate;
    private Doggo activeDoggo;
    MapFirestoreCallback mapFirestoreCallback;
    public DoggozoneFragment() {

    }

    @BindingAdapter({"bind:handler"})
    public static void bindGridViewAdapterSearch(final GridView view, final DoggozoneFragment fragment)
    {
        MainActivity mainActivity = ((MainActivity)fragment.getActivity());

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.doggozone_fragment,container,false);
        final DoggozoneFragmentBinding binding = DataBindingUtil.inflate(inflater,R.layout.doggozone_fragment,container,false);
        doggoZoneViewModel = ViewModelProviders.of(getActivity()).get(DoggoZoneViewModel.class);
        final HelperViewModel helper = ViewModelProviders.of(getActivity()).get(HelperViewModel.class);
        final ProfileViewModel prfile = ViewModelProviders.of(getActivity()).get(ProfileViewModel.class);
        binding.setHandler(this);
        binding.setManager(getFragmentManager());
        parkName = (TextView) view.findViewById(R.id.park_name);
        parkArea = (TextView) view.findViewById(R.id.area);
        type = (TextView) view.findViewById(R.id.type);
        fence = (TextView) view.findViewById(R.id.fence);

        helper.getSelectedDoggoZone().observe(this, new Observer<DoggoZone>() {
            @Override
            public void onChanged(DoggoZone doggoZone) {
                selectedDogoZone=doggoZone;
                Log.i("IDDDIDIIID","hi " + doggoZone.getId() );
                parkName.setText(doggoZone.getName());
                parkArea.setText("Fläche: " + doggoZone.getArea() );
                type.setText("Typ: " + doggoZone.getTyp());
                fence.setText("Einfriedung: " + doggoZone.getFence());
                selectedDogoZone = doggoZone;
            }
        });
        setEvents();
        TextView doggos = (TextView) view.findViewById(R.id.doggos_joining);
        MainActivity mainActivity;
        mainActivity=(MainActivity) getActivity();
        mainActivity.invalidateOptionsMenu();

        doggos.setText("8 other are joining");

        spinner = (Spinner) view.findViewById(R.id.spinner);
        arrayAdapter =new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, date );
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Toast.makeText(getContext(),"Doggos at Park Today",Toast.LENGTH_LONG).show();
                        selectedDate = LocalDate.now();
                        break;
                    case 1:
                        Toast.makeText(getContext(),"Doggos at Park Tomorrow",Toast.LENGTH_LONG).show();
                        selectedDate = LocalDate.now().plus(1, ChronoUnit.DAYS);
                        break;
                    case 2:
                        if(date.size()==3){
                            pickDate();
                        }else {
                            Toast.makeText(getContext(), "Doggos in Park on " + date.get(2)+ " shown", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case 3:
                        if(date.get(3)!= null) {
                           pickDate();
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        gridView = (GridView) view.findViewById(R.id.grid_view);
        ProfileFirestoreCallback afterDoggoRetrieved = new ProfileFirestoreCallback() {
            @Override
            public void onDataRetrieved(Doggo doggo) {
                activeDoggo = doggo;

            }

            @Override
            public void onDataRetrieved(ArrayList<DoggoEvent> events) {


            }
            @Override
            public void onDataRetrievedFollowings(ArrayList<Doggo> myFollowings) {


            }

            @Override
            public void onDataRetrievedFollowers(ArrayList<Doggo> myFollowers) {


            }


        };
        prfile.getActiveDoggo(afterDoggoRetrieved).observe(getActivity(), new Observer<Doggo>() {
            @Override
            public void onChanged(Doggo doggo) {
                activeDoggo = doggo;
            }
        });



        FloatingActionButton scheduleWalk = (FloatingActionButton) view.findViewById(R.id.schedule_walk);
        scheduleWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickTime();
            }
        });

        return view;
    }

    private void pickTime() {

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                Toast.makeText(getContext(),"Scheduled walk at " + selectedHour + ":" +  (selectedMinute < 10? "0"+selectedMinute:selectedMinute),Toast.LENGTH_LONG).show();
                DoggoEvent doggoEvent = new DoggoEvent();
                doggoEvent.setCreator(activeDoggo);
                doggoEvent.setZone(selectedDogoZone);
                doggoEvent.setTime(LocalDateTime.of(selectedDate,LocalTime.of(timePicker.getHour(),timePicker.getMinute())));
                doggoZoneViewModel.addEvent(doggoEvent);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    void setEvents(){
        /*FirestoreEventCallback firestoreCallback = new FirestoreEventCallback() {
            @Override
            public void onDataRetrieved(ArrayList<DoggoEvent> events) {
                createGrid(events);
            }
        };

        doggoZoneViewModel.getListDoggosJoining(firestoreCallback,selectedDogoZone).observe(this, new Observer<ArrayList<DoggoEvent>>() {
            @Override
            public void onChanged(ArrayList<DoggoEvent> events) {
                createGrid(events);

            }
        });*/
        FirestoreCallback firestoreCallback = new FirestoreCallback() {
            @Override
            public void onDataRetrieved(ArrayList<Doggo> doggos) {
                Log.i("DZF","doggos "+ doggos);
                createGrid(doggos);
            }
        };
        doggoZoneViewModel.getAllDoggos(firestoreCallback);
        doggoZoneViewModel.getAlld().observe(this, new Observer<ArrayList<Doggo>>() {
            @Override
            public void onChanged(ArrayList<Doggo> doggos) {
                createGrid(doggos);
            }
        });

    }

    private void pickDate() {

        final Calendar currentDate = Calendar.getInstance();
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);
        final int day = currentDate.get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog datePickerDialog;
        datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if(date.size()==4){
                    date.remove(date.size()-1);
                    date.remove(2);
                }else {
                    date.remove(date.size()-1);
                }
                date.add(dayOfMonth + "." + month + "." + year);
                date.add("Pick a date");
                arrayAdapter.notifyDataSetChanged();
                spinner.setSelection(2);
                selectedDate = LocalDate.of(view.getYear(),view.getMonth(),view.getDayOfMonth());
                Log.i("DogooZone","ARRAy " + date);
            }
        },year,month,day);
        datePickerDialog.setTitle("Pick a date");
        datePickerDialog.show();
    }

    private void createGrid(ArrayList<Doggo> events){
        ArrayList<Doggo> doggos = new ArrayList<>();
        Log.i("DZF","Doggos joining retrieved" + doggos);
        SearchImageAdapter sa = new SearchImageAdapter(((MainActivity) getActivity()).getApplicationContext(), ((MainActivity) getActivity()),doggos );
        sa.setZone(true);
        sa.setComesIn("jetzt");
        gridView.setAdapter(new SearchImageAdapter(((MainActivity)getActivity()).getApplicationContext(), ((MainActivity)getActivity()), doggos));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                ((MainActivity)getActivity()).toOtherProfile(position, 2);
            };
        });
    }
}
