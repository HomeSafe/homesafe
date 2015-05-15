package cse403.homesafe.Messaging;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;

import cse403.homesafe.Data.Contact;

/**
 * Abstraction that serves as the interface for sending text messages
 * through Android's native messaging service.
 */
public class SMS implements Message {

    // Instance of SMS
    private static final SMS _instance = new SMS();

    // Private singleton constructor
    private SMS() { }

    /**
     * Get singleton instance of SMS
     * @return Default instance of SMS
     */
    public static SMS getInstance() {
        return _instance;
    }

    /**
     * Sends SMS message to recipient through Android's SmsManager
     * @param recipient Recipient of the intended message
     * @param location Last location of user
     * @param customMessage Customized message to be sent
     */
    @Override
    public void sendMessage(Contact recipient, android.location.Location location, String customMessage, Context context, Messenger.MessageType type) {
        String message = "";

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        
        String userFirstName = preferences.getString("firstName", null);
        String userLastName = preferences.getString("lastName", null);

        userFirstName = (userFirstName == null) ? "" : userFirstName;
        userLastName = (userLastName == null) ? "" : userLastName;

        message = userFirstName + userLastName + " may need your help!\n";
        message += userFirstName + " was using HomeSafe, a walking safety app.\n\n They were"
                + " using the app to get to a destination, but did not check in with the app."
                + " As a result, this is an automated email being sent to all of " + userFirstName
                + "'s contacts. Their last know location is (" + location.getLatitude() + ", "
                + location.getLongitude() + "). You may need to check in with " + userFirstName + ".";

        if (!customMessage.isEmpty()) {
            message += "\n\n" + userFirstName + " says: " + customMessage;
        }

        SmsManager.getDefault().sendTextMessage("+1" + recipient.getPhoneNumber(), null, message, null, null);
    }
}
