package cse403.homesafe.Messaging;

import android.telephony.SmsManager;

import cse403.homesafe.Data.Contact;

/**
 * Abstraction that serves as the interface for sending text messages
 * through Android's native messaging service.
 */
public class SMS implements Message {

    // Instance of SMS
    private static SMS _instance = new SMS();
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

    }
}
