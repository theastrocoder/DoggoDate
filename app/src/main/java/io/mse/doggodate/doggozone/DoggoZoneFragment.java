package io.mse.doggodate.doggozone;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import io.mse.doggodate.R;
import io.mse.doggodate.adapters.SearchImageAdapter;
import io.mse.doggodate.databinding.DoggozoneFragmentBinding;
import io.mse.doggodate.entity.Doggo;
import io.mse.doggodate.entity.DoggoEvent;
import io.mse.doggodate.entity.DoggoZone;
import io.mse.doggodate.main.MainActivity;
import io.mse.doggodate.main.MainActivityViewModel;

public class DoggoZoneFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private static final String TAG ="DoggoZoneFragment";
    private DoggoZone selectedDoggoZone;
    private ArrayList<String> date = new ArrayList<>(Arrays.asList("Today", "Tomorrow", "Pick a date"));
    private Spinner spinner;
    private ArrayAdapter<String> arrayAdapter;
    private DoggoZoneViewModel doggoZoneViewModel;
    private LocalDateTime selectedDate;
    private Doggo activeDoggo;
    private DoggozoneFragmentBinding binding;
    private LocalDateTime pickedDate;

    public DoggoZoneFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.doggozone_fragment,container,false);
        doggoZoneViewModel = ViewModelProviders.of(
                getActivity()).
                get(DoggoZoneViewModel.class);
        final MainActivityViewModel main = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);
        View view = binding.getRoot();
        binding.setHandler(this);
        selectedDate = LocalDateTime.now();
        doggoZoneViewModel.getSelectedDoggoZoneWithID().observe(getViewLifecycleOwner(), new Observer<DoggoZone>() {
            @Override
            public void onChanged(DoggoZone doggoZone) {
                selectedDoggoZone=doggoZone;
                binding.setDoggoZone(doggoZone);
                if(doggoZone.getId()!=null){
                    doggoZoneViewModel.getListDoggosJoining(doggoZone,selectedDate);
                }
            }
        });
        doggoZoneViewModel.getListEvents().observe(getViewLifecycleOwner(), new Observer<ArrayList<DoggoEvent>>() {
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
                        selectedDate = LocalDateTime.now();
                        doggoZoneViewModel.getListDoggosJoining(selectedDoggoZone,selectedDate);
                        break;
                    case 1:
                        Toast.makeText(getContext(),"Doggos at Park Tomorrow",Toast.LENGTH_LONG).show();
                        LocalDate tomorrow = LocalDate.now().plus(1,ChronoUnit.DAYS);
                        selectedDate = LocalDateTime.of(tomorrow,LocalTime.of(0,0,0));
                        doggoZoneViewModel.getListDoggosJoining(selectedDoggoZone,selectedDate);
                        break;
                    case 2:
                        if(date.size()==3){
                            pickDate(view);
                        }else {
                            Toast.makeText(getContext(), "Doggos in Park on " + pickedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))+ " shown", Toast.LENGTH_LONG).show();
                            selectedDate = pickedDate;
                            doggoZoneViewModel.getListDoggosJoining(selectedDoggoZone,pickedDate);
                        }
                        break;
                    case 3:
                        if(date.get(3)!= null) {
                           pickDate(view);
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        main.getLoggedInDoggo().observe(getActivity(), new Observer<Doggo>() {
            @Override
            public void onChanged(Doggo doggo) {
                Log.i(TAG,"Active doggo is " + doggo);
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
                doggoEvent.setTime(LocalDateTime.of(selectedDate.toLocalDate(),LocalTime.of(timePicker.getHour(),timePicker.getMinute())));
                Log.i(TAG,"add event at " + doggoEvent.getTime());
                doggoZoneViewModel.addEvent(doggoEvent);

            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void pickDate(View view) {

        final DatePickerDialog datePickerDialog;
        datePickerDialog = new DatePickerDialog(view.getContext(),
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(java.util.Calendar.DAY_OF_MONTH));
        datePickerDialog.setTitle("Pick a date");
        datePickerDialog.show();
    }

    private void createGrid(ArrayList<DoggoEvent> events){
        ArrayList<Doggo> doggos = new ArrayList<>();
        for(DoggoEvent e:events){
            doggos.add(e.getCreator());
        }
        Log.i(TAG,"Doggos joining retrieved" + doggos);
        SearchImageAdapter sa = new SearchImageAdapter(((MainActivity) getActivity()).getApplicationContext(), ((MainActivity) getActivity()),doggos );
        sa.setZone(true);
        sa.setComesIn(setAttendances(events));
        //sa.setDoggoEvents(events);
        binding.gridView.setAdapter(sa);
        binding.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                ((MainActivity)getActivity()).toOtherProfile(position, 2);
            }
        });
    }

    private ArrayList<String> setAttendances(ArrayList<DoggoEvent> events){
        ArrayList<String> strings = new ArrayList<>();
        for(DoggoEvent e : events){
            LocalTime time = e.getTime().toLocalTime();
            strings.add(calculateAttendance(time));
        }
        return strings;
    }

    private String calculateAttendance(LocalTime time){
        LocalTime now = LocalTime.now();
        int diffSec = time.toSecondOfDay() - now.toSecondOfDay();
        if(diffSec <= 0 && !selectedDate.isAfter(LocalDateTime.now())){
            return "jetzt";
        }else if(diffSec > 2 * 60 * 60 || selectedDate.isAfter(LocalDateTime.now())){
            //More than 2h
            return "at " + time.format(DateTimeFormatter.ofPattern("HH:mm"));

        }else {
            if(diffSec > 60 * 60){
                //more than 1h
                int min =Math.floorDiv((diffSec-3600),60) + 1;
                return "in 1h " + min + " m";
            }else {
                int min = Math.floorDiv(diffSec,60) + 1;
                return "in " + min + " m";
            }
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if(date.size()==4){
            date.remove(date.size()-1);
            date.remove(2);
        }else {
            date.remove(date.size()-1);
        }
        Calendar c = Calendar.getInstance();
        c.set(year,month,dayOfMonth);
        LocalDate temp = LocalDateTime.ofInstant(c.toInstant(), ZoneId.systemDefault()).toLocalDate();
        date.add(temp.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        date.add("Pick a date");
        arrayAdapter.notifyDataSetChanged();
        spinner.setSelection(2);
        pickedDate = LocalDateTime.of(temp,LocalTime.of(0,0,0));
        selectedDate = pickedDate;
        doggoZoneViewModel.getListDoggosJoining(selectedDoggoZone,pickedDate);
    }
}
