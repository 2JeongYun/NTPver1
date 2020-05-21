package com.example.ntpver1;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;

import com.example.ntpver1.item.Store;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import kotlin.collections.MapsKt;

public class MapManager extends AppCompatActivity implements GoogleMap.OnMarkerClickListener {
    private MapManager(GoogleMap googleMap, MapActivity mapActivity) {
        this.mMap = googleMap;
        this.mapActivity = mapActivity;
        Log.d(TAG, "MapManager's constructor is called!!");
    }

    private static final String TAG = "MapManager";

    private static MapManager mapManager;

    private static final int UPDATE = 2;

    MapActivity mapActivity;
    String ClickedStore;
    Marker mymaker;
    Marker SearchCentermymaker ;
    Marker premaker ;
    ArrayList<Marker> prelist = new ArrayList<>();

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

    public LatLng getSearchCentermymakerlntlng(){
        LatLng centerlocation = new LatLng(SearchCentermymaker.getPosition().latitude , SearchCentermymaker.getPosition().longitude);
        return centerlocation;
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

        mymaker = mMap.addMarker(markerOptions);
        SearchCentermymaker = mymaker;
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(20));

    }

    public void clickButton(){
        getMyLocation();
        LatLng location ;
        if(distance(myLocation.getLatitude() , myLocation.getLongitude() ,mymaker.getPosition().latitude ,mymaker.getPosition().longitude) > 50){
            location = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(location);
            mymaker.remove();
            mymaker = mMap.addMarker(markerOptions);
            SearchCentermymaker = mymaker;
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

        LatLng location = new LatLng(store.getLatitude() , store.getLongitude());
        //MarkerOptions markerOptions = ChangeMarkertImage(store);
        MarkerOptions markerOptions = new MarkerOptions() ;
        markerOptions.position(location).title(store.getName());
        prelist.add(mMap.addMarker(markerOptions));
        this.mMap.setOnMarkerClickListener(this);
    }

    public void storesMarking(ArrayList<Store> list)
    {
        for(Store st : list){
            Marking(st);
        }
    }

    public MarkerOptions ChangeMarkertImage(Store store){
        MarkerOptions markerOptions = new MarkerOptions();
        BitmapDrawable bitmapdraw = null;
        String category = store.getType();
        switch (category) {
            case "음식점" :
                bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.restaurant);
                break;
            case "카페" :
                bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.restaurant);
                break;
            case "편의점" :
                bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.restaurant);
                break;
            case "식료품점" :
                bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.restaurant);
                break;
            case "의료" :
                bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.restaurant);
                break;
            case "패션" :
                bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.restaurant);
                break;
            case "전자제품" :
                bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.restaurant);
                break;
            case "유흥" :
                bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.restaurant);
                break;
            case "숙박" :
                bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.restaurant);
                break;
            default:
                bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.restaurant);
                break;
        }
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 200, 200, false);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
        return markerOptions;
    }

    //마크가 클릭 되었을때 마크가 있는 곳으로 카메라 중심 이동 , 색상변경
    @Override
    public boolean onMarkerClick(Marker marker) {
        if(marker.equals(mymaker)){
            return false;
        }
        if(premaker != null){
            premaker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }
        premaker = marker;
        mMap.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
        ClickedStore = marker.getTitle();
        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        return true;
    }

    //마크가 클릭된 뒤에 데이터베이스 list를 사용하여 해당 store객체를 리턴해주는 함수
    public Store FindStore(ArrayList<Store> list){
        for(Store s : list){
            if(s.getName() == ClickedStore)
                return s;
        }
        return null;
    }

    public void CheckMoveCamera(){
        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                MapActivity mapActivity = new MapActivity();
                CameraPosition movingposition = mMap.getCameraPosition();
                if(distance(movingposition.target.latitude , movingposition.target.longitude , SearchCentermymaker.getPosition().latitude , SearchCentermymaker.getPosition().longitude) > 1000){
                    LatLng location = new LatLng(movingposition.target.latitude , movingposition.target.longitude);
                    SearchCentermymaker.setPosition(location);
                    //try catch 추가 05.20 jjs
                    try {
                        mapActivity.doSearch("", movingposition.target.latitude , movingposition.target.longitude , 500 , 2);
                    } catch (InterruptedException | ExecutionException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
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
        for(Marker m : prelist){
            m.remove();
        }
    }


}
