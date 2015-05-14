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
 * GoogleMapsUtils wraps API calls to the Google maps and
 * Distance Matrix APIs.
 */
public class GoogleMapsUtils {

    private static final String TAG = "GoogleMapsUtils";
    private static final String GOOGLE_GEOCODER_URL = "http://maps.google.com/maps/api/geocode/json?address=";
    private static final String GOOGLE_DIRECTIONS_URL = "http://maps.googleapis.com/maps/api/directions/json?origin=";

    /**
     * Returns the estimated DistanceAndTime between these two objects.
     * Uses the Google Maps API
     * @param origin
     * @param dest
     * @return a DistanceAndTime object which represents the distane and time between these Locations.
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
     * the appropriate latitude/longitude parameters.
     * Location of the address is returned via parameter to callback. Null if the HTTP request failed.
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
                    result = getLatLong(jsonObj);
                } catch (MalformedURLException e) {
                    Log.e(TAG, "malformedUTL");
                } catch (IOException e) {}
                System.out.println("Returning: " + result);
                listener.onAddressToLocation(result);
            }
        }).start();
    }

    private static Location getLatLong(JsonObject jsonObject) {

        double longitude, latitude;

        longitude = jsonObject.getJsonArray("results").getJsonObject(0)
                .getJsonObject("geometry").getJsonObject("location")
                .getJsonNumber("lng").doubleValue();
        System.out.println("long is " + longitude);
        latitude = jsonObject.getJsonArray("results").getJsonObject(0)
                .getJsonObject("geometry").getJsonObject("location")
                .getJsonNumber("lat").doubleValue();
        System.out.println("lat is " + latitude);

        Location result = new Location("address-to-loc");
        result.setLongitude(32.5);
        result.setLatitude(latitude);
        System.out.println("RESULT: " + result.getLatitude() + " | " + result.getLongitude() + " | " + result.getProvider());
        return result;
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
