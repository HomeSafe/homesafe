package cse403.homesafe.Data;

import android.location.Location;

import java.util.LinkedList;
import java.util.List;

/**
 * A Destinations object represents the collection
 * of all Location of the User.
 *
 * This class provides functionality for adding and removing Location.
 */
public class Destinations {
    private List<Location> destinations;
    private static Destinations instance = new Destinations();

    // Representation invariant:
    // destinations is not null
    // instance is not null.

    /**
     * private constructor for singleton.
     */
    private Destinations() {
        destinations = new LinkedList<Location>();
    }

    public static Destinations getInstance() {
        if (instance == null) {
            instance = new Destinations();
        }
        return instance;
    }

    /**
     * Adds a Location to Destinations
     * @param loc   Location to add
     * @return      True if adding Location is successful, false otherwise
     */
    public boolean addDestination(Location loc) {
        return destinations.add(loc);
    }

    /**
     * Removes a Location from Destinations
     * @param loc   Location to remove
     * @return      True if removing Location is successful, false otherwise
     */
    public boolean removeDestination(Location loc) {
        return destinations.remove(loc);
    }

    public List<Location> getDestinations() {
        return destinations;
    }

}
