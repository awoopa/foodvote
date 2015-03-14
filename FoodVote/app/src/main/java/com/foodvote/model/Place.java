package com.foodvote.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mike Park on 14/03/2015.
 */
public class Place {
    String id;
    String name;
    String phone;
    int rating;
    Map<String, String> address;
    String imageURL;

    public Place() {
        this.id = "placeholder";
        this.name = "";
        this.phone = "";
        this.rating = -1;
        this.address = new HashMap<String, String> ();
        this.imageURL = "";
    }

    public Place(String id, String name, String phone, int rating, Map<String, String> address, String imageURL) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.rating = rating;
        this.address = address;
        this.imageURL = imageURL;
    }
}
