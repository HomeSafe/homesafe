package cse403.homesafe.Data;

/**
 * Created by Vivek on 4/28/15, modified by Ethan
 *
 * Location class represents a geo location.
 * This class provides functionality for modifying destination.
 *
 */
public class Location {
    private String locID;
    private String lng;
    private String lat;

    // Representation invariant:
    // location must not be null;

    /**
     * Constructor
     * @param locID id number for the location
     * @param lng   longitude of location
     * @param lat   latitude of location
     * @throws IllegalArgumentException if locID, lng or lat is null
     */
    public Location(String locID, String lng, String lat) {
        this.locID = locID;
        this.lng = lng;
        this.lat = lat;
    }

    /**
     * modify location with passed-in latitude and longitude
     * @param lat   new latitude
     * @param lng   new longitude
     * @return  true if success, false if failed
     * @throws IllegalArgumentException if lng or lat is null
     */
    public boolean editLocation(String lat, String lng) {
        return true;
    }

}
