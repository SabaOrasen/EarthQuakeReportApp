package com.example.android.quakereport;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.quakereport.EarthquakeActivity.LOG_TAG;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {
    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }
    public static List<Earthquake> fetchEarthquakeData(String requestUrl) {
       URL url = createURL(requestUrl);
       String response = null;
       try {
         response =  makeHTTPRequest(url);
       }catch (IOException e){
           Log.e(LOG_TAG, "Problem making the HTTP request.", e);
       }
        List<Earthquake> earthquakes = extractEarthquakes(response);
       return earthquakes;
    }

    private static String makeHTTPRequest(URL requestUrl) throws IOException {
        String jsonResponse = null;
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection)requestUrl.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.connect();
                if (urlConnection.getResponseCode()== HttpURLConnection.HTTP_OK){
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                }else{
                    Log.e(LOG_TAG,"ERROR Response Code " + urlConnection.getResponseCode());
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "ERROR Http connection went wrong: " + e);
            }finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if ( inputStream!= null) {
                    // Closing the input stream could throw an IOException, which is why
                    // the makeHttpRequest(URL url) method signature specifies than an IOException
                    // could be thrown.
                    inputStream.close();
                }
            }

        return jsonResponse;
    }


    private static String readFromStream(InputStream inputStream){
        StringBuilder jsonData = new StringBuilder();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            try {
                String line = bufferedReader.readLine();
                while (line != null){
                    jsonData.append(line);
                    line = bufferedReader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        return jsonData.toString();
    }

    private static URL createURL(String requestURL) {
        URL url = null;
        try {
            url = new URL(requestURL);
        }catch (MalformedURLException e){
            Log.e(LOG_TAG,"Error with creating URL " + e);
        }
        return url;
    }



    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing a JSON response.
     */
    private static List<Earthquake> extractEarthquakes(String JSONResponse) {

        // Create an empty ArrayList that we can start adding earthquakes to
        List<Earthquake> earthquakes = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            JSONObject root = new JSONObject(JSONResponse);
            JSONArray features = root.getJSONArray("features");
            for(int i = 0; i < features.length(); i++){
                JSONObject quakes = (JSONObject) features.get(i);
                JSONObject properties = (JSONObject) quakes.getJSONObject("properties");
                double mag = properties.getDouble("mag");
                String place = properties.getString("place");
                Long date = properties.getLong("time");
                String url = properties.getString("url");

                // Create a new {@link Earthquake} object with the magnitude, location, time,
                // and url from the JSON response.
                Earthquake earthquake = new Earthquake(mag, place, date, url);

                earthquakes.add(earthquake);
            }

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }

}