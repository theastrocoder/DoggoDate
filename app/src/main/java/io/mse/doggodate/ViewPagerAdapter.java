package io.mse.doggodate;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import io.mse.doggodate.Entity.Doggo;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private Doggo selectedDoggo;

    public ViewPagerAdapter(FragmentManager fm, Doggo selectedDoggo) {
        super(fm);
        this.selectedDoggo = selectedDoggo;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new GridHelperFragment();
            ((GridHelperFragment) fragment).setSearch(false);
            ((GridHelperFragment) fragment).setSelectedDoggo(this.selectedDoggo);

            Log.i("ViewPagerAdapter", "first tab");
        }
        else if (position == 2)
        {
            fragment = new ListHelperFragment();
            ((ListHelperFragment) fragment).setFollowers(false);
            ((ListHelperFragment) fragment).setSelectedDoggo(this.selectedDoggo);
            Log.i("ViewPagerAdapter", "second tab");

        }
        else if (position == 3)
        {
            fragment = new ListHelperFragment();
            ((ListHelperFragment) fragment).setFollowers(true);
            ((ListHelperFragment) fragment).setSelectedDoggo(this.selectedDoggo);
            Log.i("ViewPagerAdapter", "third tab");

        } else if (position == 1)
        {
            fragment = new ListHelperFragment();
            ((ListHelperFragment) fragment).setFollowers(null);
            ((ListHelperFragment) fragment).setSelectedDoggo(this.selectedDoggo);
            Log.i("ViewPagerAdapter", "forth tab");

        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
        {
            title = selectedDoggo.getPhotos().size() +" Photos";
        }
        else if (position == 2)
        {
            title = selectedDoggo.getEvents().size() +" Doggos";
        }
        else if (position == 3)
        {
            title = selectedDoggo.getFollowers().size() +" Flwrz";
        }
        else if (position == 1)
        {
            title = selectedDoggo.getFollowers().size() +" Events";
        }
        return title;
    }
}