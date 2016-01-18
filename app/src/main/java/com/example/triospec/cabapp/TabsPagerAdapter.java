package com.example.triospec.cabapp;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Cortex Hub on 1/16/2016.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm){
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 1:
                //Driver fragment Activity
                return new DriverFragment();
            case 0:
                //Car Frament Activity
                return new CarFragment();
        }
        return null;
    }
    @Override
    public int getCount() {
        return 2;
    }
}
