package io.mse.doggodate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import io.mse.doggodate.Entity.Doggo;
import io.mse.doggodate.adapters.CustomAdapter;

public class ListHelperFragment extends Fragment {


    ListView list;
    private Doggo selectedDoggo;
    private Boolean followers;
    public ListHelperFragment() {

    }


    public void setSelectedDoggo(Doggo selectedDoggo) {
        this.selectedDoggo = selectedDoggo;
    }

    public Boolean isFollowers() {
        return followers;
    }

    public void setFollowers(Boolean followers) {
        this.followers = followers;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        list = (ListView) view.findViewById(R.id.list);
        ArrayList<String> stringList = new ArrayList();

        if (followers != null && followers) {
            for (int i = 0; i < selectedDoggo.getFollowers().size(); i++) {
                stringList.add(selectedDoggo.getFollowers().get(i).getName());
            }
        } else if (followers != null && !followers){
            for (int i = 0; i < selectedDoggo.getFollowings().size(); i++) {
                stringList.add(selectedDoggo.getFollowings().get(i).getName());
            }
        }else if (followers == null){
            for (int i = 0; i < selectedDoggo.getEvents().size(); i++) {
                stringList.add(selectedDoggo.getEvents().get(i).getZone().getName() + " " + selectedDoggo.getEvents().get(i).getDateTime().format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy")) );
            }
        }

        CustomAdapter adapter = new CustomAdapter(stringList,(AppCompatActivity)getActivity());
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                openProfile(position);
            }
        });
        return view;
    }

    private void openProfile(int position) {
        ((MainActivity)getActivity()).toOtherProfile(position);
    }
}
