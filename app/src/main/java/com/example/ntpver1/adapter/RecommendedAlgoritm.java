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

import com.example.ntpver1.item.Card;
import com.example.ntpver1.item.Consumptionlist;
import com.example.ntpver1.item.Store;
import com.example.ntpver1.login.login.User;
import com.example.ntpver1.login.login.LoginManager;

import java.lang.reflect.Array;
import java.util.ArrayList;

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

    public void CalculationCategoryWeigt(){
        ArrayList<Card> cardlist = user.getCards();
        for(Card card: cardlist){
            for(Consumptionlist consumptionlist : card.getUsageHistory()){
                CalculationpaytypeWeigt(consumptionlist);
                count++;
                switch ("음식점"){
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

    public void CalculationpaytypeWeigt(Consumptionlist consumptionlist){

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


}

