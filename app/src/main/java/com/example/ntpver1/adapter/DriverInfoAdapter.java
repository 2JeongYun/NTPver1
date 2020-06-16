package com.example.ntpver1.adapter;

import android.app.SearchManager;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.ntpver1.R;
import com.example.ntpver1.item.Store;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class DriverInfoAdapter implements GoogleMap.InfoWindowAdapter {

    View window;
    Store store;

    public DriverInfoAdapter(View window, Store store){
        this.window = window;
        this.store = store;//정보를 담은 객체
    }

    @Override
    public View getInfoWindow(Marker marker) {

        TextView title = window.findViewById(R.id.marker_store_name);
        TextView address = window.findViewById(R.id.marker_store_address);
        TextView store_phone = window.findViewById(R.id.marker_store_phone);

        title.setText(store.getName());

        address.setText(store.getAddress());

        store_phone.setText(store.getPhone());

        return window;
    }

    @Override
    public View getInfoContents(Marker marker) {

        return null;

    }
}
