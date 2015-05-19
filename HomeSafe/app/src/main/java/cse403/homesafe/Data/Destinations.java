package cse403.homesafe.Data;

import android.location.Location;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * A Destinations object represents the collection
 * of all Destinations of the User.
 *
 * This class provides functionality for adding and removing Destinations.
 *
 * This is a Singleton class -- access by calling Destinations.getInstance()
 *
 * TODO: Switch to android ContentProvider
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

    /**
     *  Returns the instance of this object.
     * @return
     */
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
     * @param did   the id of destination to remove
     * @return      True if removing Location is successful, false otherwise
     */
    public boolean removeDestination(long did) {
        int index = 0;
        for (Destination e : destinations) {
            if (e.getDid() == did) {
                break;
            }
            index++;
        }
        destinations.remove(index);
        return true;
    }

    /*
     * clear all the destinations in the list
     */
    public void clearDestinations() {
        destinations.clear();
    }


    /**
     * Returns a List<Destination> of all Destinations in this object.
     * Do not modify the returned list
     * @return List of all Destinations in this Destinations object.
     */
    public List<Destination> getDestinations() {
        return destinations;
    }

    public int getSize() {
        return destinations.size();
    }
}
