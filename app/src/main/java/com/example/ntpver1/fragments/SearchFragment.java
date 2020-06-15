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

public class SearchFragment extends Fragment implements MaterialSearchBar.OnSearchActionListener, MenuActivity.OnBackPressedListener {
    private static final String TAG = "SearchFragment";

    private StoreAdapter storeAdapter;
    private String Category;
    private MyTool myTool;
    private ArrayList<Button> categoryButtons;

    //인풋매니저(키보드관리)
    private InputMethodManager imm;

    //검색바
    private MaterialSearchBar searchBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView() is called");

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_search, container, false);

        init(rootView);

        return rootView;
    }

    private void init(ViewGroup rootView) {
        myTool = MyTool.getInstance();
        imm = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);

        ArrayList<View> views = myTool.getAllViews(rootView.findViewById(R.id.category_layout));
        categoryButtons = new ArrayList<>();
        for (int i = 0; i < views.size(); i++) {
            Button button = (Button) views.get(i);
            button.setOnClickListener(this::onClickListener);
            categoryButtons.add(button);
        }

        setRecyclerView(rootView);
        setSearchBar(rootView);

        //Test

        RecommendedAlgoritm recommendedAlgoritm = RecommendedAlgoritm.getInstance();

        recommendedAlgoritm.checkPermission();
        recommendedAlgoritm.getMyLocation();
        for(Store st : recommendedAlgoritm.getRecommendlist()){
            addResult(st);
        }

    }

    //카테고리버튼 클릭리스너
    public void onClickListener(View view) {
        Button button = (Button) view;
        this.Category = button.getText().toString();

        Log.d(TAG, button.getText().toString() + " is selected");

        for (Button btn : categoryButtons) {
            btn.setSelected(false);
        }
        button.setSelected(true);
    }

    //검색바
    private void setSearchBar(ViewGroup rootView) {
    searchBar = (MaterialSearchBar) rootView.findViewById(R.id.search_bar);
        searchBar.setOnSearchActionListener(this);
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
        String keyWord = searchBar.getText();

    }

    @Override
    public void onButtonClicked(int buttonCode) {
        //TEST
        //Toast.makeText(this, "Search bar onButtonClicked Method is called, code is " + buttonCode, Toast.LENGTH_SHORT).show();
    }

    //결과데이터 추가
    private void addResult(Store store) {
        storeAdapter.addItem(store);
    }

    //리싸이클러뷰
    protected void setRecyclerView(ViewGroup rootView) {
        RecyclerView resultRecyclerView = rootView.findViewById(R.id.result_recycler_view);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        resultRecyclerView.setLayoutManager(layoutManager);

        storeAdapter = new StoreAdapter();

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
