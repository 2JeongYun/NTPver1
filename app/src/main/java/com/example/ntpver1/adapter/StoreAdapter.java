package com.example.ntpver1.adapter;

import android.app.SearchManager;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ntpver1.R;
import com.example.ntpver1.item.Store;
import com.example.ntpver1.myinterface.OnStoreItemClickListener;

import java.util.ArrayList;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolder> implements OnStoreItemClickListener {
    static final String TAG = "StoreAdapter";

    ArrayList<Store> items = new ArrayList<Store>();
    static OnStoreItemClickListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.store_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Store item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView phoneTextView;
        TextView starTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.nameTextView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);
            starTextView = itemView.findViewById(R.id.starTextView);

            //클릭 리스너 : 리스너를 별도로 설정했을시 설정한 리스너로 동작
            nameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if (listener != null) {
                        listener.onItemClick(ViewHolder.this, view, position);
                    } else {
                        String keyWord = nameTextView.getText().toString();
                        Intent intent = new Intent();

                        intent.setAction(Intent.ACTION_WEB_SEARCH);
                        intent.putExtra(SearchManager.QUERY, keyWord);
                        view.getContext().startActivity(intent);
                    }
                }
            });
        }

        public void setItem(Store item) {
            nameTextView.setText(item.getName());
            phoneTextView.setText(item.getPhone());
            starTextView.setText(Integer.toString(item.getStar()));
        }
    }

    public void setClean() {
        Log.d(TAG, "setClean() called");
        items.clear();
    }

    //리스너 설정하기
    public void setOnItemClickListener(OnStoreItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        if (listener != null) {
            listener.onItemClick(holder, view, position);
        }
    }

    public void addItem(Store item) {
        items.add(item);
    }

    public void setItems(ArrayList<Store> items) {
        this.items = items;
    }

    public Store getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, Store item) {
        items.set(position, item);
    }
}
