package cse403.homesafe.Data;

import java.util.LinkedList;

/**
 * A ContactsActivity object represents the collection
 * of all contacts of the User in a particular tier.
 *
 * This class provides functionality for adding and removing contacts.
 */
public class Contacts {
    private LinkedList<Contact> contacts;

    /**
     * Represents the 3 possible tiers contacts can fall under.
     */
    public enum Tier {
        ONE,
        TWO,
        THREE
    }

    // Representation invariant:
    // contacts must not be null;


    public Contacts(){
        contacts = new LinkedList<Contact>();
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


}
