package cse403.homesafe.Data;

import android.location.Location;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * A Destinations object represents the collection
 * of all Location of the User.
 *
 * This class provides functionality for adding and removing Location.
 */
public class Destinations {
    private List<Destination> destinations;
    private static Destinations instance = new Destinations();

    // Representation invariant:
    // destinations is not null
    // instance is not null.

    /**
     * private constructor for singleton.
     */
    private Destinations() {
        destinations = new ArrayList<Destination>();
    }

    public static Destinations getInstance() {
        if (instance == null) {
            instance = new Destinations();
        }
        return instance;
    }

    /**
     * Adds a Destination to Destinations
     * @param destination   Destination to add
     * @return      True if adding Location is successful, false otherwise
     */
    public boolean addDestination(Destination destination) {
        return destinations.add(destination);
    }

    /**
     * Removes a Destination from Destinations
     * @param destination   Destination to remove
     * @return      True if removing Location is successful, false otherwise
     */
    public boolean removeDestination(Destination destination) {
        return destinations.remove(destination);
    }

    public List<Destination> getDestinations() {
        return destinations;
    }

}
