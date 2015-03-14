package com.foodvote.model;

/**
 * Created by Mike Park on 14/03/2015.
 */
public class User {

    String name;
    int deviceID;

    public User(String name, int deviceID) {
        this.name = name;
        this.deviceID = deviceID;
    }

    public int getDeviceID() {
        return deviceID;
    }

    public String getName() {
        return name;
    }
}
