package com.example.ntpver1;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.example.ntpver1.adapter.CardAdapter;
import com.example.ntpver1.adapter.StoreAdapter;
import com.example.ntpver1.item.Store;
import com.koalap.geofirestore.GeoLocation;
import com.koalap.geofirestore.core.GeoHash;
import com.koalap.geofirestore.core.GeoHashQuery;
import com.koalap.geofirestore.util.Base32Utils;
import com.koalap.geofirestore.util.Constants;
import com.koalap.geofirestore.util.GeoUtils;
import com.mingle.sweetpick.SweetSheet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class DBManager {

    private DBManager() {
        results = new ArrayList<>();
        payCategory = new ArrayList<>();
        storeCategory = new ArrayList<>();
    }

    private static DBManager dbManager;

    private static final String TAG = "DBManager";
    private ArrayList<Store> results;
    private ArrayList<String> payCategory;
    private ArrayList<String> storeCategory;
    private String IP_ADDRESS = "34.64.134.202";

    //가게찾기 변수 jjs
    double latitude;//추가 jjs
    double longitude;//추가 jjs
    int radius;//추가 jjs
    String keyWord;

    //유저로그인 변수 jjs 05.25
    String email;
    String password;

    //카드찾기 변수 jjs 05.25
    String user_id;//소비리스트찾기에서 사용

    //소비리스트찾기 변수 jjs 05.25
    String card_id;

    //카드등록 변수 jjs 05.25
    String card_kinds;
    String card_number;
    String valid_thru;

    //유저등록 변수 jjs 05.25
    String user_name;
    String phone_number;

    public static DBManager getInstance() {
        if (dbManager == null) {
            dbManager = new DBManager();
        }

        return dbManager;
    }

    //가게정보찾기
    public void readData(MapManager mapManager, DBManager dbManager, StoreAdapter storeAdapter, SweetSheet sweetSheet) throws ExecutionException, InterruptedException {
        String payString = this.payCategory.toString();
        payString = payString.replaceAll(" ", "");
        payString = payString.replace("[", "");
        payString = payString.replace("]", "");
        String categoryString = this.storeCategory.toString();
        categoryString = categoryString.replaceAll(" ", "");
        categoryString = categoryString.replace("[", "");
        categoryString = categoryString.replace("]", "");

        Set<GeoHashQuery> newQueries = GeoHashQuery.queriesAtLocation(new GeoLocation(this.latitude, this.longitude), this.radius);
        System.out.println(newQueries);
        String json_string = "";

        for (final GeoHashQuery query : newQueries) {
            SelectStoreData task = new SelectStoreData(mapManager, dbManager, storeAdapter, sweetSheet);
            task.execute("http://" + IP_ADDRESS + "/select.php", this.keyWord, payString, categoryString, query.getStartValue(), query.getEndValue());
            String result = task.get();
        }
    }

    //가게리스트정보찾기
    public void readStoreListData(ArrayList<Store> stlist) throws ExecutionException, InterruptedException {
        Set<GeoHashQuery> newQueries = GeoHashQuery.queriesAtLocation(new GeoLocation(this.latitude, this.longitude), this.radius);
        SelectStoreListData task = new SelectStoreListData(stlist);
        for (final GeoHashQuery query : newQueries) {
            task.execute("http://" + IP_ADDRESS + "/select.php", "", "", "", query.getStartValue(), query.getEndValue());
            task.get();
        }
    }

    //유저정보찾기 jjs 05.25 login failed 판별을 위해 boolean 으로 바꿈 jjs 05.27
    public boolean readUserData() throws ExecutionException, InterruptedException, JSONException {
        SelectUserData task = new SelectUserData();
        task.execute("http://" + this.IP_ADDRESS + "/select_user.php", this.email, this.password);
        String result = task.get();
        return !result.contains("FAIL");
    }

    //카드정보찾기 jjs 05.25
    public void readCardData() throws ExecutionException, InterruptedException, JSONException {
        SelectCardData task = new SelectCardData();
        task.execute("http://" + this.IP_ADDRESS + "/select_card.php", this.email);
        task.get();
    }

    //카드정보등록하기 jjs 05.25
    public void writeCardData() throws ExecutionException, InterruptedException, JSONException {
        String json_string = "";
        InsertCardData task = new InsertCardData();
        task.execute("http://" + this.IP_ADDRESS + "/insert_card.php", this.card_kinds, this.card_number, this.user_id, this.valid_thru);
        String result = task.get();
        if (result.equals("success")) {
            System.out.println(result);
        }
        else{
            System.out.println("error");
        }
    }

    //유저정보등록하기 jjs 05.25
    public void writeUserData() throws ExecutionException, InterruptedException, JSONException {
        String json_string = "";
        InsertUserData task = new InsertUserData();
        task.execute("http://" + this.IP_ADDRESS + "/insert_user.php", this.email, this.password, this.user_name, this.phone_number);
        String result = task.get();
        if (result.equals("success")) {
            System.out.println(result);
        }
        else{
            System.out.println(result);
        }
    }

    //소비리스트찾기 jjs 05.25
    public void readConsumptionlistData() throws ExecutionException, InterruptedException, JSONException {
        SelectConsumptionlistData task = new SelectConsumptionlistData();
        task.execute("http://" + this.IP_ADDRESS + "/select_consumptionlist.php", this.email, this.card_kinds);
        task.get();
    }

    //인증번호요청 jjs 06.08
    public String sendCertificationData() throws ExecutionException, InterruptedException, JSONException {
        SendCertificationData task = new SendCertificationData();
        task.execute("http://" + this.IP_ADDRESS + "/send_mail.php",this.email);
        String result = task.get();
        System.out.println(result);
        return result;
    }

    //유저정보찾기설정 05.25 jjs
    public void setSearchUserValue(String email, String password) {
        //이메일
        this.email = email;
        //페스워드
        this.password = password;
    }

    //카드정보찾기설정 05.25 jjs
    public void setSearchCardValue(String email) {
        //user_id
        this.email = email;
    }

    //소비리스트정보찾기설정 05.25 jjs
    public void setSearchConsumptionlistValue(String user_email, String card_kinds) {
        //user_id
        this.email = user_email;
        //user_id
        this.card_kinds = card_kinds;
    }

    //가게검색설정
    public void setSearchValue(String keyWord, ArrayList<String> payCategory, ArrayList<String> storeCategory, double latitude, double longitude, int radius) {
        //키워드
        this.keyWord = keyWord;
        //페이종류
        if (payCategory.isEmpty()){
            this.payCategory.clear();
        }
        else{
            this.payCategory = payCategory;
        }
        //가게카테고리
        if (storeCategory.isEmpty()){
            this.storeCategory.clear();
        }
        else{
            this.storeCategory = storeCategory;
        }
        //위도
        this.latitude = latitude;
        //경도
        this.longitude = longitude;
        //범위
        this.radius = radius;
    }

    //가게리스트검색설정
    public void setSearchStoreListValue(double latitude, double longitude, int radius) {

        //위도
        this.latitude = latitude;
        //경도
        this.longitude = longitude;
        //범위
        this.radius = radius;
    }

    //유저등록설정 05.25 jjs
    public void setInsertUserValue(String email, String password, String user_name, String phone_number) {
        this.email = email;
//        this.user_id = user_id;
        this.password = password;
        this.user_name = user_name;
        this.phone_number = phone_number;
    }

    //카드등록설정 05.25 jjs
    public void setInsertCardValue(String card_kinds, String card_number, String email, String valid_thru) {
        this.card_kinds = card_kinds;
        this.card_number = card_number;
        this.email = email;
        this.valid_thru = valid_thru;
    }

    //인증번호요청설정 jjs 06.08
    public void setSendCertificationValue(String email){
        this.email = email;
    }

    public ArrayList<Store> getResults() {
        return results;
    }

    public static GeoHashQuery queryForGeoHash(GeoHash geohash, int bits) {
        String hash = geohash.getGeoHashString();
        int precision = (int) Math.ceil((double) bits / Base32Utils.BITS_PER_BASE32_CHAR);
        if (hash.length() < precision) {
            return new GeoHashQuery(hash, hash + "~");
        }
        hash = hash.substring(0, precision);
        String base = hash.substring(0, hash.length() - 1);
        int lastValue = Base32Utils.base32CharToValue(hash.charAt(hash.length() - 1));
        int significantBits = bits - (base.length() * Base32Utils.BITS_PER_BASE32_CHAR);
        int unusedBits = Base32Utils.BITS_PER_BASE32_CHAR - significantBits;
        // delete unused bits
        int startValue = (lastValue >> unusedBits) << unusedBits;
        int endValue = startValue + (1 << unusedBits);
        String startHash = base + Base32Utils.valueToBase32Char(startValue);
        String endHash;
        if (endValue > 31) {
            endHash = base + "~";
        } else {
            endHash = base + Base32Utils.valueToBase32Char(endValue);
        }
        return new GeoHashQuery(startHash, endHash);
    }

    public static Set<GeoHashQuery> queriesAtLocation(GeoLocation location, double radius) {
        int queryBits = Math.max(1, GeoHashQuery.Utils.bitsForBoundingBox(location, radius));
        int geoHashPrecision = (int) Math.ceil((float) queryBits / Base32Utils.BITS_PER_BASE32_CHAR);

        double latitude = location.latitude;
        double longitude = location.longitude;
        double latitudeDegrees = radius / Constants.METERS_PER_DEGREE_LATITUDE;
        double latitudeNorth = Math.min(90, latitude + latitudeDegrees);
        double latitudeSouth = Math.max(-90, latitude - latitudeDegrees);
        double longitudeDeltaNorth = GeoUtils.distanceToLongitudeDegrees(radius, latitudeNorth);
        double longitudeDeltaSouth = GeoUtils.distanceToLongitudeDegrees(radius, latitudeSouth);
        double longitudeDelta = Math.max(longitudeDeltaNorth, longitudeDeltaSouth);

        Set<GeoHashQuery> queries = new HashSet<>();

        GeoHash geoHash = new GeoHash(latitude, longitude, geoHashPrecision);
        GeoHash geoHashW = new GeoHash(latitude, GeoUtils.wrapLongitude(longitude - longitudeDelta), geoHashPrecision);
        GeoHash geoHashE = new GeoHash(latitude, GeoUtils.wrapLongitude(longitude + longitudeDelta), geoHashPrecision);

        GeoHash geoHashN = new GeoHash(latitudeNorth, longitude, geoHashPrecision);
        GeoHash geoHashNW = new GeoHash(latitudeNorth, GeoUtils.wrapLongitude(longitude - longitudeDelta), geoHashPrecision);
        GeoHash geoHashNE = new GeoHash(latitudeNorth, GeoUtils.wrapLongitude(longitude + longitudeDelta), geoHashPrecision);

        GeoHash geoHashS = new GeoHash(latitudeSouth, longitude, geoHashPrecision);
        GeoHash geoHashSW = new GeoHash(latitudeSouth, GeoUtils.wrapLongitude(longitude - longitudeDelta), geoHashPrecision);
        GeoHash geoHashSE = new GeoHash(latitudeSouth, GeoUtils.wrapLongitude(longitude + longitudeDelta), geoHashPrecision);

        queries.add(queryForGeoHash(geoHash, queryBits));
        queries.add(queryForGeoHash(geoHashE, queryBits));
        queries.add(queryForGeoHash(geoHashW, queryBits));
        queries.add(queryForGeoHash(geoHashN, queryBits));
        queries.add(queryForGeoHash(geoHashNE, queryBits));
        queries.add(queryForGeoHash(geoHashNW, queryBits));
        queries.add(queryForGeoHash(geoHashS, queryBits));
        queries.add(queryForGeoHash(geoHashSE, queryBits));
        queries.add(queryForGeoHash(geoHashSW, queryBits));

        // Join queries
        boolean didJoin;
        do {
            GeoHashQuery query1 = null;
            GeoHashQuery query2 = null;
            for (GeoHashQuery query : queries) {
                for (GeoHashQuery other : queries) {
                    if (query != other && query.canJoinWith(other)) {
                        query1 = query;
                        query2 = other;
                        break;
                    }
                }
            }
            if (query1 != null && query2 != null) {
                queries.remove(query1);
                queries.remove(query2);
                queries.add(query1.joinWith(query2));
                didJoin = true;
            } else {
                didJoin = false;
            }
        } while (didJoin);

        return queries;
    }

}