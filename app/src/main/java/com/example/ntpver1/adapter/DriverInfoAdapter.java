package com.example.ntpver1.adapter;

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
        return window;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
