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

import com.example.ntpver1.R;
import com.example.ntpver1.adapter.CardAdapter;
import com.example.ntpver1.adapter.ConsumptionAdapter;
import com.example.ntpver1.item.Card;
import com.example.ntpver1.item.Consumptionlist;

import java.util.Date;

public class CardInfoFragment extends Fragment {
    private static final String TAG = "CardInfofragment";
    RecyclerView consumptionRecyclerView;
    ConsumptionAdapter consumptionAdapter;

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

        //------------------TEST START------------------
        Card card = new Card();
        card.setCard_kinds("CARD TYPE");
        Consumptionlist consumptionlist = new Consumptionlist();
        consumptionlist.setBalance(30000);
        Date d = new Date();
        consumptionlist.setPay_date(new Date());
        consumptionlist.setBalance(3333);
        consumptionlist.setPay(33);
        consumptionlist.setStore_name("store_name");

        consumptionAdapter.addItem(consumptionlist);
        consumptionAdapter.addItem(consumptionlist);
        consumptionAdapter.addItem(consumptionlist);
        consumptionAdapter.addItem(consumptionlist);
        consumptionAdapter.addItem(consumptionlist);
        consumptionAdapter.addItem(consumptionlist);
        consumptionAdapter.addItem(consumptionlist);
        consumptionAdapter.addItem(consumptionlist);
        consumptionAdapter.addItem(consumptionlist);
        consumptionAdapter.addItem(consumptionlist);
        //------------------TEST END------------------
    }

    //리싸이클러뷰
    protected void setRecyclerView(ViewGroup rootView) {
        consumptionRecyclerView = rootView.findViewById(R.id.consumption_list);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        consumptionRecyclerView.setLayoutManager(layoutManager);

        consumptionAdapter = new ConsumptionAdapter();

        consumptionRecyclerView.setAdapter(consumptionAdapter);
    }
}
