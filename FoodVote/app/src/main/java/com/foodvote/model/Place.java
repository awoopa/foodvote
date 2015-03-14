package com.foodvote.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mike Park on 14/03/2015.
 */
public class Place {
    String id;
    String name;
    String phone;
    String displayPhone;
    int rating;
    HashMap<String, String> address;
    ArrayList<String> displayAddress;
    String imageURL;

    public Place() {
        this.id = "placeholder";
        this.name = "";
        this.phone = "";
        this.displayPhone = "";
        this.rating = -1;
        this.displayAddress = new ArrayList<String>();
        this.imageURL = "";
    }

    public Place(String id, String name, String phone, String displayPhone, int rating,
                 HashMap<String, String> address, ArrayList<String> displayAddress, String imageURL) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.displayPhone = displayPhone;
        this.rating = rating;
        this.address = address;
        this.displayAddress = displayAddress;
        this.imageURL = imageURL;
    }
}
