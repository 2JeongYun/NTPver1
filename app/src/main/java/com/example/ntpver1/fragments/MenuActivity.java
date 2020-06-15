package com.example.ntpver1.fragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.ntpver1.MapActivity;
import com.example.ntpver1.R;
import com.example.ntpver1.adapter.RecommendedAlgoritm;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MenuActivity extends AppCompatActivity {
    private static final String TAG = "MenuActivity";
    private static final int REQUEST_MAP_ACTIVITY = 3;

    public static Context mContext;

    private MenuActivity thisClass = this;
    private Activity menuAct = this;

    MyInfoFragment myInfoFragment;
    SearchFragment searchFragment;
    CardInfoFragment cardInfoFragment;
    BottomNavigationView bottomNavigationView;

    RecommendedAlgoritm recommendedAlgoritm = RecommendedAlgoritm.getInstance(thisClass);

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

        //하단바 네비게이션 리스너
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.home_tab:
                                Log.d(TAG, "home_tab selected");
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, myInfoFragment).commit();
                                return true;

                            case R.id.recommend_tab:
                                Log.d(TAG, "search_tab selected");
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, searchFragment).commit();
                                return true;

                            case R.id.map_tap:
                                Log.d(TAG, "map_tab selected");
                                bottomNavigationView.setSelectedItemId(R.id.home_tab);
                                Intent intent = new Intent(MenuActivity.this, MapActivity.class);
                                startActivityForResult(intent, REQUEST_MAP_ACTIVITY);

                                return true;
                        }
                        return false;
                    }
                }
        );
    }

    public void callMyInfoFragment(String method) {
        switch (method) {
            case "refreshList":
                myInfoFragment.refreshList();
            case "setStatistics":
                myInfoFragment.setStatistics();
        }
    }

    public void callSearchFragment(String method) {
        switch (method) {
            case "setRecomendData":
                searchFragment.setRecomendData();
        }
    }

    public void onTabSelected(int position) {
        if (position == 0) {
            bottomNavigationView.setSelectedItemId(R.id.home_tab);
        } else if (position == 1) {
            bottomNavigationView.setSelectedItemId(R.id.recommend_tab);
        } else if (position == 2) {
            bottomNavigationView.setSelectedItemId(R.id.map_tap);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        onTabSelected(0);
    }

    public void startCardInfoFragment(String cardType) {
        Bundle bundle = new Bundle();
        bundle.putString("cardType", cardType);
        cardInfoFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(  R.id.container, cardInfoFragment).commit();
    }

    public interface OnBackPressedListener {
        public void onBack();
    }

    private OnBackPressedListener mBackListener;
    public void setOnBackPressedListener(OnBackPressedListener listener) {
        mBackListener = listener;
    }

    @Override
    public void onBackPressed() {
        if (mBackListener != null) {
            mBackListener.onBack();
        } else {
            super.onBackPressed();
        }
    }

    public MyInfoFragment getMyInfoFragment() {
        return myInfoFragment;
    }
    public Activity getActivity() {
        return menuAct;
    }

    public Context getContext() {
        return mContext;
    }
}