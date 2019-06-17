package io.mse.doggodate.doggozone;

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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;

import io.mse.doggodate.MainActivity;
import io.mse.doggodate.R;
import io.mse.doggodate.adapters.SearchImageAdapter;
import io.mse.doggodate.databinding.DoggozoneFragmentBinding;
import io.mse.doggodate.entity.Doggo;
import io.mse.doggodate.entity.DoggoEvent;
import io.mse.doggodate.entity.DoggoZone;
import io.mse.doggodate.profile.ProfileFirestoreCallback;
import io.mse.doggodate.profile.ProfileViewModel;

public class DoggoZoneFragment extends Fragment{

    private DoggoZone selectedDoggoZone;
    private ArrayList<String> date = new ArrayList<>(Arrays.asList("Today", "Tomorrow", "Pick a date"));
    private Spinner spinner;
    private ArrayAdapter<String> arrayAdapter;
    private DoggoZoneViewModel doggoZoneViewModel;
    private LocalDate selectedDate;
    private Doggo activeDoggo;
    private DoggozoneFragmentBinding binding;

    public DoggoZoneFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.doggozone_fragment,container,false);
        doggoZoneViewModel = ViewModelProviders.of(
                getActivity()).
                get(DoggoZoneViewModel.class);
        final ProfileViewModel prfile = ViewModelProviders.of(getActivity()).get(ProfileViewModel.class);
        View view = binding.getRoot();
        binding.setHandler(this);
        doggoZoneViewModel.getSelectedDoggoZone().observe(this, new Observer<DoggoZone>() {
            @Override
            public void onChanged(DoggoZone doggoZone) {
                Log.i("DZF","DoggoZone on changed " + doggoZone.getName());
                selectedDoggoZone=doggoZone;
                binding.setDoggoZone(doggoZone);
                doggoZoneViewModel.getListDoggosJoining(doggoZone);
            }
        });
        doggoZoneViewModel.getListEvents().observe(this, new Observer<ArrayList<DoggoEvent>>() {
            @Override
            public void onChanged(ArrayList<DoggoEvent> doggoEvents) {
                createGrid(doggoEvents);
            }
        });

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
                final DoggoEvent doggoEvent = new DoggoEvent();
                doggoEvent.setCreator(activeDoggo);
                doggoEvent.setZone(selectedDoggoZone);
                doggoEvent.setTime(LocalDateTime.of(selectedDate,LocalTime.of(timePicker.getHour(),timePicker.getMinute())));
                doggoZoneViewModel.addEvent(doggoEvent);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
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

    private void createGrid(ArrayList<DoggoEvent> events){
        ArrayList<Doggo> doggos = new ArrayList<>();
        for(DoggoEvent e:events){
            doggos.add(e.getCreator());
        }
        Log.i("DZF","Doggos joining retrieved" + doggos);
        SearchImageAdapter sa = new SearchImageAdapter(((MainActivity) getActivity()).getApplicationContext(), ((MainActivity) getActivity()),doggos );
        sa.setZone(true);
        sa.setComesIn("jetzt");
        binding.gridView.setAdapter(sa);
        binding.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                ((MainActivity)getActivity()).toOtherProfile(position, 2);
            }
        });
    }
}
