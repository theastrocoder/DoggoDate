package io.mse.doggodate.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import io.mse.doggodate.databinding.OtherProfileFragmentBinding;
import io.mse.doggodate.databinding.ProfileFragmentBinding;
import io.mse.doggodate.entity.Doggo;
import io.mse.doggodate.MainActivity;
import io.mse.doggodate.R;
import io.mse.doggodate.adapters.ViewPagerAdapter;
import io.mse.doggodate.search.SearchFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class OtherProfileFragment extends Fragment implements MainActivity.OnBackPressedListener {

    private Doggo selectedDoggo;
    private ImageView imageView;
    private TextView name;
    private TextView breed;
    private TabLayout tabs;
    private Button followButton;
    ViewPagerAdapter viewPagerAdapter;
    ViewPager viewPager;
    private OtherProfileViewModel otherProfileViewModel;
    private ProfileViewModel profileViewModel;

    public OtherProfileFragment() {}

    public Doggo getSelectedDoggo() {
        return selectedDoggo;
    }

    public void setSelectedDoggo(Doggo selectedDoggo) {
        this.selectedDoggo = selectedDoggo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        otherProfileViewModel = ViewModelProviders.of(this).get(OtherProfileViewModel.class);

        selectedDoggo = ((MainActivity)getActivity()).getSelectedDog();

        ((MainActivity)getActivity()).invalidateOptionsMenu();

        final OtherProfileFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.other_profile_fragment, container, false);
        View view =  binding.getRoot();
        binding.setDoggo(selectedDoggo);

        Observer selectedDoggoObserver = new Observer<Doggo>() {
            @Override
            public void onChanged(@Nullable final Doggo selectedDoggo) {
                // Update the UI, in this case,binding.
                binding.setDoggo(selectedDoggo);
            }
        };
        otherProfileViewModel.getSelectedDoggo().observe(this, selectedDoggoObserver);

        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter( getChildFragmentManager(), this.selectedDoggo);
        viewPager.setAdapter(viewPagerAdapter);

        tabs = (TabLayout) view.findViewById(R.id.tabLayout);
        tabs.setupWithViewPager(viewPager);


        followButton = (Button)view.findViewById(R.id.followButton);
        followButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                   MutableLiveData<Doggo> d = profileViewModel.getActiveDoggo();
                    otherProfileViewModel.setSelectedFirebaseDoggo(d.getValue());
                    //binding.setDoggo(d);
                    followButton.setText("Followed");

            }});

        if (((MainActivity)getActivity()).getActiveDog().getFollowings().contains(this.selectedDoggo)) {
        } else {
            followButton.setText("Follow");
        }

        return view;
    }

    public void follow(View view){
        ((MainActivity)getActivity()).getActiveDog().getFollowings().add(this.selectedDoggo);
        this.selectedDoggo.getFollowers().add(((MainActivity)getActivity()).getActiveDog());

        //((MainActivity)getActivity()).updateOtherProfileFragment(this.selectedDoggo);
        Toast.makeText(getContext(),"You started following " + this.selectedDoggo.getName(),Toast.LENGTH_SHORT).show();

       /* // Setting Alert Dialog Title
        alertDialogBuilder.setTitle("You started following " + this.selectedDoggo.getName());
        // Icon Of Alert Dialog
        //alertDialogBuilder.setIcon(R.drawable.question);
        // Setting Alert Dialog Message
        //alertDialogBuilder.setMessage("Are you sure,You want to exit");
        alertDialogBuilder.setCancelable(true);

        alertDialogBuilder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        Log.i("OtherProfileFragment", "you follow the Doggo" + this.selectedDoggo.getName());*/
    }



 @Override
    public void doBack() {
            // I'm viewing Fragment C
            getFragmentManager().popBackStack();
            ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
     ((MainActivity)getActivity()).setActive(new SearchFragment(), this);

        }

    }

