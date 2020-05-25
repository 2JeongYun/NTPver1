package com.example.ntpver1.login.register;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.ntpver1.R;

public class RegisterActivity extends Activity {

    Button confirmButton;
    EditText emailEditText;
    EditText passwordEditText;
    EditText certificationEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_register);

        init();
    }

    private void init() {
        confirmButton = findViewById(R.id.confirm_button);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        certificationEditText = findViewById(R.id.certification);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String pw = passwordEditText.getText().toString();
                String cert = certificationEditText.getText().toString();

                if (!email.equals("") && !pw.equals("") && isCertValid(cert)) {
                    register(email, pw);
                }

                finish();
            }
        });
    }

    //인증확인
    //TEST
    public boolean isCertValid(String cert) {
        return true;
    }

    //등록하기
    public void register(String email, String pw) {

    }

    //외부 터치시 종료하지 않도록 함
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }
}
