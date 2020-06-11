package com.example.ntpver1.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ntpver1.R;
import com.example.ntpver1.item.Consumptionlist;
import com.example.ntpver1.myinterface.OnConsumptionItemClickListener;

import java.util.ArrayList;

public class ConsumptionAdapter extends RecyclerView.Adapter<ConsumptionAdapter.ViewHolder> implements OnConsumptionItemClickListener {
    static final String TAG = "ConsumptionAdapter";

    ArrayList<Consumptionlist> items = new ArrayList<Consumptionlist>();
    static OnConsumptionItemClickListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_consumption, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Consumptionlist item = items.get(position);
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
        TextView dateTextView;
        TextView usedPlaceTextView;
        TextView balanceTextView;
        TextView costTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            dateTextView = itemView.findViewById(R.id.pay_date);
            usedPlaceTextView = itemView.findViewById(R.id.used_place);
            balanceTextView = itemView.findViewById(R.id.balance);
            costTextView = itemView.findViewById(R.id.cost);
        }

        public void setItem(Consumptionlist item) {
            dateTextView.setText(item.getPay_date());
            usedPlaceTextView.setText(item.getStore_name());
            balanceTextView.setText(Integer.toString(item.getBalance()));
            costTextView.setText(Integer.toString(item.getPay()));
        }
    }

    public void setClean() {
        Log.d(TAG, "setClean() called");
        items.clear();
    }

    //리스너 설정하기
    public void setOnItemClickListener(OnConsumptionItemClickListener listener) {
        this.listener = listener;
    }



    public void addItem(Consumptionlist item) {
        items.add(item);
    }

    public void setItems(ArrayList<Consumptionlist> items) {
        this.items = items;
    }

    public Consumptionlist getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, Consumptionlist item) {
        items.set(position, item);
    }
}
