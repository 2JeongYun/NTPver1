package com.example.ntpver1.login.login;

import com.example.ntpver1.DBManager;

import org.json.JSONException;

import java.util.concurrent.ExecutionException;

public class LoginManager {

    private LoginManager() {}

    private static LoginManager loginManager;

    DBManager dbManager = DBManager.getInstance();
    User user;

    public boolean login(String email, String pw) throws InterruptedException, ExecutionException, JSONException {
        //Test
        dbManager.setSearchUserValue(email, pw);
        return dbManager.readUserData();
    }

    public User getUser() {
        return user;
    }

    public static LoginManager getInstance() {
        if (loginManager == null) {
            loginManager = new LoginManager();
        }

        return loginManager;
    }
}
