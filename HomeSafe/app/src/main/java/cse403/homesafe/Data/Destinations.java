package cse403.homesafe.Data;

import java.util.LinkedList;

/**
 * A Destinations object represents the collection
 * of all Location of the User.
 *
 * This class provides functionality for adding and removing Location.
 */
public class Destinations {
    private LinkedList<Location> destinations;

    // Representation invariant:
    // destinations is not null

    /**
     * Constructor
     */
    public Destinations() {
        destinations = new LinkedList<Location>();
    }

    /**
     * Adds a Location to Destinations
     * @param loc   Location to add
     * @return      True if adding Location is successful, false otherwise
     */
    public boolean addDestination(Location loc) {
        return false;
    }

    /**
     * Removes a Location from Destinations
     * @param loc   Location to remove
     * @return      True if removing Location is successful, false otherwise
     */
    public boolean removeDestination(Location loc) {
        return false;
    }
}
