package cse403.homesafe.Messaging;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.IBinder;
import android.preference.PreferenceManager;

import cse403.homesafe.Data.Contact;

/**
 * Abstraction that serves as the interface for sending emails through TODO: Decide email service
 */
public class Email extends Service implements Message {

    private static Email _instance = new Email();

    private Email() { }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

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

        Context context = getApplicationContext();  // Current application context
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);  // Lookup preferences
        String userName = prefs.getString("name", null);  // Lookup user's name in preferences, return null if not found
        if (userName == null)
            throw new RuntimeException("User's name was not set");

        String emailSubject = userName + " May Need Your Help";
        String emailBody = userName + " was using HomeSafe, a walking safety app. They set a destination but"
                + " have not checked in with the app. Their last known coordinates were (" + location.getLatitude() + ", "
                + location.getLongitude() + "). You should check in with them.";

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);
        emailIntent.setData(Uri.parse("mailto:" + recipient.getEmail()));
        emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(emailIntent);
    }
}
