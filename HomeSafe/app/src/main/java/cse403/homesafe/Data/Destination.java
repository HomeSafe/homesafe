package cse403.homesafe.Data;

import android.location.Location;
import android.util.Log;

import cse403.homesafe.Util.GoogleMapsUtils;
import cse403.homesafe.Util.GoogleMapsUtilsCallback;

/**
 * A Destination object represents a single destination created by user.
 * This class provides functionality to create and edit a destination's
 * name and location.
 *
 * When passing in an address, computes the actual geographic location of
 * the address. If an address is unable to be converted to a location
 * either due to a bad address or lack of network connection,
 * goes into a state where getLocation returns null.
 */

public class Destination implements GoogleMapsUtilsCallback {
    private static final String TAG = "Destination";
    private Location location;
    private String name;
    private String address;
    private long did;
    STATE state;

    private enum STATE {
        NO_LOCATION,
        READY,
        ERROR
    }

    // ****** Representation Invariant
    // name must not be null
    // If state is NO_LOCATION: location should be null;
    // if state is READY: location must be a valid location

    /**
     * Constructs a new Destination object with a name and address.
     * Dynamically converts the address to geographical coordinates.
     * While this conversion is in progress, this Destination's state is
     * NO_LOCATION
     *
     * @param name name of the location
     * @param address address of the location
     */
    public Destination (String name, String address) {
        this.name = name;
        this.address = address;

        state = STATE.NO_LOCATION;

        // addressToLocation converts the address to a Location
        // asynchronously.
        synchronized(this) {
            GoogleMapsUtils.addressToLocation(address, this);
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Constructs a new place-holder location in the drop down
     * so that no real address validation is required
     * @param name name of the location
     */
    public Destination (String name, long did){
        this.name = name;
        this.did = did;
    }
    /**
     * Constructs a new Destination object with a name, address, and location
     * @param name name of the location
     * @param address address of the location
     * @param location Location object of the address
     */
    public Destination (String name, String address, Location location) {
        this.name = name;
        this.address = address;
        this.location = location;
        state = STATE.READY;
    }

    /**
     * Returns true if this Destination is in a ready state.
     * Returns false otherwise.
     * @return true if Destination is ready.
     */
    public boolean isReady() {
        return this.state == STATE.READY;
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
        state = STATE.NO_LOCATION;
        GoogleMapsUtils.addressToLocation(address, this);
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
        if(state == STATE.READY) {
            return location;
        }
        return null;
    }

    /**
     * Assigns the passed in Location as the location of this Destination
     * @param location new Location
     */
    public void setLocation(Location location) {
        // We have location, so we are ready.
        state = STATE.READY;
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



    @Override
    public void onGetDistanceAndTime(Object obj) {
        // Empty

    }

    // If the passed-in object is a Location,
    // switches state to READY and notifies all
    // listeners.
    @Override
    public void onAddressToLocation(Object obj) {
        synchronized (this) {
            if (obj instanceof Location) {
                setLocation((Location) obj);
            } else {
                state = STATE.ERROR;
                Log.e(TAG, "Calculate location error");
            }
            notifyAll();
        }
    }
}
