package io.mse.doggodate.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import io.mse.doggodate.Entity.Doggo;
import io.mse.doggodate.MainActivity;
import io.mse.doggodate.R;
import io.mse.doggodate.adapters.ViewPagerAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private Doggo activeDoggo;
    private ImageView imageView;

    private TabLayout tabs;
    ViewPagerAdapter viewPagerAdapter;
    ViewPager viewPager;
    private MainActivity mainActivity;

    public ProfileFragment() {}

    public void setActiveDoggo(Doggo activeDoggo) {
        this.activeDoggo = activeDoggo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        mainActivity = (MainActivity)getActivity();
        mainActivity.invalidateOptionsMenu();


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