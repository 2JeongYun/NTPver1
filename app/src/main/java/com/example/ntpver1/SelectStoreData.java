package com.example.ntpver1;

import android.os.AsyncTask;
import android.util.Log;


import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import com.example.ntpver1.adapter.StoreAdapter;
import com.example.ntpver1.item.Store;
import com.mingle.sweetpick.SweetSheet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class SelectStoreData extends AsyncTask<String, Void, String> {
    private static final String TAG = "SelectStoreData";
    private MapManager mapManager;
    private DBManager dbManager;
    private StoreAdapter storeAdapter;
    private SweetSheet sweetSheet;
    private ArrayList<Store> results;
    private String json = "";

    public SelectStoreData(MapManager mapManager, DBManager dbManager, StoreAdapter storeAdapter, SweetSheet sweetSheet) {
        this.mapManager = mapManager;
        this.dbManager = dbManager;
        this.storeAdapter = storeAdapter;
        this.sweetSheet = sweetSheet;
        this.results = new ArrayList<>();
    }

    private Store makeStore(String payName, String storeName, String address, String phoneNumber, String category, double latitude, double longitude) {
        ArrayList<String> pays = new ArrayList<>();
        pays.add(payName);
        Random random = new Random(); //랜덤 객체 생성(디폴트 시드값 : 현재시간)
        random.setSeed(System.currentTimeMillis()); //시드값 설정을 따로 할수도 있음
        int star = random.nextInt(5);
        Store s = new Store(pays, storeName, address, phoneNumber, category, star, latitude, longitude);
        return s;
    }

    @Override
    protected String doInBackground(String... params) {

        String keyword = (String)params[1];
        String pay_name = (String)params[2];
        String category = (String)params[3];
        String start_at = (String)params[4];
        String end_at = (String)params[5];

        String serverURL = (String)params[0];
        String postParameters = "keyword=" + keyword
                + "&pay_name=" + pay_name
                + "&category=" + category
                + "&start_at=" + start_at
                + "&end_at=" + end_at;
        Log.d(TAG, postParameters);
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
//            Log.d(TAG, "POST response code - " + responseStatusCode);
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

            return new String("Error: " + e.getMessage());
        }

    }

    @Override
    protected void onPostExecute(String json_string) {
        super.onPostExecute(json_string);
        Log.d(TAG, "onPostExecute() start");
        json_string = json_string.replaceAll("thereisnodata","");
        if (!json_string.contains("thereisnodata")) {
            if (!json_string.equals("")) {
                if (!json_string.contains("error")) {
                    try {
                        JSONArray jsonArray = new JSONArray(json_string);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String payName = jsonObject.getString("pay_name");
                            String storeName = jsonObject.getString("store_name");
                            String address = jsonObject.getString("address");
                            String phoneNumber = jsonObject.getString("phone_number");
                            String category = jsonObject.getString("category");
                            double latitude = jsonObject.getDouble("latitude");
                            double longitude = jsonObject.getDouble("longitude");
                            Store store = makeStore(payName, storeName, address, phoneNumber, category, latitude, longitude);
                            results.add(store);
                            mapManager.Marking(store);
                            storeAdapter.addItem(store);
                        }

                        ((MapActivity)MapActivity.mapContext).refreshList();

                    } catch (JSONException e) {
                        Log.d(TAG, "json error" + e.toString());
                    }
                } else {
                    Log.d(TAG, "db error" + json_string);
                }
            } else {
                Log.d(TAG, "empty list");
            }
        }
        else{
            Log.d(TAG, "thereisnodata");
        }
    }

    private Store reduplicationChecker(String storeName, String phoneNumber, double latitude, double longitude){
        for(Store x : results) {
            if (x.getPhone().equals(phoneNumber) && !x.getPhone().equals("")) {
                Log.d(TAG, storeName + "전화번호일치");
                return x;
            }
            if (x.getName().equals(storeName)) {
                Log.d(TAG, storeName + " 이름일치");
                return x;
            }
            if (x.getLatitude() == latitude) {
                Log.d(TAG, storeName + " 위도일치");
                return x;
            }
            if (x.getLatitude() == longitude) {
                Log.d(TAG, storeName + "경도일치");
                return x;
            }
        }
        return null;
    }
}
