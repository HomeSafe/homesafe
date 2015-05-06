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

    // ****** Representation Invariant
    // name must not be null
    // tier must not be null
    // One of the following must not be null
    //      name, email, phone number

    /**
     * Constructs a new Contact object with the passed-in name, email, phoneNumber, and tier
     * @param name The name of the new Contact
     * @param email The email address of the new contact.
     * @param phoneNumber The phone number of the new Contact
     * @param tier The tier of the new Contact (ONE, TWO, THREE)
     */
    public Contact(String name, String email, String phoneNumber, Contacts.Tier tier) {
        this.tier = tier;
        this.name = name;
        this.email = email;
        this.phoneNumber = email;

        cid = (name + phoneNumber).hashCode();
    }

    /**
     * Constructs a new Contact object with the passed-in name, phoneNumber, and tier
     * @param name The name of the new Contact
     * @param phoneNumber The phone number of the new Contact
     * @param tier The tier of the new Contact (ONE, TWO, THREE)
     */
    public Contact(String name, String phoneNumber, Contacts.Tier tier) {
        this(name, "", phoneNumber, tier);
    }

    /**
     * Constructs a new Contact object with the passed-in name
     * @param name The name of the new Contact
     */
    public Contact(String name) {
        this(name, "", "", Contacts.Tier.ONE);
    }

    /**
     * Returns the Contact ID of this contact
     * @return This contact's Contact ID
     */
    public int getCid() {
        return cid;
    }

    /**
     * Sets the Contact ID of this contact
     * @param cid the new integer Contact ID to use for this contact
     */
    public void setCid(int cid) {
        this.cid = cid;
    }

    /**
     * Returns the tier of this Contact object.
     * @return a Tier enum representing the Tier of this contact (ONE, TWO, or THREE)
     */
    public Contacts.Tier getTier() {
        return tier;
    }


    /**
     * Sets the Tier of this object to the passed- in tier.
     * @param tier the Tier to assign this Contact to.
     */
    public void setTier(Contacts.Tier tier) {
        this.tier = tier;
    }

    /**
     * Returns the name of this Contact as a String.
     * @return the name of this Contact.
     */
    public String getName() {
        return name;
    }

    /**
     * Assigns the passed in string as the name of this Contact.
     * @param name the new Name of this Contact.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the email address of this Contact as a String.
     * @return the email address of this Contact.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Assigns the passed-in String as the email address of this contact.
     * @param email the new email address of this Contact.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the phone number of this Contact as a String.
     * @return the phone number of this Contact.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Assigns the passed-in String as the phone-number of this contact.
     * The Phone number should have the format XXXXXXXXXX
     * The Phone number should not have any dashes or non-numeric characters.
     * @param phoneNumber The new phone number of this Contact.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        String res =  "[" + name + " (" + cid + ") Tier: " + tier;
        if(email != null && email.length() != 0) {
            res += " | " + email;
        }
        if(phoneNumber != null && phoneNumber.length() != 0) {
            res += " | " + phoneNumber;
        }
        res += "]";
        return res;
    }
}
