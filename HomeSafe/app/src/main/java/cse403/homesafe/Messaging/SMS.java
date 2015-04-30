package cse403.homesafe.Messaging;

import android.telephony.SmsManager;

import cse403.homesafe.Data.Contact;
import cse403.homesafe.Data.Location;

/**
 * Abstraction that serves as the interface for sending text messages through Android's native messaging service.
 */
public class SMS {

    // Instance of SMS
    private static SMS _instance = new SMS();

    // Private singleton constructor
    private SMS() { }

    /**
     * Get singleton instance of SMS
     * @return Instance of SMS
     */
    public static SMS getInstance() {
        return _instance;
    }

    /**
     * Sends SMS message to recipient through Android's SmsManager
     * @param recipient     Recipient of the intended message
     * @param location      Last location of user
     * @param customMessage Customized message to be sent
     */
    public void sendMessage(Contact recipient, Location location, String customMessage) {
        // String number = Contact.getNumber();   // TODO: uncomment once Contact is implemented
        SmsManager.getDefault().sendTextMessage(null, null, null, null, null);
    }
}
