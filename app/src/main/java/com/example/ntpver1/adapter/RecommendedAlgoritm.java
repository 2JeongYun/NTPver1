package com.example.ntpver1.adapter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.ntpver1.DBManager;
import com.example.ntpver1.item.Card;
import com.example.ntpver1.item.Consumptionlist;
import com.example.ntpver1.item.Store;
import com.example.ntpver1.login.login.User;
import com.example.ntpver1.login.login.LoginManager;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

public class RecommendedAlgoritm extends AppCompatActivity {

    LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    GPSListener gpsListener = new GPSListener();

    class GPSListener implements LocationListener{
        @Override
        public void onLocationChanged(Location location) {

            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) { }

        @Override
        public void onProviderEnabled(String s) { }

        @Override
        public void onProviderDisabled(String s) { }
    }

    LoginManager loginManager = LoginManager.getInstance();
    User user =loginManager.getUser();

    private static RecommendedAlgoritm recommendedAlgoritm;

    private RecommendedAlgoritm(){
        getmylocation();
    }

    public static RecommendedAlgoritm getInstance(){
        if(recommendedAlgoritm == null){
            recommendedAlgoritm = new RecommendedAlgoritm();
        }

        return recommendedAlgoritm;
    }

    Location myLocation;
    double Weightres = 0;
    double Weightcafe= 0;
    double Weightcon= 0;
    double Weightgro= 0;
    double Weightmed= 0;
    double Weightfas= 0;
    double Weightele= 0;
    double Weightplay= 0;
    double Weighthotel= 0;
    double Weightother= 0;
    double count = 0;
    double Zeropayavg = 0;
    double Kyongipayavg = 0;

    ArrayList<Store> stlist = new ArrayList<Store>();
    ArrayList<Store> recommendlist = new ArrayList<Store>();


    public void calculationCategoryWeigt(){
        ArrayList<Card> cardlist = user.getCards();
        for(Card card: cardlist){
            for(Consumptionlist consumptionlist : card.getUsageHistory()){
                calculationpaytypeAvg(consumptionlist);
                count++;
                switch (consumptionlist.getCategory()){
                    case "음식점" :
                        Weightres++;
                        break;
                    case "카페" :
                        Weightcafe++;
                        break;
                    case "편의점" :
                        Weightcon++;
                        break;
                    case "식료품점" :
                        Weightgro++;
                        break;
                    case "의료" :
                        Weightmed++;
                        break;
                    case "패션" :
                        Weightfas++;
                        break;
                    case "전자제품" :
                        Weightele++;
                        break;
                    case "유흥" :
                        Weightplay++;
                        break;
                    case "숙박" :
                        Weighthotel++;
                        break;
                    default:
                        Weightother++;
                        break;
                }
            }
            Weightres = Weightres/count;
            Weightcafe = Weightcafe/count;
            Weightcon = Weightcon/count;
            Weightgro = Weightgro/count;
            Weightmed = Weightmed/count;
            Weightfas = Weightfas/count;
            Weightele = Weightele/count;
            Weightplay = Weightplay/count;
            Weighthotel =Weighthotel/count;
            Weightother= Weightother/count;
        }
    }

    public void calculationpaytypeAvg(Consumptionlist consumptionlist){

        double zero = 0;
        double kyonggi = 0;
        double zerocount = 0;
        double kyonggicount = 0;

        switch(consumptionlist.getCard_kind()){
            case "zeropay":
                zero = zero+consumptionlist.getPay();
                zerocount++;
                break;

            case "kyonggipay":
                kyonggi = kyonggi+consumptionlist.getPay();
                kyonggicount++;
        }

        Zeropayavg = zero/zerocount;
        Kyongipayavg = kyonggicount/kyonggicount;


    }

    public void makeRecommendlist(ArrayList<Store> list){
        for(Store s : list){
            double diweight = distance(s.getLatitude(), s.getLongitude() , myLocation.getLatitude() , myLocation.getLongitude()) / 100.0;
            double caweight = discernCategory(s.getType());
            double payavgweight =comparePayavg(s.getPays());
            double weight = caweight - Math.pow(diweight, 2) - payavgweight;
            s.setWeight(weight);
            recommendlist.add(s);
        }
        Collections.sort(recommendlist);
    }

    //slist 값 추가
    public void setStlist(Double latitude, Double longitude, int radius, ArrayList<Store> stlist) throws ExecutionException, InterruptedException {
        int TEST_RADIUS_SET = 300;
        DBManager dbManager = DBManager.getInstance();
        dbManager.setSearchStoreListValue(latitude,longitude,TEST_RADIUS_SET);
        dbManager.readStoreListData(stlist);
    }


    public void getmylocation(){
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions( RecommendedAlgoritm.this, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
                    0 );
        }
        else {
            myLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
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
    // 카테고리에 따른 가중치를 반환해주는 함수
    private double discernCategory(String category){
        double caweight = 0;
        switch (category){
            case "음식점" :
                caweight = Weightres * 10;
                break;
            case "카페" :
                caweight = Weightcafe * 10;
                break;
            case "편의점" :
                caweight = Weightcon * 10;
                break;
            case "식료품점" :
                caweight = Weightgro * 10;
                break;
            case "의료" :
                caweight = Weightmed * 10;
                break;
            case "패션" :
                caweight = Weightfas * 10;
                break;
            case "전자제품" :
                caweight =Weightele * 10;
                break;
            case "유흥" :
                caweight = Weightplay * 10;
                break;
            case "숙박" :
                caweight =Weighthotel * 10;
                break;
            default:
                caweight =Weightother * 10;
                break;
        }
        return caweight;
    }
    //페이타입과 남은 금액을 비교해 가중치를 반환해주는 함수
    private double comparePayavg(ArrayList<String> pay){
        double weight = 0;
        double zeropayBalance = user.getCard("zeropay").getBalance();
        double kyonggipayBalance = user.getCard("kyonggipay").getBalance();
        for(String s : pay){
            int c = 0;
            if(s.equals("zeropay") && c ==0){
                if(Zeropayavg > zeropayBalance)
                    weight = weight -10;
            }
            if(s.equals("kyonggipay")&& c ==0){
                if(Kyongipayavg > kyonggipayBalance)
                    weight = weight -10;
            }
            if(s.equals("zeropay") && c ==1){
                if(Zeropayavg <= zeropayBalance && weight < 0)
                    weight = 0;
            }

            if(s.equals("kyonggipay") && c ==1){
                if(Kyongipayavg <= kyonggipayBalance && weight < 0)
                    weight = 0;
            }
            c++;
        }
        return weight;
    }

}

