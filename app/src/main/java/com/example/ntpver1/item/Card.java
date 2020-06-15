package com.example.ntpver1.item;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Card implements Comparable {
    private static final String TAG = "Card";
    private static HashMap<String, String> koName = new HashMap<>();
    private static HashMap<String, String> enName = new HashMap<>();
    String card_kinds;
    String card_number;
    Date valid_thru;
    ArrayList<Consumptionlist> usageHistory;
    int id;
    int balance;

    public Card() {
        koName.put("kyonggipay", "경기페이");
        koName.put("zeropay", "제로페이");
        koName.put("alipay", "알리페이");
        koName.put("kakaopay", "카카오페이");
        enName.put("경기페이", "kyonggipay");
        enName.put("제로페이", "zeropay");
        enName.put("알리페이", "alipay");
        enName.put("카카오페이", "kakaopay");

        usageHistory = new ArrayList<>();
    }

    public int getUseCount() {
        return usageHistory.size();
    }

    public int getSpending(int start, int end) {
        String temp;
        String payDate;
        int payDateNum;

        int total = 0;

        for (Consumptionlist consumption : usageHistory) {
            payDate = consumption.getPay_date();
            temp = payDate.substring(0, 9);
            temp = temp.replaceAll("[^0-9]", "");
            Log.d(TAG, temp);
            payDateNum = Integer.parseInt(temp);
            Log.d(TAG, Integer.toString(payDateNum));

            if (payDateNum <= end && payDateNum >= start) {
                total += consumption.getPay();
            }
        }

        return total;
    }

    public void setUsageHistory(Consumptionlist csmpt) {
        this.usageHistory.add(csmpt);
    }

    public static String getKoName(String enName) {
        return koName.get(enName);
    }

    public static String getEnName(String koName) {
        return enName.get(koName);
    }

    public void setUsageHistory(ArrayList<Consumptionlist> usageHistory) {
        this.usageHistory = usageHistory;
    }

    public ArrayList<Consumptionlist> getUsageHistory() {
        return this.usageHistory;
    }

    public String getCard_kinds() {
        return this.card_kinds;
    }

    public void setCard_kinds(String card_kinds) {
        this.card_kinds = card_kinds;
    }

    public String getCard_number() {
        return this.card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBalance() {
        return this.balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public Date getValid_thru() {
        return valid_thru;
    }

    public void setValid_thru(Date valid_thru) {
        this.valid_thru = valid_thru;
    }

    @Override
    public int compareTo(Object card) {
        int useCount = this.getUseCount();
        Card compare = (Card) card;
        int compareCount = compare.getUseCount();

        if (useCount > compareCount)
            return -1;
        else if (useCount == compareCount)
            return 0;
        else
            return 1;
    }
}
