package cse403.homesafe.Data;

import java.util.LinkedList;

/**
 * A Contacts object represents the collection
 * of all contacts of the User in a particular tier.
 *
 * This class provides functionality for adding and removing contacts.
 */
public class Destinations {
    private LinkedList<Location> destinations;

    // Representation invariant:
    // destinations is not null

    public Destinations() {
        destinations = new LinkedList<Location>();
    }

    public boolean addDestination(Location loc) {
        return false;
    }

    public boolean removeDestination(Location loc) {
        return false;
    }
}
