package com.example.ntpver1.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.example.ntpver1.R;
import com.example.ntpver1.login.login.LoginManager;
import com.example.ntpver1.login.login.User;
import com.google.android.material.textfield.TextInputEditText;

public class CardRegisterActivity extends Activity {

    LoginManager loginManager;
    User user;
    AutoCompleteTextView cardTypeText;
    TextInputEditText cardNumberText;
    TextInputEditText cardValidText;
    Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_card_register);

        init();
    }

    private void init() {
        loginManager = LoginManager.getInstance();
        user =  loginManager.getUser();
        setComboBox();
        cardTypeText = findViewById(R.id.card_type);
        cardNumberText = findViewById(R.id.card_number);
        cardValidText = findViewById(R.id.card_valid);
        confirmButton = findViewById(R.id.confirm_button);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    
                }
            }
        });
    }

    private boolean isValid() {
        return true;
    }

    private void setComboBox() {
        String[] Pays = new String[]{"경기페이", "제로페이", "카카오페이", "알리페이"};

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        getApplicationContext(),
                        R.layout.dropdown_menu_popup_item,
                        Pays);

        AutoCompleteTextView editTextFilledExposedDropdown =
                findViewById(R.id.card_type);
        editTextFilledExposedDropdown.setAdapter(adapter);
    }
}
