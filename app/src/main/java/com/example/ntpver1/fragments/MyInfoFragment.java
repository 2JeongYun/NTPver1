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
import com.example.ntpver1.adapter.StoreAdapter;
import com.example.ntpver1.item.Card;

public class MyInfoFragment extends Fragment {
    private static final String TAG = "MyInfoFragment";
    RecyclerView cardRecyclerView;
    CardAdapter cardAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView() is called");

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_my_info, container, false);

        setUI(rootView);

        return rootView;
    }

    private void setUI(ViewGroup rootView) {
        setRecyclerView(rootView);

        //------------------TEST START------------------
        Card card1 = new Card();
        card1.setBalance(100000);
        card1.setCard_kinds("제로페이");


        Card card2 = new Card();
        card2.setBalance(10000);
        card2.setCard_kinds("경기페이");

        cardAdapter.addItem(card1);
        cardAdapter.addItem(card2);
        //------------------TEST END------------------
    }

    //리싸이클러뷰
    protected void setRecyclerView(ViewGroup rootView) {
        cardRecyclerView = rootView.findViewById(R.id.card_list);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        cardRecyclerView.setLayoutManager(layoutManager);

        cardAdapter = new CardAdapter();

        cardRecyclerView.setAdapter(cardAdapter);
    }

    public void refreshList() {
        Log.d(TAG, "refreshList() is called");
        cardAdapter.notifyDataSetChanged();
    }

}
