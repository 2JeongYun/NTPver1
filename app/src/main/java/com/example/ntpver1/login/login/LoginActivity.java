package com.example.ntpver1.login.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ntpver1.R;
import com.example.ntpver1.fragments.MenuActivity;
import com.example.ntpver1.login.find_pw.FindPWActivity;
import com.example.ntpver1.login.register.RegisterActivity;

import org.json.JSONException;

import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {
    private static final int REGISTER_ACTIVITY_CODE = 3;
    private static final String TAG = "LoginActivity";

    LoginManager loginManager;
    EditText emaliEditText;
    EditText passwordEditText;
    Button loginButton;
    Button registerButton;
    Button findPWButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginManager = LoginManager.getInstance();

        setViews();
    }

    private void setViews() {
        EditText emaliEditText = findViewById(R.id.user_email);
        EditText passwordEditText = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.login);
        Button registerButton = findViewById(R.id.register);
        Button findPWButton = findViewById(R.id.find_pw);

        //로그인 버튼
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emaliEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                if (!email.equals("") && !password.equals("")) {
                    try { // 비동기로 인한 try catch 추가 jjs 05.27
                        if (loginManager.login(email, password)) {
                            Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "올바른 이메일과 패스워드를 입력해주세요.", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onClick: Login FAIL!");
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
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        //PW찾기 버튼
        findPWButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FindPWActivity.class);
                startActivity(intent);
            }
        });
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
