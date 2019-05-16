package io.mse.doggodate;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import io.mse.doggodate.Entity.DoggoZone;

public class DoggoZoneFragment extends Fragment {

    private DoggoZone selectedDogoZone;

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

        parkName.setText(selectedDogoZone.getName());
        parkArea.setText("Area: " + String.valueOf(selectedDogoZone.getSurface() + "m2"));
        type.setText("Type: Park");
        fence.setText("Fence: Ja");

        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource((MainActivity)getContext(), R.array.days, android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        GridView gridView = (GridView) view.findViewById(R.id.grid_view);
        ArrayList<Integer> images = new ArrayList<>();
        //set which dogs' pics should be shown
        for (int i=0;i<((MainActivity)getActivity()).getDefaultSearch().size();i++){
            images.add(((MainActivity)getActivity()).getDefaultSearch().get(i).getProfilePic());
        }
        gridView.setAdapter(new ImageAdapter(getActivity().getApplicationContext(), (AppCompatActivity) getActivity(),images));

        return view;
    }

    public DoggoZone getSelectedDogoZone() {
        return selectedDogoZone;
    }

    public void setSelectedDogoZone(DoggoZone selectedDogoZone) {
        this.selectedDogoZone = selectedDogoZone;
    }
}
