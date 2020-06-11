package com.example.ntpver1.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ntpver1.DBManager;
import com.example.ntpver1.R;
import com.example.ntpver1.adapter.CardAdapter;
import com.example.ntpver1.adapter.ConsumptionAdapter;
import com.example.ntpver1.item.Card;
import com.example.ntpver1.item.Consumptionlist;
import com.example.ntpver1.login.login.LoginManager;

import java.util.Date;

public class CardInfoFragment extends Fragment {
    private static final String TAG = "CardInfofragment";
    private static ConsumptionAdapter consumptionAdapter;
    RecyclerView consumptionRecyclerView;
    DBManager dbManager = DBManager.getInstance();
    LoginManager loginManager = LoginManager.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView() is called");

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_card_info, container, false);

        setUI(rootView);

        return rootView;
    }

    private void setUI(ViewGroup rootView) {
        setRecyclerView(rootView);
//        try {
//            dbManager.setSearchConsumptionlistValue(loginManager.getUser().getUserEmail(), loginManager.getUser().get());
//            dbManager.readConsumptionlistData();
//        } catch (Exception e) {
//            Log.d(TAG, "setUI, ERROR : can't read Consumption data");
//        }
    }

    //리싸이클러뷰
    protected void setRecyclerView(ViewGroup rootView) {
        consumptionRecyclerView = rootView.findViewById(R.id.consumption_list);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        consumptionRecyclerView.setLayoutManager(layoutManager);

        consumptionRecyclerView.setAdapter(consumptionAdapter);
    }

    public static ConsumptionAdapter getCsmptInstance() {
        if (consumptionAdapter == null) {
            consumptionAdapter = new ConsumptionAdapter();
        }

        consumptionAdapter.setClean();

        return consumptionAdapter;
    }
}
