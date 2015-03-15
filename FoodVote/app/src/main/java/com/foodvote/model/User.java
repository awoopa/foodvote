package com.foodvote.model;

/**
 * Created by Mike Park on 14/03/2015.
 */
public class User {
    String name;
    int deviceID;
    String socketId = "";

    public User(String name, int deviceID) {
        this.name = name;
        this.deviceID = deviceID;
    }
    public User() {

    }

    public User(String socketId, String userName) {
        this.name = userName;
        this.socketId = socketId;
    }

    

    public String getSocketId() {
        return socketId;
    }

    public void setSocketId(String socketId) {
        this.socketId = socketId;
    }

    public void setName(String name) {
        this.name = name;
    }
}
