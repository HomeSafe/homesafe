package cse403.homesafe.Util;

import android.location.Location;
import android.text.format.Time;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonReader;
import javax.json.Json;
import javax.json.JsonObject;


/**
 * GoogleMapsUtils wraps API calls to the Google Maps Geocode
 * and Directions APIs.
 */
public class GoogleMapsUtils {

    private static final String TAG = "GoogleMapsUtils";
    private static final String GOOGLE_GEOCODER_URL = "http://maps.google.com/maps/api/geocode/json?address=";
    private static final String GOOGLE_DIRECTIONS_URL = "http://maps.googleapis.com/maps/api/directions/json?origin=";

    /**
     * Uses the Google Maps API to calculate the duration of a trip on foot
     * and distance of path. Returns the resulting DistanceAndTime through
     * a callback to the specified GoogleMapsUtilsCallback listener. If the
     * Google Maps query returns no results, callback is invoked with null Location.
     * @param origin Starting Location
     * @param dest Ending Location
     */
    public static void getDistanceAndTime(final Location origin, final Location dest, final GoogleMapsUtilsCallback listener) {
        new Thread(new Runnable() {
            public void run() {
                try {

                    URL url = new URL(GOOGLE_DIRECTIONS_URL + origin.getLatitude() + "," + origin.getLongitude()
                            + "&destination=" + dest.getLatitude() + "," + dest.getLongitude() + "&mode=walking");
                    URLConnection urlConnection = url.openConnection();
                    InputStream inputStream = urlConnection.getInputStream();
                    JsonReader reader = Json.createReader(inputStream);
                    JsonObject jsonObj = reader.readObject();

                    if (!isValid(jsonObj))  // Check if the query had results
                        listener.onGetDistanceAndTime(null);

                    double distance = getDistance(jsonObj);
                    double time = getTime(jsonObj);

                    DistanceAndTime result = new DistanceAndTime(origin, dest, distance, time);
                    listener.onGetDistanceAndTime(result);

                } catch (MalformedURLException e) {
                    Log.e(TAG, "");
                } catch (IOException e) {
                    Log.e(TAG, "ioexception");
                }
            }
        }
        ).start();
    }

    /**
     * Sends the specified address to Google Directions API and returns a Location object with
     * the appropriate latitude/longitude parameters through a callback to the specified
     * GoogleMapsUtilsCallback listener. Null if the HTTP request fails or if the query did
     * not return any results.
     * @param address Address from which Location will be derived.
     */
    public static void addressToLocation(final String address, final GoogleMapsUtilsCallback listener) {
        new Thread(new Runnable() {
            public void run() {
                StringBuilder jsonString = new StringBuilder();
                Location result = null;
                try {
                    URL url = new URL(GOOGLE_GEOCODER_URL + address);
                    URLConnection urlConnection = url.openConnection();
                    InputStream inputStream = urlConnection.getInputStream();
                    JsonReader reader = Json.createReader(inputStream);
                    JsonObject jsonObj = reader.readObject();

                    if (!isValid(jsonObj))  // Check if the query had results
                        listener.onAddressToLocation(null);

                    result = getLatLong(jsonObj);

                    listener.onAddressToLocation(result);

                } catch (MalformedURLException e) {
                    Log.e(TAG, "malformedUTL");
                } catch (IOException e) {}
            }
        }).start();
    }

    // Parses and extracts the Latitude and Longitude from the passed
    // in JsonObject and returns a new Location object with those parameters.
    private static Location getLatLong(JsonObject jsonObject) {

        double longitude, latitude;

        longitude = jsonObject.getJsonArray("results").getJsonObject(0)
                .getJsonObject("geometry").getJsonObject("location")
                .getJsonNumber("lng").doubleValue();

        latitude = jsonObject.getJsonArray("results").getJsonObject(0)
                .getJsonObject("geometry").getJsonObject("location")
                .getJsonNumber("lat").doubleValue();

        Location result = new Location("address-to-loc");
        result.setLongitude(longitude);
        result.setLatitude(latitude);

        return result;
    }

    // Returns true if the query stored in the specified JsonObject yielded results
    private static boolean isValid(JsonObject jsonObject) {
        return jsonObject.getJsonString("status").getString().equals("OK");
    }

    // Returns distance in meters
    private static double getDistance(JsonObject jsonObject) {

        double distance;

        distance = jsonObject.getJsonArray("routes").getJsonObject(0)
                .getJsonArray("legs").getJsonObject(0).getJsonObject("distance")
                .getJsonNumber("value").doubleValue();

        return distance;
    }

    // Returns time in seconds
    private static double getTime(JsonObject jsonObject) {

        double time;

        time = jsonObject.getJsonArray("routes").getJsonObject(0)
                .getJsonArray("legs").getJsonObject(0).getJsonObject("duration")
                .getJsonNumber("value").doubleValue();

        return time;
    }


}
