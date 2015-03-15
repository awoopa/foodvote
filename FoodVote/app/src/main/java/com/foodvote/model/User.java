package com.foodvote.model;

/**
 * Created by Mike Park on 14/03/2015.
 */
public class User {

    String name;
    int deviceID;
    String socketID;

    public User(String name, int deviceID) {
        this.name = name;
        this.deviceID = deviceID;
        this.socketID = "";
    }

    public User() {
        this.name = "";
        this.deviceID =  -1;
        this.socketID = "";

    }

    public User(String socketID, String userName) {
        this.name = userName;
        this.socketID = socketID;
        this.deviceID = -1;
    }

    public String getSocketId() {
        return socketID;
    }

    public void setSocketId(String socketID) {
        this.socketID = socketID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDeviceID() {
        return deviceID;
    }

    public String getName() {
        return name;
    }
}
