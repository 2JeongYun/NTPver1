package com.example.ntpver1.adapter;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Paint;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ntpver1.MapActivity;
import com.example.ntpver1.MapManager;
import com.example.ntpver1.R;
import com.example.ntpver1.fragments.MenuActivity;
import com.example.ntpver1.item.Store;
import com.example.ntpver1.myinterface.OnStoreItemClickListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolder> implements OnStoreItemClickListener {
    static final String TAG = "StoreAdapter";
    public static final int SEARCH_RESULT = 0;
    public static final int RECOMMEND = 1;

    ArrayList<Store> items = new ArrayList<Store>();
    static OnStoreItemClickListener listener;

    public int mode = 0;

    public StoreAdapter(int mode) {
        this.mode = mode;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_store, parent, false);

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

    public class ViewHolder extends RecyclerView.ViewHolder {
        int star;
        TextView nameTextView;
        TextView phoneTextView;
        TextView addressTextView;
        TextView payTextView;
        RatingBar ratingBar;
        MapManager mapManager = MapManager.getInstance();

        public ViewHolder(View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.nameTextView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);
            addressTextView = itemView.findViewById(R.id.addressTextView);
            payTextView = itemView.findViewById(R.id.payTextView);
            ratingBar = itemView.findViewById(R.id.rating_bar);

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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if (listener != null) {
                        listener.onItemClick(ViewHolder.this, view, position);
                    } else {
                        if (mode == SEARCH_RESULT) {
                            Log.d(TAG, "Clicked item is " + getItem(position).getName());
                            mapManager.Findmarker(getItem(position));
                        }
                        else if (mode == RECOMMEND) {
                            Log.d(TAG, "getItem name : " + getItem(position).getName());
                            MapActivity.setMapMode(MapActivity.REQUEST_BY_ADAPTER);
                            MapActivity.setMapTargetStore(getItem(position));
                            ((MenuActivity) MenuActivity.mContext).onTabSelected(2);
                        }
                    }
                }
            });
        }

        public void setItem(Store item) {
            nameTextView.setText(item.getName());
            nameTextView.setPaintFlags(nameTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            phoneTextView.setText(item.getPhone());
            addressTextView.setText(item.getAddress());
            ratingBar.setRating(item.getStar());
            payTextView.setText(item.getPays().get(0));
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
