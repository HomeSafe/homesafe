package cse403.homesafe.Messaging;

import cse403.homesafe.Data.Contact;

import android.content.Context;
import android.location.Location;

/**
 * Interface for the various communication channels available from the device. Provides
 * a consistent method of invocation.
 */
public interface Message {

    /**
     * Sends alert message to the specified recipient through the appropriate subclass
     * communication channel. Requires caller to pass in application Context. This is
     * easily achievable using getApplicationContext() from within an Activity.
     * @param recipient Recipient of the intended message
     * @param location Last location of user
     * @param customMessage Message to be send
     */
    public void sendMessage(Contact recipient, Location location, String customMessage, Context context);

}
