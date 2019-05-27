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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;

import io.mse.doggodate.Entity.DoggoZone;
import io.mse.doggodate.adapters.ImageAdapter;
import io.mse.doggodate.MainActivity;
import io.mse.doggodate.R;

public class DoggoZoneFragment extends Fragment {

    private DoggoZone selectedDogoZone;
    private ArrayList<String> date = new ArrayList<>(Arrays.asList("Today", "Tomorrow", "Pick a date"));
    Spinner spinner;
    ArrayAdapter<String> arrayAdapter;
    public DoggoZoneFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.doggozone_fragment,container,false);
        TextView parkName = (TextView) view.findViewById(R.id.park_name);
        TextView parkArea = (TextView) view.findViewById(R.id.area);
        TextView type = (TextView) view.findViewById(R.id.type);
        TextView fence = (TextView) view.findViewById(R.id.fence);
        TextView doggos = (TextView) view.findViewById(R.id.doggos_joining);

        parkName.setText(selectedDogoZone.getName());
        parkArea.setText("Area: " + String.valueOf(selectedDogoZone.getSurface() + "m2"));
        type.setText("Type: Dog Zone");
        fence.setText("Fence: Yes");
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
                        break;
                    case 1:
                        Toast.makeText(getContext(),"Doggos at Park Tomorrow",Toast.LENGTH_LONG).show();
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

        GridView gridView = (GridView) view.findViewById(R.id.grid_view);
        ArrayList<Integer> images = new ArrayList<>();
        //set which dogs' pics should be shown
        for (int i = 0; i<((MainActivity)getActivity()).getDefaultSearch().size(); i++){
            images.add(((MainActivity)getActivity()).getDefaultSearch().get(i).getProfilePic());
        }

        gridView.setAdapter(new ImageAdapter(getActivity().getApplicationContext(), (AppCompatActivity) getActivity(),images));
        FloatingActionButton scheduleWalk = (FloatingActionButton) view.findViewById(R.id.schedule_walk);
        scheduleWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickTime();
            }
        });

        //gridView.setAdapter(new SearchImageAdapter(getActivity().getApplicationContext(), (MainActivity) getActivity()));


        return view;
    }

    public DoggoZone getSelectedDogoZone() {
        return selectedDogoZone;
    }

    public void setSelectedDogoZone(DoggoZone selectedDogoZone) {
        this.selectedDogoZone = selectedDogoZone;
    }

    private void pickTime() {

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                Toast.makeText(getContext(),"Scheduled walk in " + selectedHour + ":" + selectedMinute,Toast.LENGTH_LONG).show();
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void pickDate() {

        Calendar currentDate = Calendar.getInstance();
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
                Log.i("DogooZone","ARRAy " + date);
            }
        },year,month,day);
        datePickerDialog.setTitle("Pick a date");
        datePickerDialog.show();
    }
}
