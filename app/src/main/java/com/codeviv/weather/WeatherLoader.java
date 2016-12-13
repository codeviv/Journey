package com.codeviv.weather;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by vivek on 12/12/2016.
 */

public class WeatherLoader extends AsyncTaskLoader<List<Weather>> {
    private static final String LOG_TAG = WeatherLoader.class.getName();
    private String mUrl;

    public WeatherLoader(Context context, String mUrl) {
        super(context);
        this.mUrl = mUrl;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Weather> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        List<Weather> weathers = QueryUtils.fetchEarthqukeData(mUrl);
        return weathers;
    }

}
