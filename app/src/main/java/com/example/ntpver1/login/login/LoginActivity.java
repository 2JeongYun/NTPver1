package com.example.ntpver1.login.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ntpver1.DBManager;
import com.example.ntpver1.R;
import com.example.ntpver1.fragments.MenuActivity;
import com.example.ntpver1.login.register.UserRegisterActivity;

import org.json.JSONException;

import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {
    private static final int REGISTER_ACTIVITY_CODE = 3;
    private static final String TAG = "LoginActivity";

    LoginManager loginManager;
    DBManager dbManager = DBManager.getInstance();
    EditText emailEditText;
    EditText passwordEditText;
    Button loginButton;
    Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginManager = LoginManager.getInstance();

        setViews();
    }

    private void setViews() {
        EditText emailEditText = findViewById(R.id.user_email);
        EditText passwordEditText = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.login);
        Button registerButton = findViewById(R.id.register);

        //로그인 버튼
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                if (!email.equals("") && !password.equals("")) {
                    try { // 비동기로 인한 try catch 추가 jjs 05.27
                        if (loginManager.login(email, password)) {

                            //유저정보 및 카드 정보 가져오기 실행지웠음 jjs 06.11
                            Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                            startActivity(intent);

                        }
                        else{
                            Toast.makeText(getApplicationContext(), "올바른 이메일과 패스워드를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (InterruptedException | ExecutionException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "이메일과 패스워드를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //회원가입 버튼
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserRegisterActivity.class);
                startActivity(intent);
            }
        });


        //---------------DEBUG ONLY//AutoLogin---------------
//        emailEditText.setText("master");
//        passwordEditText.setText("0000");
//        loginButton.callOnClick();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REGISTER_ACTIVITY_CODE:
                String email = data.getStringExtra("email");
                String pw = data.getStringExtra("pw");
                Log.d(TAG, "onActivityResult() is called.. Code : " + Integer.toString(requestCode));
                break;
        }
    }
}
