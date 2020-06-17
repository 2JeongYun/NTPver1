package com.example.ntpver1.item;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Store implements Comparable<Store>, Serializable {
    ArrayList<String> pays;
    String name;
    String address;
    String phone;
    String type;
    double latitude;
    double longitude;
    int star;
    double weight;

    public Store(ArrayList<String> pays, String name, String address, String phone, String type, int star, double latitude, double longitude) {
        this.pays = pays;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.type = type;
        this.star = star;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public ArrayList<String> getPays() {
        return pays;
    }

    public void setPays(ArrayList<String> pays) {
        this.pays = pays;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getWeight(){
        return weight;
    }

    public void setWeight(double weight){
        this.weight = weight;
    }

    @Override
    public int compareTo(Store s) {
        if (this.weight > s.getWeight())
            return -1;
        else if(this.weight < s.getWeight())
            return 1;
        return 0;
    }
}
