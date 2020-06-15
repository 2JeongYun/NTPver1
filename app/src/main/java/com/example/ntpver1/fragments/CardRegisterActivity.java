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

import com.example.ntpver1.DBManager;
import com.example.ntpver1.R;
import com.example.ntpver1.item.Card;
import com.example.ntpver1.login.login.LoginManager;
import com.example.ntpver1.login.login.User;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;

import java.util.concurrent.ExecutionException;

public class CardRegisterActivity extends Activity {

    LoginManager loginManager;
    User user;
    AutoCompleteTextView cardTypeText;
    TextInputEditText cardNumberText;
    TextInputEditText cardValidText;
    Button confirmButton;
    DBManager dbManager = DBManager.getInstance();

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
                    dbManager.setInsertCardValue(Card.getEnName(cardTypeText.getText().toString()),
                            cardNumberText.getText().toString(), user.getUserEmail(), cardValidText.getText().toString());
                    try {
                        dbManager.writeCardData();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    finish();
                    loginManager.userUpdate();
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
