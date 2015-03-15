package com.foodvote.model;

/**
 * Created by kinwa91 on 15-03-14.
 */

public class Round {

    // these are places IDs of the two places to be compared
    private String placeA;
    private String placeB;

    private int score;

    public Round(String placeA, String placeB) {
        this.placeA = placeA;
        this.placeB = placeB;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getPlaceA() {
        return placeA;
    }

    public void setPlaceA(String placeA) {
        this.placeA = placeA;
    }

    public String getPlaceB() {
        return placeB;
    }

    public void setPlaceB(String placeB) {
        this.placeB = placeB;
    }
}
