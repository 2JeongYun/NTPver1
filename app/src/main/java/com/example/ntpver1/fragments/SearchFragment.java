package com.example.ntpver1.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ntpver1.MyTool;
import com.example.ntpver1.R;
import com.example.ntpver1.adapter.RecommendedAlgoritm;
import com.example.ntpver1.adapter.StoreAdapter;
import com.example.ntpver1.item.Store;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class SearchFragment extends Fragment implements MenuActivity.OnBackPressedListener {
    private static final String TAG = "SearchFragment";

    private StoreAdapter storeAdapter;
    private String Category;
    private MyTool myTool;
    private ArrayList<Button> categoryButtons;

    //인풋매니저(키보드관리)
    private InputMethodManager imm;

    //검색바
    private MaterialSearchBar searchBar;

    RecommendedAlgoritm recommendedAlgoritm;
    RecyclerView resultRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView() is called");

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_search, container, false);

        init(rootView);

        return rootView;
    }

    private void init(ViewGroup rootView) {
        imm = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);

        setRecyclerView(rootView);

        ArrayList<String> test = new ArrayList<>();

        recommendedAlgoritm = RecommendedAlgoritm.getInstance();

        recommendedAlgoritm.checkPermission();
        recommendedAlgoritm.getMyLocation();
    }

    public void setRecomendData() {
        storeAdapter.setClean();
        for(Store st : recommendedAlgoritm.getRecommendlist()) {
            Log.d(TAG, "recomlist lentg =  " + Integer.toString(recommendedAlgoritm.getRecommendlist().size()));
            Log.d(TAG, "add Store : " + st.getName());
            addResult(st);
        }
        storeAdapter.notifyDataSetChanged();
    }

    //결과데이터 추가
    private void addResult(Store store) {
        storeAdapter.addItem(store);
    }

    //리싸이클러뷰
    protected void setRecyclerView(ViewGroup rootView) {
        resultRecyclerView = rootView.findViewById(R.id.result_recycler_view);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        resultRecyclerView.setLayoutManager(layoutManager);

        storeAdapter = new StoreAdapter(StoreAdapter.RECOMMEND);

        resultRecyclerView.setAdapter(storeAdapter);
    }

    @Override
    public void onBack() {
        MenuActivity menuActivity = (MenuActivity)getActivity();
        menuActivity.setOnBackPressedListener(null);
        menuActivity.onTabSelected(0);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MenuActivity) MenuActivity.mContext).setOnBackPressedListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        ((MenuActivity) MenuActivity.mContext).setOnBackPressedListener(null);
    }
}
