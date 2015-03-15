package com.foodvote.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Mike Park on 14/03/2015.
 */
public class Room {
    String name;
    List<Place> places;
    List<User> users;
    int radius;
    double lat;
    double lon;
    Date date;

    String roomId;


    Boolean completed;
    Place chosenPlace;

    public Room(String name) {
        this.name = name;
        this.places = new ArrayList<Place>();
        this.users = new ArrayList<User>();
        this.completed = false;
        this.chosenPlace = new Place();
    }

    public Room(String name, List<Place> places, List<User> users, Boolean completed, Place chosenPlace) {
        this.name = name;
        this.places = places;
        this.users = users;
        this.completed = completed;
        this.chosenPlace = chosenPlace;
    }

    public Room(String roomId, String roomName, int radius, double lat, double lon, Date date) {
        this.roomId = roomId;
        this.name = roomName;
        this.radius = radius;
        this.lat = lat;
        this.lon = lon;
        this.date = date;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void setChosenPlace(Place chosenPlace) {
        this.chosenPlace = chosenPlace;
    }

    public void pickOneFromEqualPlaces(List<String> places) {
        PlaceManager pm = PlaceManager.getInstance();

    }


    public String getName() {
        return name;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public List<User> getUsers() {
        return users;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public Place getChosenPlace() {
        return chosenPlace;
    }
}
