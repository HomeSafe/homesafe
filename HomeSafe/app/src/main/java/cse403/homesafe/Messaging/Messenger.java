package cse403.homesafe.Messaging;

import java.util.List;

import cse403.homesafe.Data.Contact;
import cse403.homesafe.Data.Contacts;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;  // May substitute with android.location.Location
import android.preference.PreferenceManager;

/**
 * Handles the sending of messages by the retrieving appropriate contacts and sending
 * corresponding messages.
 */
public class Messenger {

    /**
     * Public enum for specifying the type of message to be sent out.
     * DANGER specifies that the user did not check in with the app.
     * HOMESAFE specifies that the user arrived safely.
     */
    public enum MessageType {
        DANGER,
        HOMESAFE
    }

    private static SMS sms = SMS.getInstance();  // For sending text messages
    private static Email email = Email.getInstance();  // For sending email

    /**
     * Sends notifications to the specified tier of Contacts with the specified
     * last known user Location.
     * @param tier Tier of contacts to be notified
     * @param location  Last known user location
     */
    public static void sendNotifications (Contacts.Tier tier, Location location, Context context, MessageType type) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String customMessage = preferences.getString("customMessage", "");

        customMessage = customMessage.trim();

        Contacts contacts = Contacts.getInstance();

        // grab cumulative list of contacts
        List<Contact> contactsNotified = contacts.getContactsInTier(Contacts.Tier.ONE);
        if(tier == Contacts.Tier.TWO || tier == Contacts.Tier.THREE) {
            contactsNotified.addAll(contacts.getContactsInTier(Contacts.Tier.TWO));
        }
        if(tier == Contacts.Tier.THREE) {
            contactsNotified.addAll(contacts.getContactsInTier(Contacts.Tier.THREE));
        }
        for (Contact c : contactsNotified) {
            if (c.getEmail() != null)
                email.sendMessage(c, location, customMessage, context, type);  // Change customMessage parameter to appropriate in SharedPreferences
            if (c.getPhoneNumber() != null)
                sms.sendMessage(c, location, customMessage, context, type);
        }
    }
}
