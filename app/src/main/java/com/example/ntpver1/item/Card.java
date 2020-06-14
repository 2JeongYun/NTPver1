package com.example.ntpver1.item;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Card {
    private static HashMap<String, String> koName;
    String card_kinds;
    String card_number;
    Date valid_thru;
    ArrayList<Consumptionlist> usageHistory;
    int id;
    int balance;

    public Card() {
        koName = new HashMap<String, String> ();
        koName.put("kyonggipay", "경기페이");
        koName.put("zeropay", "제로페이");

        usageHistory = new ArrayList<>();
    }

    public String getKoName() {
        return koName.get(card_kinds);
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
}
