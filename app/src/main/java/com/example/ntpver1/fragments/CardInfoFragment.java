package com.example.ntpver1.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.example.ntpver1.login.login.User;

import java.util.ArrayList;
import java.util.Date;

public class CardInfoFragment extends Fragment implements MenuActivity.OnBackPressedListener {
    private static final String TAG = "CardInfofragment";
    private static ConsumptionAdapter consumptionAdapter;
    RecyclerView consumptionRecyclerView;
    DBManager dbManager = DBManager.getInstance();
    LoginManager loginManager = LoginManager.getInstance();
    User user;
    Card card;
    String cardType;

    ArrayList<Consumptionlist> consumptionlist;

    TextView payBalanceTextView;
    TextView payTypeTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView() is called");

        if (getArguments() != null) {
            cardType = getArguments().getString("cardType");
            Log.d(TAG, cardType);
        }

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_card_info, container, false);

        init(rootView);

        return rootView;
    }

    private void init(ViewGroup rootView) {
        user = loginManager.getUser();
        card = user.getCard(cardType);
        consumptionlist = card.getUsageHistory();
        consumptionAdapter = new ConsumptionAdapter();

        payTypeTextView = rootView.findViewById(R.id.pay_type);
        payBalanceTextView = rootView.findViewById(R.id.pay_balance);

        payTypeTextView.setText(card.getKoName(card.getCard_kinds()));
        payBalanceTextView.setText(Integer.toString(card.getBalance()));

        setRecyclerView(rootView);
    }

    //리싸이클러뷰
    protected void setRecyclerView(ViewGroup rootView) {
        consumptionRecyclerView = rootView.findViewById(R.id.consumption_list);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        consumptionRecyclerView.setLayoutManager(layoutManager);

        consumptionRecyclerView.setAdapter(consumptionAdapter);

        for (Consumptionlist cons : consumptionlist) {
            consumptionAdapter.addItem(cons);
        }
    }

    @Override
    public void onBack() {
        MenuActivity menuActivity = (MenuActivity)getActivity();
        menuActivity.setOnBackPressedListener(null);
        menuActivity.onTabSelected(0);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MenuActivity) MenuActivity.mContext).setOnBackPressedListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        ((MenuActivity) MenuActivity.mContext).setOnBackPressedListener(null);
    }
}
