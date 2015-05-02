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
    private int cid;
    private Contacts.Tier tier;
    private String name;
    private String email;
    private String phoneNumber;

    public Contact(String name, String email, String phoneNumber, Contacts.Tier tier) {
        this.tier = tier;
        this.name = name;
        this.email = email;
        this.phoneNumber = email;

        cid = (name + phoneNumber).hashCode();
    }

    public Contact(String name, String phoneNumber, Contacts.Tier tier) {
        this(name, "", phoneNumber, tier);
    }

    public Contact(String name) {
        this(name, "", "", Contacts.Tier.ONE);
    }
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
     * Replaces this contact's tier with the passed in tier.
     * @param newTier the tier to replace with
     */
    public void switchTier(Contacts.Tier newTier) {
        this.tier = newTier;
    }
}
