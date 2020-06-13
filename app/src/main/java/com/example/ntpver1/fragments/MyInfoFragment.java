package com.example.ntpver1.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ntpver1.DBManager;
import com.example.ntpver1.R;
import com.example.ntpver1.adapter.CardAdapter;
import com.example.ntpver1.adapter.StoreAdapter;
import com.example.ntpver1.item.Card;
import com.example.ntpver1.login.login.LoginManager;
import com.example.ntpver1.login.register.UserRegisterActivity;

import org.json.JSONException;

import java.util.concurrent.ExecutionException;

public class MyInfoFragment extends Fragment {
    private static final String TAG = "MyInfoFragment";
    private static CardAdapter cardAdapter;
    RecyclerView cardRecyclerView;
    Button registCardButton;
    DBManager dbManager;
    LoginManager loginManager = LoginManager.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView() is called");

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_my_info, container, false);
        dbManager = DBManager.getInstance();

        setUI(rootView);

        return rootView;
    }

    private void setUI(ViewGroup rootView) {
        setRecyclerView(rootView);
        registCardButton = rootView.findViewById(R.id.regist_card);

        try {
            dbManager.setSearchCardValue(loginManager.getUser().getUserEmail());
            dbManager.readCardData();
        } catch (Exception e) {
            Log.d(TAG, "setUI, ERROR : can't read card data");
        }

        registCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CardRegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    //리싸이클러뷰
    protected void setRecyclerView(ViewGroup rootView) {
        cardRecyclerView = rootView.findViewById(R.id.card_list);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        cardRecyclerView.setLayoutManager(layoutManager);

        cardRecyclerView.setAdapter(cardAdapter);
    }

    public void refreshList() {
        Log.d(TAG, "refreshList() is called");
        cardAdapter.notifyDataSetChanged();
    }

    public static CardAdapter getCardAdapterInstance() {
        if (cardAdapter == null) {
            cardAdapter = new CardAdapter();
        }

        cardAdapter.setClean();

        return cardAdapter;
    }

}
