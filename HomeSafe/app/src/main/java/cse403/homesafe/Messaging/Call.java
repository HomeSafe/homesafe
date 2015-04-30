package cse403.homesafe.Messaging;

import cse403.homesafe.Data.Contact;
import cse403.homesafe.Data.Location;

/**
 * TODO: Discuss if feasible
 */
public class Call {

    private static Call _instance = new Call();

    private Call() { }

    public static Message getInstance() {
        return null;
    }

    public void sendMessage(Contact recipient, Location location, String customMessage) {

    }
}
