package com.codeviv.weather;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;

import static com.codeviv.weather.MainActivity.location;

public class GraphActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Weather>> {

    public static final String LOG_TAG = MainActivity.class.getName();
    private static final String USGS_REQUEST_URL = "http://api.openweathermap.org/data/2.5/forecast/city";
    private static final int WEATHER_LOADER_ID = 1;
    private TextView mEmptyStateView;
    Weather currentWeather;
    GraphView graph;
    String city;
    long date;
    double temp;
    LineGraphSeries<DataPoint> series;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        //To display the Empty state of our app
        mEmptyStateView = (TextView) findViewById(R.id.empty_text_view);


        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network_info = connMgr.getActiveNetworkInfo();

        if (network_info != null && network_info.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(WEATHER_LOADER_ID, null, this);
        } else {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            mEmptyStateView.setText(R.string.no_internet);
        }
    }

    @Override
    public Loader<List<Weather>> onCreateLoader ( int i, Bundle bundle){
        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("q", location);
        uriBuilder.appendQueryParameter("units", "metric");
        uriBuilder.appendQueryParameter("APPID", "01d2ff8db13d01d0b61195a14144992e");

        return new WeatherLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished (Loader < List < Weather >> loader, List < Weather > weathers){
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

           mEmptyStateView.setText(location);

            //    mAdapter.clear();

            if (weathers != null && !weathers.isEmpty()) {

                graph = (GraphView) findViewById(R.id.graph);
                series = new LineGraphSeries<DataPoint>();

                for(int i=0;i<30;i++) {
                    currentWeather = weathers.get(i);
                    date = currentWeather.getmDate();
                    temp = currentWeather.getmTemp();
                    series.appendData(new DataPoint(i, temp), true, 30);
                }
                graph.addSeries(series);
            }
        }


        @Override
        public void onLoaderReset (Loader < List < Weather >> loader) {

        }

    }
