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

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    final Fragment fragment1 = new HomeFragment();
    final Fragment fragment2 = new MapFragment();
    final Fragment fragment3 = new SearchFragment();
    final Fragment fragment4 = new ProfileFragment();
    final Fragment otherProfileFragment = new OtherProfileFragment();

    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment1;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            if (active == otherProfileFragment) {
                Log.i(TAG, "current fragment is profile");
            }
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
                    fm.beginTransaction().hide(active).show(fragment4).commit();
                    active = fragment4;
                    getSupportActionBar().hide();
                    Log.i(TAG, "profile opened");

                    break;
            }
            return false;
        }
    };

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

    }

    protected OnBackPressedListener onBackPressedListener;

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
        public void toOtherProfile() {
            fm.beginTransaction().hide(active).show(otherProfileFragment).commit();
            active = otherProfileFragment;
            getSupportActionBar().show();
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");

        }
}
