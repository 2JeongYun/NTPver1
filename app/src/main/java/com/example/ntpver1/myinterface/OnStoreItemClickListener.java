package com.example.ntpver1.myinterface;

import android.view.View;

import com.example.ntpver1.adapter.StoreAdapter;

public interface OnStoreItemClickListener {
    public void onItemClick(StoreAdapter.ViewHolder holder, View view, int position);
}
