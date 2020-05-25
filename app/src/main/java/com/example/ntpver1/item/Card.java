package com.example.ntpver1.item;

import java.util.ArrayList;

public class Card {
    String type;
    String number;
    String validity;
    ArrayList<String> usageHistory;

    public Card() {
        usageHistory = new ArrayList<>();
    }

    public void setUsageHistory(ArrayList<String> usageHistory) {
        this.usageHistory = usageHistory;
    }

    public ArrayList<String> getUsageHistory() {
        return this.usageHistory;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }
}
