package com.foodvote.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by William on 03/14/15.
 */
public class PlaceParser {

    private PlaceManager manager;

    public void parse(String input) {

        try {
            JSONObject obj = new JSONObject(input);
            JSONArray businesses = obj.getJSONArray("businesses");
                for (int i = 0; i < businesses.length(); i++) {
                    JSONObject business = businesses.getJSONObject(i);
                    String id = business.getString("id");
                    String name = business.getString("name");
                    String phone = business.getString("phone");
                    String displayPhone = business.getString("display_phone");
                    int rating = business.getInt("rating") * 2;
                    String imageURL = business.getString("image_url");
                    JSONObject location = business.getJSONObject("location");

                    HashMap<String, String> address = new HashMap<String, String>();
                    String street = location.getJSONArray("address").getString(1);
                    String city = location.getString("city");
                    String country = location.getString("country_code");
                    String postalCode = location.getString("postal_code");
                    String state = location.getString("state_code");
                    address.put("street", street);
                    address.put("city", city);
                    address.put("country", country);
                    address.put("postalCode", postalCode);
                    address.put("state", state);

                    JSONArray displayAddressArray = location.getJSONArray("display_address");
                    ArrayList<String> displayAddress = new ArrayList<String>();
                    for (int j = 0; i < displayAddressArray.length(); j++) {
                        displayAddress.add(displayAddressArray.getString(j));
                    }

                    Place place = new Place(id, name, phone, displayPhone, rating, address,
                            displayAddress, imageURL);
                    manager.add(place);
                }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

