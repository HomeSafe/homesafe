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
    private String address;
    private long did;

    // ****** Representation Invariant
    // name must not be null

    public Destination (String name, String address, Location location) {
        this.name = name;
        this.address = address;
        this.location = location;
    }

    /**
     * Returns address of this Destination
     * @return  the address of this destination
     */
    public String getAddress() {
        return this.address;
    }

    /**
     * Assigns the passed in String as the address of destination
     * @param address   the new address of destination
     */
    public void setAddress(String address) {
        this.address = address;
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
     * @param location new Location
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Assigns the passed in did as the id of this Destination
     * @param did   the id of this Destination
     */
    public void setDid(long did) {
        this.did = did;
    }

    /**
     * Returns the id of this Destination
     * @return  the id of this Destination
     */
    public long getDid() {
        return this.did;
    }
}
