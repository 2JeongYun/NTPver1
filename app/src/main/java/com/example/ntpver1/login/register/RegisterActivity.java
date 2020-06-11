package com.example.ntpver1.login.register;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ntpver1.DBManager;
import com.example.ntpver1.R;
import com.example.ntpver1.adapter.CardAdapter;

import org.json.JSONException;

import java.util.concurrent.ExecutionException;

public class RegisterActivity extends Activity {

    Button confirmButton;
    Button certificationButton;
    EditText emailEditText;
    EditText passwordEditText;
    EditText certificationEditText;
    EditText nameEditText;
    EditText phoneEditText;
    String certificationString; // 인증번호 저장 jjs 06.08
    DBManager dbManager = DBManager.getInstance();
    CardAdapter cardAdapter;

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
        nameEditText = findViewById(R.id.name);
        phoneEditText = findViewById(R.id.phone);
        certificationButton = findViewById(R.id.certificationButton); // 버튼추가 jjs 06.08
        // 버튼인증이벤트추가 jjs 06.08
        certificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String email = emailEditText.getText().toString();
                    dbManager.setSendCertificationValue(email);
                    certificationString = dbManager.sendCertificationData();
                    Toast.makeText(getApplicationContext(), "인증 메일을 발송했습니다.", Toast.LENGTH_SHORT).show();
                } catch (ExecutionException | InterruptedException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String pw = passwordEditText.getText().toString();
                String cert = certificationEditText.getText().toString();
                String name = nameEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                dbManager.setSearchUserValue(email, pw);

                try {
                    if (!email.equals("") && !pw.equals("")
                            && !name.equals("") && !phone.equals("") && isCertValid(cert)) {
                        try {
                            register(email, pw, name, phone);

                        } catch (InterruptedException | JSONException | ExecutionException e) {
                            e.printStackTrace();//insert에러 발생시 빨간색으로 바꾸기추가해야됨 jjs
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "입력칸을 모두 작성해주세요.", Toast.LENGTH_SHORT).show();
                    }
                } catch (InterruptedException | ExecutionException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //인증확인
    //TEST
    public boolean isCertValid(String cert) throws InterruptedException, ExecutionException, JSONException {
//        dbManager.readUserData();
        if (cert.equals(certificationString)) {
            return true;
        }
        else{
            Toast.makeText(getApplicationContext(), "올바른 인증번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    //등록하기
    public void register(String email, String password, String user_name, String phone_number) throws InterruptedException, ExecutionException, JSONException {
        dbManager.setInsertUserValue(email, password, user_name, phone_number);
        dbManager.writeUserData();
        finish();
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
