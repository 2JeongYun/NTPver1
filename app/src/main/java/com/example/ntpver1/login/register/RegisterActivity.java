package com.example.ntpver1.login.register;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.ntpver1.R;

public class RegisterActivity extends Activity {

    Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_register);

        init();
    }

    private void init() {
        confirmButton = findViewById(R.id.confirm_button);
        //TEST 메일 인증절차 구현 완료시 변경할 것
        enableConfirm();
        //
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //완료버튼 활성화
    private void enableConfirm() {
        if (true) {
            confirmButton.setEnabled(true);
        }
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
