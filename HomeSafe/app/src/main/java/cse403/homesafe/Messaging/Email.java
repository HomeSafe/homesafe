package cse403.homesafe.Messaging;

import cse403.homesafe.Data.Contact;
import cse403.homesafe.Data.Location;

/**
 * Abstraction that serves as the interface for sending emails through TODO: Decide email service
 */
public class Email {

    private static Email _instance = new Email();

    private Email() { }

    /**
     * Get singleton instance of Email
     * @return Instance of Email
     */
    public static Email getInstance() {
        return _instance;
    }

    /**
     * Sends email to recipient
     * @param recipient     Recipient of the intended message
     * @param location      Last location of user
     * @param customMessage Customized message to be sent
     */
    public void sendMessage(Contact recipient, Location location, String customMessage) {

        // Integration with GMail ?
    }
}
