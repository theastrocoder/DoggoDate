package io.mse.doggodate;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new GridHelperFragment();
            Log.i("ViewPagerAdapter", "first tab");
        }
        else if (position == 1)
        {
            fragment = new ListHelperFragment();
            Log.i("ViewPagerAdapter", "second tab");

        }
        else if (position == 2)
        {
            fragment = new ListHelperFragment();
            Log.i("ViewPagerAdapter", "third tab");

        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
        {
            title = "Photos";
        }
        else if (position == 1)
        {
            title = "Followings";
        }
        else if (position == 2)
        {
            title = "Followers";
        }
        return title;
    }
}