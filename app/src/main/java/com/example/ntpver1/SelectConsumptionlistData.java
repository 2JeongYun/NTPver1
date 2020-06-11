package com.example.ntpver1;

import android.os.AsyncTask;
import android.util.Log;

import com.example.ntpver1.adapter.ConsumptionAdapter;
import com.example.ntpver1.fragments.CardInfoFragment;
import com.example.ntpver1.fragments.MenuActivity;
import com.example.ntpver1.item.Card;
import com.example.ntpver1.item.Consumptionlist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public class SelectConsumptionlistData extends AsyncTask<String, Void, String> {
    private static final String TAG = "";
    private String json = "";
    ConsumptionAdapter Csmpt = CardInfoFragment.getCsmptInstance();

    @Override
    protected String doInBackground(String... params) {
        String user_email = (String) params[1];
        String card_kinds = (String) params[2];

        String serverURL = (String) params[0];
        String postParameters = "user_email=" + user_email
                + "&card_kinds=" + card_kinds;

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
            if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
            } else {
                inputStream = httpURLConnection.getErrorStream();
            }


            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder sb = new StringBuilder();
            String line = null;
            String toJson = "";
            while ((line = bufferedReader.readLine()) != null) {
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
        Log.d(TAG, "onPostExecute: " + json_string);
        if (!json_string.contains("FAIL")) {
            try {
                JSONArray jsonArray = new JSONArray(json_string);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int balance = jsonObject.getInt("balance");
                    int pay = jsonObject.getInt("pay");
                    String pay_date = jsonObject.getString("pay_date");
                    String card_kinds = jsonObject.getString("card_kinds");
                    String store_name = jsonObject.getString("store_name");
                    int id = jsonObject.getInt("id");
                    Consumptionlist csmpt = new Consumptionlist();
                    csmpt.setBalance(balance);
                    csmpt.setId(id);
                    csmpt.setPay(pay);
                    csmpt.setPay_date(pay_date);
                    csmpt.setStore_name(store_name);
                    csmpt.setCard_kind(card_kinds);
                    Csmpt.addItem(csmpt);
                }
                ((MenuActivity) MenuActivity.mContext).startCardInfoFragment();
                System.out.println("test done");
            } catch (JSONException e) {
                Log.d(TAG, e.toString());
            }

        }
    }
}
