package com.example.android.quakereport;

import java.util.Date;

public class Earthquake {
    double mMagnitude;
    String mPlace;
    long mDate;
    String url;

    public Earthquake(double mag, String place, long date, String url){
        this.mMagnitude = mag;
        this.mPlace = place;
        this.mDate = date;
        this.url = url;
    }

    public long getDate() {
        return mDate;
    }

    public double getMag() {
        return mMagnitude;
    }

    public String getPlace() {
        return mPlace;
    }
}
