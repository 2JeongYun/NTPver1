package com.example.ntpver1;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.ntpver1.adapter.DirectionsJSONParser;
import com.example.ntpver1.adapter.DriverInfoAdapter;
import com.example.ntpver1.item.Store;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

public class MapManager extends AppCompatActivity implements GoogleMap.OnMarkerClickListener {

    private MapManager(GoogleMap googleMap, MapActivity mapActivity) {
        this.mMap = googleMap;
        this.mapActivity = mapActivity;
        Log.d(TAG, "MapManager's constructor is called!!");
    }

    public static Context MapManagercontext;

    private static final String TAG = "MapManager";

    private static MapManager mapManager;

    private static final int UPDATE = 2;

    private Map<String, MarkerOptions> mMarkers = new ConcurrentHashMap<String, MarkerOptions>();

    MapActivity mapActivity;
    Marker mymaker;
    LatLng SearchCenter;
    Marker premaker ;
    ArrayList<String> pmlist = new ArrayList<>();
    ArrayList<Store> aroundlist = new ArrayList<>();
    ArrayList<Marker> prelist = new ArrayList<>();
    private Polyline mPolyline;
    private GoogleMap mMap;
    String[] permission_list={
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };


    Location myLocation;
    LocationManager manager;

    public static MapManager getInstance(GoogleMap googleMap, MapActivity mapActivity) {
        if (mapManager == null) {
            mapManager = new MapManager(googleMap, mapActivity);
        }

        return mapManager;
    }

    public static MapManager getInstance() {
        if (mapManager == null) {
            return null;
        }

        return mapManager;
    }

    //권환 확인하기
    public void checkPermission(){
        Log.d("1", "checkPermission");
        boolean isGrant=false;
        for(String str : permission_list){
            if(ContextCompat.checkSelfPermission(mapActivity.getActivity(), str)== PackageManager.PERMISSION_GRANTED){          }
            else{
                isGrant=false;
                break;
            }
        }
        if(isGrant==false){
            ActivityCompat.requestPermissions(mapActivity.getActivity(), permission_list,0);
        }
    }

