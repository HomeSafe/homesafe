package cse403.homesafe.Data;

import android.location.Location;

/**
 * A Destination object represents a single destination created by user.
 * This class provides functionality to create and edit a destination's
 * name and location.
 *
 */

public class Destination {
    private Location location;
    private String name;

    // ****** Representation Invariant
    // name must not be null
    // location must not be null

    public Destination (Location location, String name) {
        this.location = location;
        this.name = name;
    }

    /**
     * Returns name of this Destination
     * @return  the name of this Destination
     */
    public String getName() {
        return name;
    }

    /**
     * Assigns the passed in string as the name of this Destination
     * @param name  new name of this destination
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns Location of this Destination
     * @return  Location of this Destination
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Assigns the passed in Location as the location of this Destination
     * @param location
     */
    public void setLocation(Location location) {
        this.location = location;
    }
}
