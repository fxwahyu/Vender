package com.example.vender.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vender.Adapter.PagerAdapter_Find;
import com.example.vender.Adapter.PagerAdapter_Offer;
import com.example.vender.R;

public class FragmentOffer extends Fragment {
    private View view;
    private ViewPager viewPager;
    Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_offer, container, false);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tablayout_offer);
        tabLayout.addTab(tabLayout.newTab().setText("Offer from Event"));
        tabLayout.addTab(tabLayout.newTab().setText("Offer from Talent"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("Offer List");

        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager_offer);
        final PagerAdapter_Offer adapter = new PagerAdapter_Offer
                (getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        return view;

    }
}
