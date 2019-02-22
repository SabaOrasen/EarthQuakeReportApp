/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import android.support.v4.content.AsyncTaskLoader;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>>{
    private static final int EARTHQUAKE_LOADER_ID = 1;
    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    public static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";
    private ArrayList<Earthquake> mEarthquakeArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        TextView mEmptyTextview = (TextView) findViewById(R.id.emptyView);

        // Create a new adapter that takes an empty list of earthquakes as input
        //EarthquakeAdapter adapter = new EarthquakeAdapter(this,mEarthquakeArray);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setEmptyView(mEmptyTextview);
        // earthquakeListView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int i, Bundle bundle) {
        return new EarthquakeLoader(EarthquakeActivity.this, USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {
        // Clear the adapter of previous earthquake data
        mEarthquakeArray.clear();
        TextView mEmptyTextview = (TextView) findViewById(R.id.emptyView);
       // ProgressBar mProgressBar = (ProgressBar)findViewById(R.id.progressbar);
       // mProgressBar.setVisibility(View.GONE);

        mEmptyTextview.setText("No Earthquakes are found!");

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (earthquakes != null && !earthquakes.isEmpty()) {
            mEarthquakeArray.addAll(earthquakes);
            EarthquakeAdapter adapter = new EarthquakeAdapter(EarthquakeActivity.this,mEarthquakeArray);

            // Set the adapter on the {@link ListView}
            // so the list can be populated in the user interface
            ListView earthquakeListView = (ListView) findViewById(R.id.list);

            earthquakeListView.setAdapter(adapter);

        }

        Toast.makeText(EarthquakeActivity.this,"Last 10 recent earthquakes!",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        mEarthquakeArray.clear();
    }

    /*
    private class QuakeAsyncTask extends AsyncTask<String,Void,List<Earthquake>>{
        @Override
            protected List<Earthquake> doInBackground(String... strings) {
                List<Earthquake> earthquakes = QueryUtils.fetchEarthquakeData(strings[0]);
                Log.v(LOG_TAG,earthquakes.toArray().toString());
                return earthquakes;
        }

        @Override
        protected void onPostExecute(List<Earthquake> earthquakes) {
            // Clear the adapter of previous earthquake data
            mEarthquakeArray.clear();

            // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
            // data set. This will trigger the ListView to update.
            if (earthquakes != null && !earthquakes.isEmpty()) {
                mEarthquakeArray.addAll(earthquakes);
                EarthquakeAdapter adapter = new EarthquakeAdapter(EarthquakeActivity.this,mEarthquakeArray);

                // Set the adapter on the {@link ListView}
                // so the list can be populated in the user interface
                ListView earthquakeListView = (ListView) findViewById(R.id.list);

                earthquakeListView.setAdapter(adapter);

            }

            Toast.makeText(EarthquakeActivity.this,"Last 10 recent earthquakes!",Toast.LENGTH_LONG).show();
        }

    }*/
}
