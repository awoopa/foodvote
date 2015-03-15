package com.foodvote.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kinwa91 on 15-03-14.
 */

public class Round implements Parcelable {

    // these are places IDs of the two places to be compared
    private String placeA;
    private String placeB;

    private int score;

    public Round(String placeA, String placeB) {
        this.placeA = placeA;
        this.placeB = placeB;
    }

    public Round(Parcel parcel) {
        this.placeA = parcel.readString();
        this.placeB = parcel.readString();
        this.score = parcel.readInt();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.placeA);
        dest.writeString(this.placeB);
        dest.writeInt(this.score);
    }

    public static Creator<Round> CREATER = new Creator<Round>() {
        @Override
        public Round createFromParcel(Parcel source) {
            return new Round(source);
        }

        @Override
        public Round[] newArray(int size) {
            return new Round[size];
        }
    };
}
