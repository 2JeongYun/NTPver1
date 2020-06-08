package com.example.ntpver1.adapter;

import android.app.SearchManager;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ntpver1.R;
import com.example.ntpver1.fragments.MenuActivity;
import com.example.ntpver1.item.Card;
import com.example.ntpver1.myinterface.OnCardItemClickListener;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> implements OnCardItemClickListener {
    static final String TAG = "CardAdapter";

    ArrayList<Card> items = new ArrayList<Card>();
    static OnCardItemClickListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_card, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Card item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        if (listener != null) {
            listener.onItemClick(holder, view, position);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView cardIDTextView;
        TextView balanceTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            cardIDTextView = itemView.findViewById(R.id.card_id);
            balanceTextView = itemView.findViewById(R.id.balance);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if (listener != null) {
                        listener.onItemClick(ViewHolder.this, view, position);
                    } else {
                        ((MenuActivity) MenuActivity.mContext).startCardInfoFragment();
                    }
                }
            });
        }

        public void setItem(Card item) {
            cardIDTextView.setText(item.getCard_kinds());
            balanceTextView.setText(Integer.toString(item.getBalance()));
        }
    }

    public void setClean() {
        Log.d(TAG, "setClean() called");
        items.clear();
    }

    //리스너 설정하기
    public void setOnItemClickListener(OnCardItemClickListener listener) {
        this.listener = listener;
    }



    public void addItem(Card item) {
        items.add(item);
    }

    public void setItems(ArrayList<Card> items) {
        this.items = items;
    }

    public Card getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, Card item) {
        items.set(position, item);
    }
}