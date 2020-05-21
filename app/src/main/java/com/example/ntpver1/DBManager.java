package com.example.ntpver1;

import android.util.Log;

import com.example.ntpver1.item.Store;
import com.google.firebase.firestore.FirebaseFirestore;
import com.koalap.geofirestore.GeoLocation;
import com.koalap.geofirestore.core.GeoHash;
import com.koalap.geofirestore.core.GeoHashQuery;
import com.koalap.geofirestore.util.Base32Utils;
import com.koalap.geofirestore.util.Constants;
import com.koalap.geofirestore.util.GeoUtils;

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
        db = FirebaseFirestore.getInstance();

        setTest();
    }

    private static DBManager dbManager;

    private static final String TAG = "DBManager";
    FirebaseFirestore db;

    private ArrayList<Store> results;
    private ArrayList<String> payCategory;
    private ArrayList<String> storeCategory;
    double latitude;//추가 jjs
    double longitude;//추가 jjs
    int radius;//추가 jjs
    String keyWord;

    public static DBManager getInstance() {
        if (dbManager == null) {
            dbManager = new DBManager();
        }

        return dbManager;
    }

    private void setTest() {
        keyWord = "치킨";
        payCategory.add("경기페이");
        payCategory.add("제로페이");
        storeCategory.add("음식점");
        storeCategory.add("식료품점");
    }

    public void readData() throws ExecutionException, InterruptedException, JSONException {
        Set<GeoHashQuery> newQueries = GeoHashQuery.queriesAtLocation(new GeoLocation(this.latitude, this.longitude), this.radius);

        System.out.println(newQueries);
        String json_string = "";
        for (final GeoHashQuery query : newQueries) {
            String IP_ADDRESS = "34.64.134.202";
            SelectData task = new SelectData();
            /*
            if (this.keyWord.equals("") && this.payCategory.isEmpty() && this.storeCategory.isEmpty()) {
                task.execute("http://" + IP_ADDRESS + "/select.php", "", "", "", query.getStartValue(), query.getEndValue());
                String result = task.get();
                json_string += result;
            }
            else if (this.keyWord.equals("") && this.payCategory.isEmpty()){
                for (final String category  : this.storeCategory) {
                    task.execute("http://" + IP_ADDRESS + "/select.php", "", "", category, query.getStartValue(), query.getEndValue());
                    String result = task.get();
                    json_string += result;
                }
            }
            else if (this.keyWord.equals("") && this.storeCategory.isEmpty()){
                for (final String payName  : this.payCategory) {
                    task.execute("http://" + IP_ADDRESS + "/select.php", "", payName, "", query.getStartValue(), query.getEndValue());
                    String result = task.get();
                    json_string += result;
                }
            }
            else if (this.keyWord.equals("")){
                for (final String payName  : this.payCategory) {
                    for (final String category  : this.storeCategory) {
                        task.execute("http://" + IP_ADDRESS + "/select.php", "", payName, category, query.getStartValue(), query.getEndValue());
                        String result = task.get();
                        json_string += result;
                    }
                }
            }
            else if (this.payCategory.isEmpty()){
                for (final String category  : this.storeCategory) {
                    task.execute("http://" + IP_ADDRESS + "/select.php", this.keyWord, "", category, query.getStartValue(), query.getEndValue());
                    String result = task.get();
                    json_string += result;
                }
            }
            else if (this.storeCategory.isEmpty()){
                for (final String payName  : this.payCategory) {
                    task.execute("http://" + IP_ADDRESS + "/select.php", this.keyWord, payName, "", query.getStartValue(), query.getEndValue());
                    String result = task.get();
                    json_string += result;
                }
            }
            else{
                for (final String payName  : this.payCategory) {
                    for (final String category  : this.storeCategory) {
                        task.execute("http://" + IP_ADDRESS + "/select.php", this.keyWord, payName, category, query.getStartValue(), query.getEndValue());
                        String result = task.get();
                        json_string += result;
                    }
                }
            }
            */
            task.execute("http://" + IP_ADDRESS + "/select.php", this.keyWord, "kyonggipay", "", query.getStartValue(), query.getEndValue());
            String result = task.get();
            json_string += result;
        }
        System.out.println(json_string);
        JSONArray jsonArray = new JSONArray(json_string);
        for(int i=0; i<10; i++)
        {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            System.out.println("주소 = " + jsonObject.getString("address"));
            System.out.println("이름 = " + jsonObject.getString("store_name"));
            String payName = jsonObject.getString("pay_name");
            String storeName = jsonObject.getString("store_name");
            String phoneNumber = jsonObject.getString("phone_number");
            String category = jsonObject.getString("category");
            double latitude = jsonObject.getDouble("latitude");
            double longitude = jsonObject.getDouble("longitude");
            results.add(makeStore(payName, storeName, phoneNumber, category, latitude, longitude));
        }
    }

    //구현 부탁함다~
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

    //구현 부탁함다~
    private Store makeStore(String payName, String storeName, String phoneNumber, String category, double latitude, double longitude) {
        ArrayList<String> pays = new ArrayList<>();
        pays.add(payName);
        Store s = new Store(pays, storeName, phoneNumber, category, 0, latitude, longitude);
        return s;
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