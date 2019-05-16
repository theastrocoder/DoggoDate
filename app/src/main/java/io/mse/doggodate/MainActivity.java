package io.mse.doggodate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

import io.mse.doggodate.Entity.Doggo;
import io.mse.doggodate.Entity.DoggoZone;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    Doggo activeDog;
    ArrayList<Doggo> defaultSearchDoggos = new ArrayList<>();
    private DoggoZone park1;
    private DoggoZone park2;


    final Fragment fragment1 = new HomeFragment();
    final Fragment fragment2 = new MapFragment();
    final Fragment fragment3 = new SearchFragment();
    Fragment fragment4 = new ProfileFragment();
    Fragment otherProfileFragment = new OtherProfileFragment();;

    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment1;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {


            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fm.beginTransaction().hide(active).show(fragment1).commit();
                    active = fragment1;
                    item.setChecked(true);
                    ActionBar ab = getSupportActionBar();
                    ab.show();
                    ab.setHomeButtonEnabled(false);
                    ab.setDisplayHomeAsUpEnabled(false);
                    ab.setTitle("DoggoDate");

                    Log.i(TAG, "homescreen opened");

                    break;
                case R.id.navigation_map:
               /*Intent toMap = new Intent(MainActivity.this,MapActivity.class);
               startActivity(toMap);*/
                    item.setChecked(true);
                    fm.beginTransaction().hide(active).show(fragment2).commit();
                    active = fragment2;
                    getSupportActionBar().hide();
                    Log.i(TAG, "navigation opened");

                    break;
                case R.id.navigation_doggos:
              /* Intent toSearch = new Intent(MainActivity.this,SearchActivity.class);
               startActivity(toSearch);*/
                    fm.beginTransaction().hide(active).show(fragment3).commit();
                    active = fragment3;
                    item.setChecked(true);
                    getSupportActionBar().hide();
                    Log.i(TAG, "doggos opened");

                    break;
                case R.id.navigation_profile:
                    item.setChecked(true);
                    //fm.beginTransaction().hide(active).show(fragment4).commit();
                    getSupportActionBar().hide();
                    Log.i(TAG, "profile opened");


                    fragment4 = new ProfileFragment();
                    ((ProfileFragment) fragment4).setActiveDoggo(activeDog);
                    fm.beginTransaction().add(R.id.main_container, fragment4, "4").hide(fragment4).commit();
                    fm.beginTransaction().hide(active).show(fragment4).commit();
                    active = fragment4;


                    break;
            }
            return false;
        }
    };

    public ArrayList<Doggo> getDefaultSearch() {
        return defaultSearchDoggos;
    }

    public void setDefaultSearch(ArrayList<Doggo> defaultSearch) {
        this.defaultSearchDoggos = defaultSearch;
    }

    public void setActive(Fragment fragment, Fragment old) {
        Log.i(TAG, "Setting the active fragment other profile from search fragment");
        fm.beginTransaction().hide(active).show(fragment).commit();
        active = fragment;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        fm.beginTransaction().add(R.id.main_container, otherProfileFragment, "5").hide(otherProfileFragment).commit();
        fm.beginTransaction().add(R.id.main_container, fragment4, "4").hide(fragment4).commit();
        fm.beginTransaction().add(R.id.main_container, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.main_container, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.main_container, fragment1, "1").commit();

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Doggo Bonnie = new Doggo("Bonnie", "Golden retriever", R.drawable.profile_image);
        Doggo Alex = new Doggo("Alex", "Labrador",  R.drawable.labrador_profile);
        Doggo Chichi = new Doggo("Chichi", "Chivava", R.drawable.chivava_prof);
        Doggo Rex = new Doggo("Rex", "Wolfdog", R.drawable.wolfdog_profile);
        Doggo Akki = new Doggo("Akki", "Akita Inu", R.drawable.akita_profile);
        Doggo Alfonz = new Doggo("Alfonz", "Buldog", R.drawable.dog4);
        Doggo Nina = new Doggo("Nina", "Labrador", R.drawable.dog3);
        Doggo Bowie = new Doggo("Bowie", "Retriever", R.drawable.dog2);
        Doggo Makawa = new Doggo("Makawa", "Chivuavua", R.drawable.chivava_1);


        /** SETTING ACTIVE DOG ATTRIBUTES */
        activeDog = Bonnie;
        ArrayList<Doggo> followers = new ArrayList<>();
        followers.add(Alex);
        followers.add(Chichi);
        followers.add(Alfonz);
        followers.add(Nina);
        followers.add(Bowie);
        followers.add(Makawa);
        Bonnie.setFollowers(followers);

        ArrayList<Doggo> followings = new ArrayList<>();
        //followings.add(Rex);
        //followings.add(Akki);
        //followers.add(Bowie);
        Bonnie.setFollowings(followings);

        ArrayList<Integer> photos = new ArrayList<>();
        photos.add(R.drawable.golden2);
        photos.add(R.drawable.golden3);
        photos.add(R.drawable.golden4);
        photos.add(R.drawable.golden5);

        Bonnie.setPhotos(photos);
        Bonnie.setProfilePic(R.drawable.profile_image);
        ((ProfileFragment)fragment4).setActiveDoggo(activeDog);

        ArrayList<Integer> alexPhotos = new ArrayList<>();
        alexPhotos.add(R.drawable.labrador_1);
        alexPhotos.add(R.drawable.labrador_2);
        alexPhotos.add(R.drawable.labrador_3);
        alexPhotos.add(R.drawable.labrador_profile);
        Alex.setPhotos(alexPhotos);
        Alex.setFollowers(followers);
        Alex.setFollowings(followings);

        ArrayList<Integer> ChiChiPhotos = new ArrayList<>();
        ChiChiPhotos.add(R.drawable.chivava_1);
        ChiChiPhotos.add(R.drawable.chivava_2);
        ChiChiPhotos.add(R.drawable.chivava3);
        Chichi.setPhotos(ChiChiPhotos);
        Chichi.setFollowers(followers);
        Chichi.setFollowings(followings);

        ArrayList<Integer> rexPhotos = new ArrayList<>();
        rexPhotos.add(R.drawable.wd_2);
        rexPhotos.add(R.drawable.wd_1);
        rexPhotos.add(R.drawable.wd_3);
        rexPhotos.add(R.drawable.wd_4);
        Rex.setPhotos(rexPhotos);
        Rex.setFollowers(followers);
        Rex.setFollowings(followings);

        ArrayList<Integer> akkiPhotos = new ArrayList<>();
        akkiPhotos.add(R.drawable.akita_1);
        akkiPhotos.add(R.drawable.akita_2);
        akkiPhotos.add(R.drawable.akita_3);
        akkiPhotos.add(R.drawable.akita_4);
        akkiPhotos.add(R.drawable.akita_profile);
        Akki.setPhotos(akkiPhotos);
        Akki.setFollowers(followers);
        Akki.setFollowings(followers);

        Alfonz.setFollowings(followers);
        Nina.setFollowings(followings);
        Makawa.setFollowings(followings);
        Bowie.setFollowings(followers);
        Nina.setFollowers(followers);
        Makawa.setFollowers(followers);

        /** SETTING ALL DOGS LIST */
        //defaultSearchDoggos.add(Bonnie);
        defaultSearchDoggos.add(Alfonz);
        defaultSearchDoggos.add(Nina);
        defaultSearchDoggos.add(Makawa);
        defaultSearchDoggos.add(Bowie);
        defaultSearchDoggos.add(Akki);
        defaultSearchDoggos.add(Rex);
        defaultSearchDoggos.add(Chichi);
        defaultSearchDoggos.add(Alex);



        ((OtherProfileFragment)otherProfileFragment).setSelectedDoggo(Rex);


        /** Creating Doggo Zones*/
        park1 = new DoggoZone(48.239539376028745, 16.333220189004898,
                "Hugo-Wolf-Park" ,8169, false);
        park2 = new DoggoZone(48.22027912193935, 16.480529082068596,
                "GA Aspernstraße",323, true);
    }

    public Doggo getActiveDog() {
        return activeDog;
    }

    protected OnBackPressedListener onBackPressedListener;

    public DoggoZone getPark2() {
        return park2;
    }

    public DoggoZone getPark1() {
        return park1;
    }

    public interface OnBackPressedListener {
        void doBack();
    }

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    public Fragment getFragment(String integer) {
        switch(integer) {
            case "1":
                return fragment1;
            case "2":
                return fragment2;
            case "3":
                return fragment3;
            case "4":
                return fragment4;
            case "5":
                return otherProfileFragment;
        }
        return null;
    }
        public void toOtherProfile(int position) {
            getSupportActionBar().show();
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
            otherProfileFragment = new OtherProfileFragment();
            fm.beginTransaction().add(R.id.main_container, otherProfileFragment, "5").hide(otherProfileFragment).commit();
            Log.i("MainActivity", "selected dogs name is" + defaultSearchDoggos.get(position).getName());
            ((OtherProfileFragment)otherProfileFragment).setSelectedDoggo(defaultSearchDoggos.get(position));
            fm.beginTransaction().hide(active).show(otherProfileFragment).commit();
            active = otherProfileFragment;


        }
}
