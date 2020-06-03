package com.example.ntpver1.fragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.ntpver1.MapActivity;
import com.example.ntpver1.R;
import com.example.ntpver1.myinterface.OnTabItemSelectedListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MenuActivity extends AppCompatActivity implements OnTabItemSelectedListener {
    private static final String TAG = "MenuActivity";
    public static Context mContext;

    MyInfoFragment myInfoFragment;
    SearchFragment searchFragment;
    CardInfoFragment cardInfoFragment;
    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        init();
    }

    private void init() {
        mContext = this;
        myInfoFragment = new MyInfoFragment();
        searchFragment = new SearchFragment();
        cardInfoFragment = new CardInfoFragment();

        //프래그먼트 매니저
        getSupportFragmentManager().beginTransaction().replace(  R.id.container, myInfoFragment).commit();

        //하단바 네비게이션 리스터
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.home_tab:
                                Log.d(TAG, "home_tab selected");
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, myInfoFragment).commit();
                                return true;

                            case R.id.search_tab:
                                Log.d(TAG, "search_tab selected");
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, searchFragment).commit();
                                return true;

                            case R.id.map_tab:
                                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                                startActivity(intent);
                                return true;
                        }
                        return false;
                    }
                }
        );
    }

    public void onTabSelected(int position) {
        if (position == 0) {
            bottomNavigation.setSelectedItemId(R.id.home_tab);
        } else if (position == 1) {
            bottomNavigation.setSelectedItemId(R.id.search_tab);
        } else if (position == 2) {
            bottomNavigation.setSelectedItemId(R.id.map_tab);
        }
    }

    public void startCardInfoFragment() {
        getSupportFragmentManager().beginTransaction().replace(  R.id.container, cardInfoFragment).commit();
    }
}
