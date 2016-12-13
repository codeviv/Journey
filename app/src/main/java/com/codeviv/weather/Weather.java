package com.codeviv.weather;

/**
 * Created by vivek on 12/12/2016.
 */

public class Weather {
    private String mPlace;
    private long mDate;
    private double mTemp;

    public Weather(String place, long date, double temp) {
        mPlace = place;
        mDate = date;
        mTemp = temp;
    }

    public String getmPlace() {
        return mPlace;
    }

    public long getmDate() {
        return mDate;
    }

    public double getmTemp() {
        return mTemp;
    }
}
