package cse403.homesafe.Messaging;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;

import cse403.homesafe.Data.Contact;
import cse403.homesafe.Utility.ContextHolder;

/**
 * Abstraction that serves as the interface for sending text messages
 * through Android's native messaging service.
 */
public class SMS implements Message {

    // Instance of SMS
    private static final SMS _instance = new SMS();
    private SmsManager smsManager;

    // Private singleton constructor
    private SMS() {
        smsManager = SmsManager.getDefault();
    }

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
    public void sendMessage(Contact recipient, android.location.Location location, String customMessage) {
        String message = "";
        Context context = ContextHolder.getContext();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String userName = preferences.getString("name", null);

        if (userName == null) {
            throw new RuntimeException("No user name found.");
        }

        message = "This user may need your help.\n";
        message += "User: " + userName + "\nLocation: " + location.toString();

        if (!customMessage.isEmpty()) {
            message += "\nMessage: " + customMessage;
        }

        smsManager.sendTextMessage("+1" + recipient.getPhoneNumber(), null, message, null, null);
    }
}
