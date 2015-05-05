package cse403.homesafe.Messaging;

import java.util.List;

import cse403.homesafe.Data.Contact;
import cse403.homesafe.Data.Contacts;
import cse403.homesafe.Data.Location;  // May substitute with android.location.Location

/**
 * Handles the sending of messages by the retrieving appropriate contacts and sending
 * corresponding messages.
 */
public class Messenger {

    private static SMS sms = SMS.getInstance();  // For sending text messages
    private static Email email = Email.getInstance();  // For sending email

    /**
     * Sends notifications to the specified tier of Contacts with the specified
     * last known user Location.
     * @param tier Tier of contacts to be notified
     * @param location  Last known user location
     */
    public static void sendNotifications (Contacts.Tier tier, Location location) {
        Contacts contacts = new Contacts();
        List<Contact> contactsNotified = contacts.getContactsInTier(tier);
        for (Contact c : contactsNotified) {
            if (!c.getEmail().equals(""))  // TODO (Alex): Change to '!= null' if Contact changes behavior
                email.sendMessage(c, location, null);  // Change customMessage parameter to appropriate in SharedPreferences
            if (!c.getPhoneNumber().equals(""))  // TODO (Alex): Change to '!= null' if Contact changes behavior
                sms.sendMessage(c, location, null);
        }
    }
}
