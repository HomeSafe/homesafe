package cse403.homesafe;

import cse403.homesafe.Data.Location;
import cse403.homesafe.Messaging.Messenger;

/**
 * Holds methods to perform various activities during the trip.
 */
public class Trip {

    private int tier;
    private Location startLocation;
    private Location endLocation;
    private HSTimer timer;
    private boolean arrived;
    private Messenger messenger;

    /**
     * Constructor
     * @param endLocation   Destination location
     * @param delay         Estimated time to arrive at destination
     */
    public Trip(Location endLocation, long delay) {

    }

    /**
     * Start trip using existing settings
     */
    public void startTrip() {

    }

    /**
     * End trip, program prompt for password to end trip.
     * @return  True if successfully ended trip
     */
    public boolean endTrip() {
        return false;
    }

    /**
     * Callback action for timer when time is up. Prompt for extend or end trip
     */
    public void timerEndAction() {

    }

    /**
     * Extend time to arrive at destination
     */
    public void extendTime() {

    }

    /**
     * Actions to execute when user ignores timer. Notify contacts.
     */
    public void userIgnoredTimerAction() {

    }

    /**
     * Prompt user for password.
     * @return password entered by user
     */
    private String promptForPassword() {
        return null;
    }

    /**
     * Verify password given against stored passwords
     * @param password  Password entered by user
     * @return          True if given password matches stored password, false otherwise
     */
    private boolean verifyPassword(String password) {
        return false;
    }

    /**
     * Prompt user to input a time to extend trip for.
     * @return  Time to extend trip for in milliseconds
     */
    private long promptForExtendTime() {
        return 0;
    }

}
