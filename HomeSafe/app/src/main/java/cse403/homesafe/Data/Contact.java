package cse403.homesafe.Data;

/**
 * A Contact object represents a single contact of the user.
 * This class provides functionality to create and edit a contact's
 * Name, phone number, and email address.
 *
 * Each contact has a tier associated with it, representing
 * the alert level at which this Contact should be notified.
 */
public class Contact {
    String cid;
    int tier;
    String name;
    String email;
    String phoneNumber;


    /**
     * Replaces the contact's name, email address, and phone number with the passed
     * in values.
     *
     * @return true if successful
     */
    public boolean editContact(String name, String email, String phoneNumber) {
        return false;
    }

    /**
     * replaces this contact's tier with the passed in int tier
     * @param tier the tier to replace with
     * @return true if successful
     */
    public boolean switchTier(int tier) {
        return true;
    }
}
