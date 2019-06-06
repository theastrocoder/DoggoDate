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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import io.mse.doggodate.entity.Doggo;
import io.mse.doggodate.MainActivity;
import io.mse.doggodate.R;
import io.mse.doggodate.adapters.ViewPagerAdapter;
import io.mse.doggodate.databinding.ProfileFragmentBinding;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private Doggo activeDoggo;
    private Doggo firebaseDoggo;
    private ImageView imageView;
    private TextView nameTextView;
    private TabLayout tabs;
    ViewPagerAdapter viewPagerAdapter;
    ViewPager viewPager;
    private MainActivity mainActivity;
    private ProfileViewModel profileViewModel;
    private Observer<Doggo> nameObserver;
    public ProfileFragment() {}
    public void setActiveDoggo(Doggo activeDoggo) {
        this.activeDoggo = activeDoggo;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final ProfileFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.profile_fragment, container, false);
        View view = binding.getRoot();

        //profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);

        final Doggo temp = new Doggo();
        binding.setDoggo(temp);
        nameObserver = new Observer<Doggo>() {
            @Override
            public void onChanged(@Nullable final Doggo activeDoggo) {
                // Update the UI, in this case,binding.
                temp.setName(activeDoggo.getName());
                binding.setDoggo(activeDoggo);
            }
        };
        profileViewModel.getActiveDoggo().observe(this, nameObserver);

        Button button2 = (Button) view.findViewById(R.id.button) ;
        button2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Doggo d = new Doggo();
                d.setName("tyrdgjfkghjk123123121");
                profileViewModel.getActiveDoggo().setValue(d);

            }});

        mainActivity = (MainActivity)getActivity();
        mainActivity.invalidateOptionsMenu();
        mainActivity.getSupportActionBar().setTitle("My Profile");
        activeDoggo = ((MainActivity) getActivity()).getActiveDog();

       //nameTextView.setText(firebaseDoggo.getName());
        imageView = (ImageView) view.findViewById(R.id.profile_image);
        activeDoggo = ((MainActivity)getActivity()).getActiveDog();
        imageView.setImageResource(activeDoggo.getProfilePic());


        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter( getChildFragmentManager(), this.activeDoggo);
        viewPager.setAdapter(viewPagerAdapter);

        tabs = (TabLayout) view.findViewById(R.id.tabLayout);
        tabs.setupWithViewPager(viewPager);

        FloatingActionButton button =(FloatingActionButton) view.findViewById(R.id.followButton);
        button.hide();


        return view;
    }



}