package com.example.ntpver1.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ntpver1.DBManager;
import com.example.ntpver1.R;
import com.example.ntpver1.adapter.CardAdapter;
import com.example.ntpver1.adapter.StoreAdapter;
import com.example.ntpver1.item.Card;
import com.example.ntpver1.login.login.LoginManager;
import com.example.ntpver1.login.login.User;
import com.example.ntpver1.login.register.UserRegisterActivity;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

public class MyInfoFragment extends Fragment {
    private static final String TAG = "MyInfoFragment";
    private static CardAdapter cardAdapter;
    RecyclerView cardRecyclerView;
    Button registCardButton;
    DBManager dbManager;
    LoginManager loginManager = LoginManager.getInstance();

    TextView rankFirstTextView;
    TextView rankSecondTextView;
    TextView rankThirdTextView;

    TextView incomeTextView;
    TextView spendingTextView;
    TextView totalTextView;
    TextView userTotalTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView() is called");

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_my_info, container, false);
        dbManager = DBManager.getInstance();

        init(rootView);

        return rootView;
    }

    private void init(ViewGroup rootView) {
        setRecyclerView(rootView);
        registCardButton = rootView.findViewById(R.id.regist_card);

        rankFirstTextView = rootView.findViewById(R.id.rank_first);
        rankSecondTextView = rootView.findViewById(R.id.rank_second);
        rankThirdTextView = rootView.findViewById(R.id.rank_third);

        incomeTextView = rootView.findViewById(R.id.income);
        spendingTextView = rootView.findViewById(R.id.spending);
        totalTextView = rootView.findViewById(R.id.total);
        userTotalTextView = rootView.findViewById(R.id.user_total_balance);

        setStatistics();

        registCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CardRegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    public void setStatistics() {
        int total = 0;
        int income = 0;
        int spending = 0;
        int userTotal = 0;

        User user = loginManager.getUser();
        ArrayList<Card> cards = user.getCards();
        ArrayList<TextView> rankings = new ArrayList<>();
        Collections.sort(cards);

        for (Card card : cards) {
            Log.d(TAG, "cards use cnt : " + card.getCard_kinds() + Integer.toString(card.getUseCount()));
        }

        rankings.add(rankFirstTextView);
        rankings.add(rankSecondTextView);
        rankings.add(rankThirdTextView);

        Log.d(TAG, Integer.toString(cards.size()));

        for (int i = 0; i < cards.size(); i++) {
            rankings.get(i).setText(Integer.toString(i+1) + ". " + cards.get(i).getKoName(cards.get(i).getCard_kinds()));
            if (i >= 2)
                break;
        }

        for (Card card : cards) {
            int history = card.getSpending(Integer.MIN_VALUE, Integer.MAX_VALUE);
            if (0 <= history)
                income += history;
            else
                spending += history;
            total += card.getSpending(Integer.MIN_VALUE, Integer.MAX_VALUE);

            userTotal += card.getBalance();
        }

        incomeTextView.setText("+" + Integer.toString(income) + " 원");
        spendingTextView.setText(Integer.toString(spending) + " 원");
        if (total > 0)
            totalTextView.setText("+" + Integer.toString(total) + " 원");
        else
            totalTextView.setText(Integer.toString(total) + " 원");

        userTotalTextView.setText(userTotal + " 원");
    }

    //리싸이클러뷰
    protected void setRecyclerView(ViewGroup rootView) {
        cardRecyclerView = rootView.findViewById(R.id.card_list);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        cardRecyclerView.setLayoutManager(layoutManager);

        cardRecyclerView.setAdapter(cardAdapter);
    }

    public void refreshList() {
        Log.d(TAG, "refreshList() is called");
        cardAdapter.notifyDataSetChanged();
    }

    public static CardAdapter getCardAdapterInstance() {
        if (cardAdapter == null) {
            cardAdapter = new CardAdapter();
        }

        cardAdapter.setClean();

        return cardAdapter;
    }

}
