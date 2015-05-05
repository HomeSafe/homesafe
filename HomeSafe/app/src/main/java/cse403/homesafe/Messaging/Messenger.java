package cse403.homesafe.Messaging;

import cse403.homesafe.Data.Location;  // May substitute with android.location.Location

/**
 * Handles the sending of messages by the retrieving appropriate contacts and sending
 * corresponding messages.
 */
public class Messenger {

    private static SMS sms = SMS.getInstance();  // For sending text messages
    private static Email email = Email.getInstance();  // For sending email

    /**
     * Sends notifications to the specified tier of ContactsActivity
     * @param tier      Tier of contacts to be notified
     * @param location  Last known user location
     */
    public static void sendNotifications (int tier, Location location) {

    }
}
