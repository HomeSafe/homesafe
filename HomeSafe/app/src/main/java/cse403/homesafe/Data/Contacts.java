package cse403.homesafe.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * A ContactsActivity object represents the collection
 * of all contacts of the User in a particular tier.
 *
 * This class provides functionality for adding and removing contacts.
 */
public class Contacts{
    private List<Contact> contacts;
    private static Contacts instance = new Contacts();

    /**
     * Represents the 3 possible tiers contacts can fall under.
     */
    public enum Tier {
        ONE,
        TWO,
        THREE
    }

    // ***** Representation invariant:
    // Contacts must not be null.
    // No two contacts in the LinkedList contacts may have the same contact id.
    // instance is not null.

    /**
     * Private constructor for the Singleton
     */
    private Contacts(){
        contacts = new LinkedList<Contact>();
    }

    public static Contacts getInstance() {
        if (instance == null) {
            instance = new Contacts();
        }
        return instance;
    }

    /**
     * Adds the passed-in contact as a new member of this ContactsActivity object.
     * @param c the contact to be added
     * @return true on success
     */
    public boolean addContact(Contact c, Tier tier){
        contacts.add(c);
        return true;
    }

    /**
     * Removes the contact with the passed-in cid
     * from this object.
     *
     * @param cid the cid of the contact to be removed.
     * @return true if a contact with the passed-in cid
     * existed in this ContactsActivity object, and was removed.
     * false otherwise.
     */
    public boolean removeContact(String cid) {
        return false;
    }

    /**
     * Returns a LinkedList of all Contact objects which are of the passed-in tier.
     *
     * @param tier The desired Tier of the returned objects
     * @return A LinkedList<Contact> which contains all Contact objects with the given Tier
     */
    public List<Contact> getContactsInTier(Contacts.Tier tier) {
        if (tier != Tier.ONE && tier != Tier.TWO && tier != Tier.THREE) {
            return null;
        }
        List<Contact> result = new LinkedList<Contact>();
        for(Contact c : contacts) {
            if(c.getTier() == tier) {
                result.add(c);
            }
        }
        return result;
    }


}
