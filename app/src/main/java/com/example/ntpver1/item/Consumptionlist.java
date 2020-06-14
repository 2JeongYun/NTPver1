package com.example.ntpver1.item;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Consumptionlist {
    int balance;
    int card_id;
    int id;
    int pay;
    String pay_date;
    String store_name;
    String user_email;
    String card_kind;
    String category;

    public int getBalance() {
        return this.balance;
    }

    public int getCard_id() {
        return this.card_id;
    }

    public int getId() {
        return this.id;
    }

    public int getPay() {
        return this.pay;
    }

    public String getPay_date(){
        return this.pay_date;
    }

    public String getStore_name(){
        return this.store_name;
    }

    public String getUser_email(){
        return this.user_email;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void setCard_id(int card_id) {
        this.card_id = card_id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPay(int pay) {
        this.pay = pay;
    }

    public void setPay_date(String pay_date) {
        this.pay_date = pay_date;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public void setCard_kind(String card_kind){
        this.card_kind = card_kind;
    }

    public String getCard_kind(){
        return card_kind;
    }

    public void setCategory(String category){
        this.category = category;
    }

    public String getCategory(){
        return this.category;
    }
//    public String getDateAsString() {
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        String date = format.format(this.pay_date);
//        return date;
//    }
}