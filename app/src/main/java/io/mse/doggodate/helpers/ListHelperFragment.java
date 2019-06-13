package io.mse.doggodate.helpers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import io.mse.doggodate.MainActivity;
import io.mse.doggodate.R;
import io.mse.doggodate.databinding.FragmentListBinding;
import io.mse.doggodate.adapters.CustomAdapter;
import io.mse.doggodate.entity.DoggoEvent;

public class ListHelperFragment extends Fragment {

    private Boolean followers;
    public ListHelperFragment() {

    }



    public Boolean isFollowers() {
        return followers;
    }

    public void setFollowers(Boolean followers) {
        this.followers = followers;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final FragmentListBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false);
        final HelperViewModel helperViewModel = ViewModelProviders.of(getActivity()).get(HelperViewModel.class);

        //create and bind empty list
        final ArrayList<String> stringList = new ArrayList();
        stringList.add("tryyyyyy");
        CustomAdapter adapter = new CustomAdapter(stringList,(AppCompatActivity)getActivity());
        binding.list.setAdapter(adapter);

        //bind listener
        binding.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                openProfile(position, 1);
            }
        });
       /* if (followers != null && followers) {
            for (int i = 0; i < selectedDoggo.getFollowers().size(); i++) {
                stringList.add(selectedDoggo.getFollowers().get(i).getName());
            }
        } else if (followers != null && !followers){
            for (int i = 0; i < selectedDoggo.getFollowings().size(); i++) {
                stringList.add(selectedDoggo.getFollowings().get(i).getName());
            }
        }else if (followers == null){
            for (int i = 0; i < selectedDoggo.getEvents().size(); i++) {
                stringList.add(selectedDoggo.getEvents().get(i).getZone().getName() + " " + selectedDoggo.getEvents().get(i).getTime().format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy")) );
            }
        }*/

       //setup observer on list of events of current doggo
        helperViewModel.getCurrentDoggoEvents().observe(this,  new Observer<ArrayList<DoggoEvent>>() {
            @Override
            public void onChanged(@Nullable final ArrayList<DoggoEvent> events) {
                // Update the UI, in this case,binding.

                //get string of new list of events
                for (int i = 0; i < events.size(); i++) {
                    //stringList.add(events.get(i).getZone().getName() + " " + events.get(i).getTime().format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy")) );
               stringList.add(events.get(i).getTime().format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy")));
                }

                //project changes to UI
                binding.list.setAdapter(new CustomAdapter(stringList,(AppCompatActivity)getActivity()));
                binding.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View v,
                                            int position, long id) {

                        openProfile(position, 1);
                    }
                });            }
        });
        return binding.getRoot();
    }

    private void openProfile(int position, int type) {
        ((MainActivity)getActivity()).toOtherProfile(position, type);
    }
}
