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

    private static final String[] JSONKeys = {"id", "name", "phone", "display_phone", "image_url"};
    private static final String[] addressJSONKeys = {"city", "country_code", "postal_code", "state_code"};
    private static final String[] addressHashMapKeys = {"city", "country", "postalCode", "state"};
    private String id = "";
    private String name = "";
    private String phone = "";
    private String displayPhone = "";
    private String imageURL = "";
    private String[] stringParams = {id, name, phone, displayPhone, imageURL};
    int rating;
    private HashMap<String, String> address;
    private ArrayList<String> displayAddress;

    public void parse(String input) {
        manager = PlaceManager.getInstance();
        try {
            JSONObject obj = new JSONObject(input);
            JSONArray businesses = obj.getJSONArray("businesses");
                for (int i = 0; i < businesses.length(); i++) {
                    JSONObject business = businesses.getJSONObject(i);


                    parseStrings(JSONKeys, stringParams, business);
                    //String id = business.getString("id");
                    //String name = business.getString("name");
                    //String phone = business.getString("phone");
                    //String displayPhone = business.getString("display_phone");
                    try {
                        rating = business.getInt("rating") * 2;
                    } catch (JSONException e) {
                        rating = -1;
                    }
                    //String imageURL = business.getString("image_url");
                    JSONObject location;
                    address = new HashMap<>();
                    try {
                        location = business.getJSONObject("location");
                    } catch (JSONException e) {
                        location = new JSONObject();
                    }

                    String street;
                    try {
                        street = location.getJSONArray("address").getString(1);
                    } catch (JSONException e) {
                        street = "";
                    }
                    address.put("street", street);
                    parseAndAddToHashMap(addressJSONKeys, addressHashMapKeys, address, location);
                    //String city = location.getString("city");
                    //String country = location.getString("country_code");
                    //String postalCode = location.getString("postal_code");
                    //String state = location.getString("state_code");
                    //address.put("city", city);
                    //address.put("country", country);
                    //address.put("postalCode", postalCode);
                    //address.put("state", state);

                    JSONArray displayAddressArray;
                    displayAddress = new ArrayList<>();
                    try {
                        displayAddressArray = location.getJSONArray("display_address");
                    } catch (JSONException e) {
                        displayAddressArray = new JSONArray();
                    }

                    for (int j = 0; j < displayAddressArray.length(); j++) {
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

    private void parseAndAddToHashMap(String[] jsonKeys, String[] keys, HashMap<String, String> map, JSONObject json) {
        for (int i=0; i<jsonKeys.length; i++) {
            try {
                String s = json.getString(jsonKeys[i]);
                map.put(keys[i], s);
            } catch (JSONException e) {
                String s = "";
                map.put(keys[i], s);
            }
        }
    }

    private void parseStrings(String[] jsonKeys, String[] stringParams, JSONObject json) {
        for (int i=0; i<jsonKeys.length; i++) {
            try {
                stringParams[i] = json.getString(jsonKeys[i]);
            } catch (JSONException e) {
                stringParams[i] = "";
            }
        }
        id = stringParams[0];
        name = stringParams[1];
        phone = stringParams[2];
        displayPhone = stringParams[3];
        imageURL = stringParams[4];
    }
}

