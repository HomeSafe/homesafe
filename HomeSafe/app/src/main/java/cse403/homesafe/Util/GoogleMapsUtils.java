package cse403.homesafe.Util;

import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * GoogleMapsUtils wraps API calls to the Google maps and
 * Distance Matrix APIs.
 */
public class GoogleMapsUtils {

    private static final String TAG = "GoogleMapsUtils";
    private static final String API_KEY = "AIzaSyCwJ1fLRapClAn9-vkr-UOovPqHhuEaxdo";

    /**
     * Prevent this from being instantiated. This is a
     * Class of static methods.
     */
//    private GoogleMapsUtils() {
//        // nothing
//    }

    /**
     * Returns the estimated DistanceAndTime between these two objects.
     * Uses the Google Maps API
     * @param loc1
     * @param loc2
     * @return a DistandAndTime object which represents the distane and time between these Locations.
     */
    public static DistanceAndTime getDistanceAndTime(Location loc1, Location loc2) {
        // insert API call here
        return null;
    }

    /**
     * Sends the specified address to Google Directions API and returns a Location object with
     * the appropriate latitude/longitude parameters.
     * @param address Address from which Location will be derived.
     * @return Location of the address. Null if the HTTP request failed.
     */
    public static Location addressToLocation(String address) {

        StringBuilder jsonString = new StringBuilder();
        try {
            URL url = new URL("http://maps.google.com/maps/api/geocode/json?address=" + address);
            URLConnection urlConnection = url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            int b;
            while ((b = inputStream.read()) != -1) {
                jsonString.append((char) b);
            }
            try {
                System.out.println(jsonString.toString());
                JSONObject httpJsonResult = new JSONObject(jsonString.toString());
                return getLatLong(httpJsonResult);
            } catch (JSONException e) {
                Log.e(TAG, "JSONObject wasn't able to be made");
            }
        } catch (MalformedURLException e) {
            Log.e(TAG, "");
        } catch (IOException e) {}

        return null;
    }

    private static Location getLatLong(JSONObject jsonObject) {

        double longitude, latitude;
        try {

            longitude = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lng");

            latitude = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");

            Location result = new Location("");
            result.setLongitude(longitude);
            result.setLatitude(latitude);
            return result;
        } catch (JSONException e) {
            Log.e(TAG, "JSON exception while retrieving lat/long from JSON object");
            return null;
        }
    }


}
