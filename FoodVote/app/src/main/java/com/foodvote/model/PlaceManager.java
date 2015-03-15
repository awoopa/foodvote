package com.foodvote.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by William on 03/14/15.
 */
public class PlaceManager {

    private static PlaceManager instance = null;
    private List<Place> places;

    public static PlaceManager getInstance() {
        if (instance == null) {
            instance = new PlaceManager();
        }
        return instance;
    }

    protected PlaceManager() {
        places = new ArrayList<Place>();
    }

    public Place get(int index) {
        return places.get(index);
    }

    public void add(Place p) {
        places.add(p);
    }

    public void clear() {
        places = new ArrayList<Place>();
    }

    public Place findPlaceById(String id) {
        for (Place p : places) {
            if (p.getId().equals(id))
                return p;
        }
        throw new RuntimeException("No Places found in PlaceManager using id " + id);
    }

    public int getSize() { return places.size(); }
}
