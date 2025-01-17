package com.example.ntpver1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.Toast;


import com.example.ntpver1.adapter.StoreAdapter;
import com.example.ntpver1.fragments.MenuActivity;
import com.example.ntpver1.item.Store;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mingle.sweetpick.CustomDelegate;
import com.mingle.sweetpick.SweetSheet;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MapActivity extends AppCompatActivity implements MaterialSearchBar.OnSearchActionListener {
    private static final String TAG = "MapActivity";
    private static final int SEARCH = 1;
    private static final int UPDATE = 2;
    private static final double TEST_LATITUDE_VALUE = 37.245347801;
    private static final double TEST_LONGITUDE_VALUE = 127.01442311;
    private static final int TEST_RADIUS_VALUE = 500;
    public static int REQUEST_BY_ADAPTER = 1;
    public static int NORMAL = 0;
    static int mode = NORMAL;


    private MapActivity thisClass = this;
    private Activity mapAct;
    public static Context mapContext;

    MyTool myTool;
    DBManager dbManager;
    MapManager mapManager;

    //상태
    static final int ON_SEARCH_SETTING_BUTTON = 1;
    static final int ON_SEARCH_SETTING_LAYOUT = 2;
    static final int ON_RESULT_LAYOUT = 3;

    int bottomLayoutState = ON_SEARCH_SETTING_BUTTON;

    //인풋매니저(키보드관리)
    InputMethodManager imm;

    //핸들러
    Handler handler = new Handler();

    //구글맵
    SupportMapFragment mapFragment;
    GoogleMap map;

    //리싸이클러뷰
    RecyclerView resultRecyclerView;
    StoreAdapter storeAdapter;

    //검색바
    MaterialSearchBar searchBar;

    //스윗시트
    SweetSheet sweetSheet;
    RelativeLayout sweetSheetRl;
    View ssView;

    //검색설정 테이블
    TableLayout payTableLayout;
    TableLayout storeTableLayout;
    ArrayList<String> payCategory;
    ArrayList<String> storeCategory;

    String keyWord;

    static Store targetStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mapAct = MapActivity.this;
        mapContext = this;

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        myTool = MyTool.getInstance();
        dbManager = DBManager.getInstance();

        setViews();
        setRecyclerView();

        searchSettingButtonListener(ssView);
    }

    private void setViews() {
        setMap();
        setSearchBar();
        setSweetSheet();
        setSearchSettingTable();
    }

    //구글맵프래그먼트
    private void setMap() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Log.d("Map", "Map is ready.");
                map = googleMap;

                map.setMinZoomPreference(15.0f);

                mapManager = MapManager.getInstance(map, thisClass);
                mapManager.checkPermission();

                if (mode == NORMAL) {
                    mapManager.getMyLocation();
                    mapManager.showMyLocation();
                }

                if (mode == REQUEST_BY_ADAPTER) {
                    mapManager.getMyLocation();
                    mapManager.showMyLocation();
                    handler.postDelayed(new Runnable(){
                        public void run()   {
                            mapManager.Marking(targetStore);
                            mapManager.Findmarker(targetStore);
                        }
                        }, 3000);

                }
            }
        });
    }

    //검색바
    private void setSearchBar() {
        searchBar = (MaterialSearchBar) findViewById(R.id.search_bar);
        searchBar.setOnSearchActionListener(this);
    }

    //스윗시트
    private void setSweetSheet() {
        sweetSheetRl = findViewById(R.id.sweet_sheet_rlayout);
        sweetSheet = new SweetSheet(sweetSheetRl);

        CustomDelegate customDelegate = new CustomDelegate(true,
                CustomDelegate.AnimationType.DuangLayoutAnimation);
        ssView = LayoutInflater.from(this).inflate(R.layout.map_bottom_layout, null, false);
        customDelegate.setCustomView(ssView);
        sweetSheet.setDelegate(customDelegate);

        sweetSheet.setBackgroundClickEnable(false);
    }

    //검색설정 테이블
    private void setSearchSettingTable() {
        payTableLayout = ssView.findViewById(R.id.pay_category_tlayout);
        storeTableLayout = ssView.findViewById(R.id.store_category_tlayout);
        payCategory = new ArrayList<>();
        storeCategory = new ArrayList<>();
    }

    //테이블 체크값 가져오기
    public void setSearchSettingTableValue() {
        CheckBox checkBox;

        payCategory.removeAll(payCategory);
        storeCategory.removeAll(storeCategory);

        ArrayList<View> payBoxes = myTool.getAllTableCheckBox(payTableLayout);

        for (View payBox : payBoxes) {
            checkBox = (CheckBox) payBox;
            if (checkBox.isChecked()) {
                payCategory.add(checkBox.getText().toString());
                Log.d("getTableValue", checkBox.getText().toString() + " is added.");
            }
        }

        ArrayList<View> storeBoxes = myTool.getAllTableCheckBox(storeTableLayout);
        for (View storeBox : storeBoxes) {
            checkBox = (CheckBox) storeBox;
            if (checkBox.isChecked()) {
                storeCategory.add(checkBox.getText().toString());
                Log.d("getTableValue", checkBox.getText().toString() + " is added.");
            }
        }

        //TEST
        Log.i("CheckCount", "Pay check count" + Integer.toString(payCategory.size()));
        Log.i("CheckCount", "Store check count" + Integer.toString(storeCategory.size()));
    }

    //리싸이클러뷰
    protected void setRecyclerView() {
        resultRecyclerView = ssView.findViewById(R.id.result_recycler_view);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        resultRecyclerView.setLayoutManager(layoutManager);

        storeAdapter = new StoreAdapter(StoreAdapter.SEARCH_RESULT);

        resultRecyclerView.setAdapter(storeAdapter);
    }

    //결과데이터 추가
    private void addResult(Store store) {
        storeAdapter.addItem(store);
    }

    //검색설정 버튼
    public void searchSettingButtonListener(View view) {
        bottomLayoutState = ON_SEARCH_SETTING_LAYOUT;
        mySetVisibility(bottomLayoutState);

        if (!sweetSheet.isShow()) {
            try {
                sweetSheet.show();
            } catch (Exception e) {
                Toast.makeText(this, "Please, Wait a second", Toast.LENGTH_SHORT).show();
                bottomLayoutState = ON_SEARCH_SETTING_BUTTON;
                mySetVisibility(bottomLayoutState);
            }
        }
    }

    //검색설정완료 버튼
    public void searchSettingConfirmButtonListener(View view) {
        mapManager.checkMoveCamera();

        sweetSheet.dismiss();

        setSearchSettingTableValue();

        bottomLayoutState = ON_SEARCH_SETTING_BUTTON;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mySetVisibility(bottomLayoutState);
            }
        }, 500);

        try {
            if (keyWord == null) {
                keyWord = "";
            }
            doSearch(mapManager.SearchCenter.latitude, mapManager.SearchCenter.longitude, TEST_RADIUS_VALUE, SEARCH);
        } catch (Exception e) {

        }
    }

    public void myLocationButtonListener(View view) {
        mapManager.clickButton();
    }

    //검색바 리스너
    @Override
    public void onSearchStateChanged(boolean enabled) {
        //TEST
        //Toast.makeText(this, "Search bar's onSearchStateChanged Method is called, parameter is " + enabled, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSearchConfirmed(CharSequence text) {
        imm.hideSoftInputFromWindow(searchBar.getWindowToken(), 0);
        keyWord = searchBar.getText();

        bottomLayoutState = ON_RESULT_LAYOUT;
        mySetVisibility(bottomLayoutState);

        //변경 jjs 05.19 try catch 추가
        try {
            doSearch(mapManager.SearchCenter.latitude, mapManager.SearchCenter.longitude, TEST_RADIUS_VALUE, SEARCH);
//                doSearch(keyWord, TEST_LATITUDE_VALUE, TEST_LONGITUDE_VALUE, TEST_RADIUS_VALUE, SEARCH);
        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onButtonClicked(int buttonCode) {
        //TEST
        //Toast.makeText(this, "Search bar onButtonClicked Method is called, code is " + buttonCode, Toast.LENGTH_SHORT).show();
    }

    //검색결과창 닫기 버튼
    public void resultCloseButtonListener(View view) {
        Log.d(TAG, "resultCloseButtonListener is called");

        sweetSheet.dismiss();

        bottomLayoutState = ON_SEARCH_SETTING_BUTTON;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mySetVisibility(bottomLayoutState);
            }
        }, 500);
    }

    public void doSearch(double latitude, double longitude, int radius, int requestCode) throws InterruptedException, ExecutionException, JSONException {
        Log.d(TAG, "doSearch() Called");

        dbManager.setSearchValue(keyWord, payCategory, storeCategory, latitude, longitude, radius);
        storeAdapter.setClean();
        mapManager.RemovePremarker();
        dbManager.readData(mapManager, dbManager, storeAdapter, sweetSheet); //변경 jjs 05.25
        Log.d(TAG, "getStoreList() is called");
        //변경 jjs 05.19 getResults로 list 받아오는 것으로 기존 콜백 list에서 변경
        Log.d(TAG, "1testset");

        if (requestCode == SEARCH) {
            if (!sweetSheet.isShow()) {
                try {
                    sweetSheet.show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Please, Wait a second", Toast.LENGTH_SHORT).show();
                    bottomLayoutState = ON_SEARCH_SETTING_BUTTON;
                    mySetVisibility(bottomLayoutState);
                }
            }
        }
    }

    //가시성 설정
    private void mySetVisibility(final int bottomLayoutState) {
        switch (bottomLayoutState) {
            case ON_SEARCH_SETTING_BUTTON:
                findViewById(R.id.search_setting_button).setVisibility(View.VISIBLE);
                ssView.findViewById(R.id.search_setting_rlayout).setVisibility(View.INVISIBLE);
                ssView.findViewById(R.id.result_rlayout).setVisibility(View.INVISIBLE);
                break;
            case ON_SEARCH_SETTING_LAYOUT:
                findViewById(R.id.search_setting_button).setVisibility(View.INVISIBLE);
                ssView.findViewById(R.id.search_setting_rlayout).setVisibility(View.VISIBLE);
                ssView.findViewById(R.id.result_rlayout).setVisibility(View.INVISIBLE);
                break;
            case ON_RESULT_LAYOUT:
                findViewById(R.id.search_setting_button).setVisibility(View.INVISIBLE);
                ssView.findViewById(R.id.search_setting_rlayout).setVisibility(View.INVISIBLE);
                ssView.findViewById(R.id.result_rlayout).setVisibility(View.VISIBLE);
                break;
        }
    }

    public static void setMapTargetStore(Store store) {
        targetStore = store;
    }

    //리스트 갱신 (무식하게 엎는 방법)
    public void refreshList() {
        Log.d(TAG, "refreshList() is called");
        storeAdapter.notifyDataSetChanged();
    }


    public Activity getActivity() {
        return mapAct;
    }

    public Context getContext() {
        return mapContext;
    }

    public static void setMapMode(int type) {
        mode = type;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        mapManager.init();
        super.onDestroy();
    }
}