    //어플의 권한 획득하기 , 내위치 불러오기
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mapActivity.getActivity().onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isGrant = true;
        for(int result : grantResults){
            if(result == PackageManager.PERMISSION_DENIED){
                isGrant = false;
                break;
            }
        }
        // 모든 권한을 허용했다면 사용자 위치를 측정한다.
        if(isGrant == true){
            getMyLocation();
        }
    }

    // GPS Listener
    class GpsListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            Log.d("1", "댐?");
            myLocation = location;
            manager.removeUpdates(this);
            showMyLocation();
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
        @Override
        public void onProviderDisabled(String provider) {
        }
        @Override
        public void onProviderEnabled(String provider) {
        }
    }

    //내 위치 찾기
    public void getMyLocation(){
        manager = (LocationManager) mapActivity.getActivity().getSystemService(Activity.LOCATION_SERVICE);

        // 권한이 모두 허용되어 있을 때만 동작하도록 한다.
        int chk1 = ContextCompat.checkSelfPermission(mapActivity.getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        int chk2 = ContextCompat.checkSelfPermission(mapActivity.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
        if(chk1 == PackageManager.PERMISSION_GRANTED && chk2 == PackageManager.PERMISSION_GRANTED){
            myLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        //새롭게 위치 찾기
        GpsListener listener = new GpsListener();
        if(manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10, listener);
        }
        if(manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, listener);
        }

        Log.d(TAG, "myLocation Value : " + myLocation.getLatitude());
    }

    // 내위치 보여주기
    public void showMyLocation(){
        // LocationManager.GPS_PROVIDER 부분에서 null 값을 가져올 경우를 대비하여 장치
        if(myLocation == null){
            return;
        }
        // 현재 위치값을 추출한다.
        double lat=myLocation.getLatitude();
        double lng=myLocation.getLongitude();

        LatLng location = new LatLng(lat, lng);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(location);
        SearchCenter = location;

        if (mymaker != null){
            mymaker.remove();
        }
        mymaker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(20));

    }

    // 내위치 버튼이 클릭될때 작됭되는 함수
    public void clickButton(){
        getMyLocation();
        LatLng location ;
        if(distance(myLocation.getLatitude() , myLocation.getLongitude() ,mymaker.getPosition().latitude ,mymaker.getPosition().longitude) > 50){
            location = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(location);
            mymaker.remove();
            mymaker = mMap.addMarker(markerOptions);
            SearchCenter = location;
            mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(20));
            mapManager.showMyLocation();
        }
        location = new LatLng(mymaker.getPosition().latitude, mymaker.getPosition().longitude);

        myLocation.setLatitude(mymaker.getPosition().latitude);
        myLocation.setLongitude(mymaker.getPosition().longitude);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
    }

    // 스토어 객체를 받아와 마크찍기
    public void Marking(Store store){
        Log.d(TAG, "Marking() store name : " + store.getName());

        aroundlist.add(store);

        LatLng location = new LatLng(store.getLatitude() , store.getLongitude());
        Marker marker ;
        MarkerOptions markerOptions = new MarkerOptions();

        switch (store.getType()) {
            case "음식점" :
                markerOptions.position(location).title(store.getName())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.restaurant));
                break;
            case "카페" :
                markerOptions.position(location).title(store.getName())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.cafe));
                break;
            case "편의점" :
                markerOptions.position(location).title(store.getName())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.cstore));
                break;
            case "식료품점" :
                markerOptions.position(location).title(store.getName())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.grocery));
                break;
            case "의료" :
                markerOptions.position(location).title(store.getName())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.medical));
                break;
            case "패션" :
                markerOptions.position(location).title(store.getName())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.clothing));
                break;
            case "전자제품" :
                markerOptions.position(location).title(store.getName())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.electronic));
                break;
            case "유흥" :
                markerOptions.position(location).title(store.getName())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.pleasure));
                break;
            case "숙박" :
                markerOptions.position(location).title(store.getName())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.lodgment));
                break;
            default:
                markerOptions.position(location).title(store.getName())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.others));
                break;
        }
        marker = mMap.addMarker(markerOptions);
        mMarkers.put(store.getName(), markerOptions);
        prelist.add(marker);
        pmlist.add(store.getName());

        this.mMap.setOnMarkerClickListener(this);
    }

    // 네비게이션 함수
    public void navigation(LatLng start , LatLng destination) throws IOException, JSONException {

        String site="https://maps.googleapis.com/maps/api/directions/json";
        site+="?origin="+start.latitude +","+ start.longitude +"&amp"
                +"&destination=" + destination.latitude +","+destination.longitude+"&amp"
                //+"&radius=1000&sensor=false&language=ko"
                +"&key=AIzaSyCRo5dYag4CpeJPBwzwmmdkrRL4-n8FyV0";

        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(site);

    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("DownloadTask","DownloadTask : " + data);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(8);
                lineOptions.color(Color.RED);
            }

            // Drawing polyline in the Google Map for the i-th route
            if(lineOptions != null) {
                if(mPolyline != null){
                    mPolyline.remove();
                }
                mPolyline = mMap.addPolyline(lineOptions);

            }else
                Toast.makeText(getApplicationContext(),"No route is found", Toast.LENGTH_LONG).show();
        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception on download", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    //마크가 클릭 되었을때 마크가 있는 곳으로 카메라 중심 이동 , 색상변경
    @Override
    public boolean onMarkerClick(Marker marker)  {

        if(marker.equals(mymaker)){
            return false;
        }

        premaker = marker;
        mMap.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
        LayoutInflater inflater = (LayoutInflater) mapActivity.getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View infoWindow = inflater.inflate(R.layout.item_markerinfo ,null);
        DriverInfoAdapter driverInfoAdapter = new DriverInfoAdapter(infoWindow, FindStore());
        mMap.setInfoWindowAdapter(driverInfoAdapter);
        marker.showInfoWindow();
        return true;
    }

    //현재 선택한 마크의 스토어객체를 리턴하는 함수
    public Store FindStore(){
        Store store = null;
        for(Store s : aroundlist){
            if(premaker.getTitle().equals(s.getName()))
                return  s;
        }
        return store;
    }

    //현재 위치에서 2000미터 떨어지면 작동하는 함수
    public void checkMoveCamera(){
        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                MapActivity mapActivity = new MapActivity();
                CameraPosition movingposition = mMap.getCameraPosition();
                if(distance(movingposition.target.latitude , movingposition.target.longitude , SearchCenter.latitude , SearchCenter.longitude) > 2000){
                    RemovePremarker();
                    pmlist.clear();
                    LatLng location = new LatLng(movingposition.target.latitude , movingposition.target.longitude);
                    SearchCenter = movingposition.target;
                    try {
                        Log.d("search", "movecamera");
                        ((MapActivity)MapActivity.mapContext).doSearch(location.latitude,location.longitude,3000 ,10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void Findmarker(Store s){
        Log.d(TAG, "Findmarker() store name : " + s.getName());

        for(Marker m : prelist){
            if(m.getTitle().equals(s.getName())) {
                premaker = m;
                mMap.moveCamera(CameraUpdateFactory.newLatLng(m.getPosition()));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(20));
                LayoutInflater inflater = (LayoutInflater) mapActivity.getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                View infoWindow = inflater.inflate(R.layout.item_markerinfo ,null);
                DriverInfoAdapter driverInfoAdapter = new DriverInfoAdapter(infoWindow, FindStore());
                mMap.setInfoWindowAdapter(driverInfoAdapter);
                m.showInfoWindow();
            }
        }
    }
    // 두 지점사이의 거리를 meter로 반환해 주기
    private double distance(double movinglat , double movinglnt , double centerlat , double centerlnt){
        double theta = Math.abs(movinglnt - centerlnt);
        double dist = Math.sin(deg2rad(movinglat)) * Math.sin(deg2rad(centerlat)) + Math.cos(deg2rad(movinglat)) * Math.cos(deg2rad(centerlat)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515* 1609.344;

        return dist;
    }
    // This function converts decimal degrees to radians
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // This function converts radians to decimal degrees
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    public void RemovePremarker(){
        for(String s : pmlist){
            remove(s);
        }
    }

    private void remove(String name){
        mMarkers.remove(name);
    }



}
