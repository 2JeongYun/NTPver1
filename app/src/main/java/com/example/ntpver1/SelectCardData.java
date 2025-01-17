package com.example.ntpver1;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import android.content.Intent;
import android.view.View;

import com.example.ntpver1.fragments.MenuActivity;
import com.example.ntpver1.fragments.MyInfoFragment;

import com.mingle.sweetpick.SweetSheet;

import com.example.ntpver1.adapter.CardAdapter;
import com.example.ntpver1.item.Card;
import com.example.ntpver1.login.login.LoginManager;
import com.example.ntpver1.login.login.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class SelectCardData extends AsyncTask<String, Void, String> {
    private static final String TAG = "SelectCardData";
    private String json = "";
    CardAdapter cardAdapter = MyInfoFragment.getCardAdapterInstance();

    LoginManager lgManager = LoginManager.getInstance();
    User user = lgManager.getUser();
    DBManager dbManager = DBManager.getInstance();
    @Override
    protected String doInBackground(String... params) {
        String user_email = (String)params[1];

        String serverURL = (String)params[0];
        String postParameters = "user_email=" + user_email;

        try {

            URL url = new URL(serverURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.connect();


            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(postParameters.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();


            int responseStatusCode = httpURLConnection.getResponseCode();
            Log.d(TAG, "POST response code - " + responseStatusCode);
            InputStream inputStream;
            if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
            }
            else{
                inputStream = httpURLConnection.getErrorStream();
            }


            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder sb = new StringBuilder();
            String line = null;
            String toJson = "";
            while((line = bufferedReader.readLine()) != null){
                toJson += line;
            }


            bufferedReader.close();
            return toJson;


        } catch (Exception e) {

            Log.d(TAG, "InsertData: Error ", e);

            return new String("Error: " + e.getMessage());
        }

    }
    @Override
    protected void onPostExecute(String json_string) {
        super.onPostExecute(json_string);
        if (!json_string.contains("FAIL")) {
            try {
                JSONArray jsonArray = new JSONArray(json_string);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int balance = jsonObject.getInt("balance");
                    String card_kinds = jsonObject.getString("card_kinds");
                    int id = jsonObject.getInt("id");
                    Card card = new Card();
                    card.setBalance(balance);
                    card.setId(id);
                    card.setCard_kinds(card_kinds);
                    user.setCards(card);
                    cardAdapter.addItem(card);

                    dbManager.setSearchConsumptionlistValue(user.getUserEmail(),card_kinds);
                    try {
                        dbManager.readConsumptionlistData();
                    } catch (ExecutionException | InterruptedException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            catch (JSONException e) {
                Log.d(TAG, e.toString());
            }

            // 06.11 null point오류 수정 -> 첫로그인시에는 menu액티비티 존재안함
            if(MenuActivity.mContext == null){

            }
            else {
                ((MenuActivity) MenuActivity.mContext).callMyInfoFragment("refreshList");
            }
        }
    }
}
