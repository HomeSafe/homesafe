package cse403.homesafe.Messaging;

import cse403.homesafe.Data.Contact;
import cse403.homesafe.Data.Location;

/**
 *
 */
public interface Message {

    /**
     *
     * @param recipient Recipient of the intended message
     * @param location Last location of user
     * @param customMessage Message to be send
     */
    public void sendMessage(Contact recipient, Location location, String customMessage);

}
