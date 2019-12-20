package com.example.vender.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.vender.Fragment.FragmentFind;
import com.example.vender.Fragment.FragmentOffer;
import com.example.vender.Fragment.FragmentProfile;
import com.example.vender.Fragment.FragmentSubmission;
import com.example.vender.GlobalUser;
import com.example.vender.Model.User;
import com.example.vender.R;

import java.io.Serializable;

public class HomeActivity extends AppCompatActivity implements Serializable {
    User user;
    GlobalUser g = GlobalUser.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
//        Intent intent = getIntent();
//        user = (User)intent.getSerializableExtra("user");
        user = g.getUser();

        Intent i = getIntent();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentFind()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;


            switch(menuItem.getItemId()){
                case R.id.find :
                    selectedFragment = new FragmentFind();
                    break;
                case R.id.offer :
                    selectedFragment = new FragmentOffer();
                    break;
                case R.id.submission :
                    selectedFragment = new FragmentSubmission();
                    break;
//                case R.id.message :
//                    selectedFragment = new FragmentMessage();
//                    break;
                case R.id.profile :
                    selectedFragment = new FragmentProfile();
//                    Toast.makeText(HomeActivity.this, g.getUser().getId()+"\n"+g.getUser().getNama(), Toast.LENGTH_SHORT).show();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        }
    };
}
