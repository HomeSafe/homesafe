package cse403.homesafe.Util;

import android.location.Location;

/**
 * GoogleMapsUtils wraps API calls to the Google maps and
 * Distance Matrix APIs.
 */
public class GoogleMapsUtils {

    /**
     * Prevent this from being instantiated. This is a
     * Class of static methods.
     */
    private GoogleMapsUtils() {
        // nothing
    }

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



}
