package cse403.homesafe.Util;

import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.json.JsonArray;
import javax.json.JsonReader;
import javax.json.Json;
import javax.json.JsonObject;


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
        Location result = null;
        try {
            URL url = new URL("http://maps.google.com/maps/api/geocode/json?address=" + address);
            URLConnection urlConnection = url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            int b;
            JsonReader reader = Json.createReader(inputStream);
            JsonObject jsonObj = reader.readObject();
            System.out.println("Json:\n" + jsonObj.toString());
            result = getLatLong(jsonObj);
        } catch (MalformedURLException e) {
            Log.e(TAG, "malformedUTL");
        } catch (IOException e) {}
        System.out.println("Returning: " + result);
        return result;
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


}
