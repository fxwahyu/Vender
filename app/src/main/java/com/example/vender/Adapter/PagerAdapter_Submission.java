package com.example.vender.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.vender.Fragment.FragmentOffer_Hire;
import com.example.vender.Fragment.FragmentOffer_JobOffer;
import com.example.vender.Fragment.FragmentSubmission;
import com.example.vender.Fragment.FragmentSubmissionJob;
import com.example.vender.Fragment.FragmentSubmissionRecruiting;

public class PagerAdapter_Submission extends FragmentPagerAdapter {

    private int numberOfTabs;

    public PagerAdapter_Submission(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.numberOfTabs = 2;
    }


    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                FragmentSubmissionJob tab1 = new FragmentSubmissionJob();
                return tab1;
            case 1:
                FragmentSubmissionRecruiting tab2 = new FragmentSubmissionRecruiting();
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
