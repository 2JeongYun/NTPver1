package com.example.ntpver1.myinterface;

import android.view.View;

import com.example.ntpver1.adapter.CardAdapter;
import com.example.ntpver1.adapter.ConsumptionAdapter;

public interface OnCardItemClickListener {
    public void onItemClick(CardAdapter.ViewHolder holder, View view, int position);
}
