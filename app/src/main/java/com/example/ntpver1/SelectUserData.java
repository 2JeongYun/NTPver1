package com.example.ntpver1;

import android.os.AsyncTask;
import android.util.Log;

import com.example.ntpver1.adapter.CardAdapter;
import com.example.ntpver1.fragments.MyInfoFragment;
import com.example.ntpver1.login.login.LoginManager;
import com.example.ntpver1.login.login.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class SelectUserData extends AsyncTask<String, Void, String> {
    private static final String TAG = "";
    private String json = "";
    DBManager dbManager = DBManager.getInstance();
    LoginManager lgManager = LoginManager.getInstance();
    User user = lgManager.getUser();
    CardAdapter cardAdapter = MyInfoFragment.getCardAdapterInstance();;


    @Override
    protected String doInBackground(String... params) {

        String email = (String)params[1];
        String password = (String)params[2];

        String serverURL = (String)params[0];
        String postParameters = "email=" + email
                + "&password=" + password;

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
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                String user_name = jsonObject.getString("user_name");
                String email = jsonObject.getString("email");
                String phone_number = jsonObject.getString("phone_number");
                user.setUserEmail(email);
                user.setUserName(user_name);
                user.setPhoneNumber(phone_number);

                dbManager.setSearchCardValue(email);
                dbManager.readCardData();
            }
            catch (JSONException e) {
                Log.d(TAG, e.toString());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}
