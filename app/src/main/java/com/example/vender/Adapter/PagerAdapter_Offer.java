package com.example.vender.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.vender.Fragment.FragmentOffer_JobOffer;
import com.example.vender.Fragment.FragmentOffer_Hire;

public class PagerAdapter_Offer extends FragmentPagerAdapter {
    private int numberOfTabs;

    public PagerAdapter_Offer(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
    }


    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                FragmentOffer_JobOffer tab1 = new FragmentOffer_JobOffer();
                return tab1;
            case 1:
                FragmentOffer_Hire tab2 = new FragmentOffer_Hire();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return this.numberOfTabs;
    }
}
