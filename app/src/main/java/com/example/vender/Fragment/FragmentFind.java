package com.example.vender.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;

import com.example.vender.Adapter.PagerAdapter_Find;
import com.example.vender.R;

public class FragmentFind extends Fragment {
    private View view;
    private ViewPager viewPager;
    private Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_find, container, false);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tablayout_find);
        tabLayout.addTab(tabLayout.newTab().setText("Event"));
        tabLayout.addTab(tabLayout.newTab().setText("Talent"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("Find");

        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager_find);
        final PagerAdapter_Find adapter = new PagerAdapter_Find
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


//        BottomNavigationView topNav = view.findViewById(R.id.talentevent_navigation);
//        getChildFragmentManager().beginTransaction().replace(R.id.fragment_container_talentevent, new FragmentFindEvent()).commit();
//        topNav.setOnNavigationItemSelectedListener(navListener);

        return view;

    }

    public int getPage(){
        return 0;
    }

    /*private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;

            switch(menuItem.getItemId()){
                case R.id.eventmenu :
                    selectedFragment = new FragmentFindEvent();
                    break;
                case R.id.talentmenu :
                    selectedFragment = new FragmentFindTalent();
                    break;
            }

            getChildFragmentManager().beginTransaction().replace(R.id.fragment_container_talentevent, selectedFragment).commit();
            return true;
        }
    };*/

}