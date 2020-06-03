package com.example.ntpver1.myinterface;

import android.view.View;

import com.example.ntpver1.adapter.ConsumptionAdapter;

public interface OnConsumptionItemClickListener {
    public void onItemClick(ConsumptionAdapter.ViewHolder holder, View view, int position);
}
