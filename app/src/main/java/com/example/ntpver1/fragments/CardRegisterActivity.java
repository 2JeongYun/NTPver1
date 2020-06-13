package com.example.ntpver1.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.example.ntpver1.R;

public class CardRegisterActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_user_register);
    }
}
