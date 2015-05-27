package cse403.homesafe.Messaging;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;

import cse403.homesafe.Data.Contact;

import static cse403.homesafe.Messaging.Messenger.*;

/**
 * Abstraction that serves as the interface for sending text messages
 * through Android's native messaging service.
 */
public class SMS implements Message {

    // Instance of SMS
    private static SMS _instance;

    // Private singleton constructor
    private SMS() { }

    /**
     * Get singleton instance of SMS
     * @return Default instance of SMS
     */
    public static SMS getInstance() {
        if (_instance == null)
            _instance = new SMS();
        return _instance;
    }

    /**
     * Sends SMS message to recipient through Android's SmsManager
     * @param recipient Recipient of the intended message
     * @param location Last location of user
     * @param customMessage Customized message to be sent
     */
    @Override
    public void sendMessage(Contact recipient, Location location, String customMessage, Context context, MessageType type) {
        String message = "";

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        
        String userFirstName = preferences.getString("firstName", null);
        String userLastName = preferences.getString("lastName", null);

        userFirstName = (userFirstName == null) ? "" : userFirstName;
        userLastName = (userLastName == null) ? "" : userLastName;

        if (type == MessageType.DANGER) {
            message = "Hi, this is " + userFirstName + " " + userLastName + ". I may need your help!"
                    + " You should check in with me. [AUTOMATED SMS SENT BY HOMESAFE]";
        } else {
            message = "Hi, this is " + userFirstName + " " + userLastName + ". Just checking in to let"
                    + " you know I arrived to my destination safely!";
        }

        SmsManager.getDefault().sendTextMessage("+1" + recipient.getPhoneNumber(), null, message, null, null);
    }
}
