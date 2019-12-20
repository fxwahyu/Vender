package com.example.vender.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.vender.Fragment.FragmentFindEvent;
import com.example.vender.Fragment.FragmentFindTalent;

public class PagerAdapter_Find extends FragmentPagerAdapter {
    private int numberOfTabs;

    public PagerAdapter_Find(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                FragmentFindEvent tab1 = new FragmentFindEvent();
                return tab1;
            case 1:
                FragmentFindTalent tab2 = new FragmentFindTalent();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
