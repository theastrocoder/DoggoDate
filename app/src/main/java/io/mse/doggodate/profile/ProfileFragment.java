package io.mse.doggodate.profile;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import io.mse.doggodate.entity.Doggo;
import io.mse.doggodate.MainActivity;
import io.mse.doggodate.R;
import io.mse.doggodate.adapters.ViewPagerAdapter;
import io.mse.doggodate.databinding.ProfileFragmentBinding;
import io.mse.doggodate.entity.DoggoEvent;
import io.mse.doggodate.entity.DoggoPOJO;
import io.mse.doggodate.helpers.HelperViewModel;
import io.mse.doggodate.search.FirestoreCallback;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private Doggo firebaseDoggo;
    private TabLayout tabs;
    ViewPagerAdapter viewPagerAdapter;
    ViewPager viewPager;
    private MainActivity mainActivity;
    private ProfileViewModel profileViewModel;
    private Observer<Doggo> doggoObserver;
    public ProfileFragment() {}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        final ProfileFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.profile_fragment, container, false);

        //set handler and fragment manager for view pager in tab layout
        //binding.setHandler(this);
        //binding.setManager(getFragmentManager());
        final Doggo temp = new Doggo();
        binding.setDoggo(temp);
        final HelperViewModel hv = ViewModelProviders.of(getActivity()).get(HelperViewModel.class);

        doggoObserver = new Observer<Doggo>() {
            @Override
            public void onChanged(@Nullable final Doggo activeDoggo) {
                // Update the UI, in this case,binding.
                binding.setDoggo(activeDoggo);
                hv.setCurrentDoggo(activeDoggo);
                final ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager(), activeDoggo);
                binding.pager.setAdapter(adapter);


            }
        };
        ProfileFirestoreCallback afterDoggoRetrieved = new ProfileFirestoreCallback() {
            @Override
            public void onDataRetrieved(Doggo doggo) {
                firebaseDoggo = doggo;
                Log.i("ProfileFragment", "sizeeeeeeeeee"+firebaseDoggo.getPhotos().size());
                hv.setCurrentDoggo(doggo);
                final ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager(), doggo);
                binding.pager.setAdapter(adapter);

            }

            @Override
            public void onDataRetrieved(ArrayList<DoggoEvent> events) {
                hv.setCurrentDoggoEvents(events);
                Log.i("ProfileFragment", "THE LOADED EVENTS ARE OF SIZE " + events.size());

            }


        };

       profileViewModel.getMyEvents(afterDoggoRetrieved, "97XuSnfcOmfW8pKF7B8y");
        profileViewModel.getActiveDoggo(afterDoggoRetrieved).observe(this, doggoObserver);


        mainActivity = (MainActivity)getActivity();
        mainActivity.invalidateOptionsMenu();
        mainActivity.getSupportActionBar().setTitle("My Profile");
        return binding.getRoot();
    }



    @BindingAdapter({"bind:pager"})
    public static void bindViewPagerTabs(final TabLayout view, final ViewPager pagerView)
    {
        view.setupWithViewPager(pagerView, true);
    }


    private Doggo getFirebaseDoggo() {
        return this.firebaseDoggo;
    }

    private ProfileViewModel getProfileViewModel() {
        return this.profileViewModel;
    }




}