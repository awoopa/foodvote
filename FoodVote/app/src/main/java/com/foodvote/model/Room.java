package com.foodvote.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mike Park on 14/03/2015.
 */
public class Room {
    String name;
    List<Place> places;
    List<User> users;
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
