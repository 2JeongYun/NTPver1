package com.example.ntpver1.login.login;

import com.example.ntpver1.item.Card;

import java.util.ArrayList;

public class User {

    String userEmail;
    String userName;
    String phoneNumber;
    ArrayList<Card> cards = new ArrayList<Card>();

    public void uesrInit() {
        userEmail = "";
        userName = "";
        phoneNumber = "";
        cards.clear();
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setCards(Card card) {
        this.cards.add(card);
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    public Card getCard(String cardType) {
        for (Card card : cards) {
            if (card.getCard_kinds().equals(cardType))
                return card;
        }

        return null;
    }
}
