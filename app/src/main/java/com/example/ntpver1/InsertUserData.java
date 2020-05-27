package com.example.ntpver1;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class InsertUserData extends AsyncTask<String, Void, String> {
    private static final String TAG = "";
    private String json = "";

    @Override
    protected String doInBackground(String... params) {

        String email = (String)params[1];
//        String user_id = (String)params[2];
        String password = (String)params[2];
        String user_name = (String)params[3];
        String phone_number = (String)params[4];

        String serverURL = (String)params[0];

        String postParameters = "email=" + email
                + "&password=" + password
                + "&phone_number=" + phone_number
                + "&user_name=" + user_name;
        System.out.println(postParameters);
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
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
//        return s;
    }
}
