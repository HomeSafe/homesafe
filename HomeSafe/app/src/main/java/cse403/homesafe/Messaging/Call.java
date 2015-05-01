package cse403.homesafe.Messaging;

import android.app.IntentService;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import cse403.homesafe.Data.Contact;
import cse403.homesafe.Data.Location;

/**
 * TODO: Discuss if feasible
 */
public class Call extends IntentService {

    public Call() {
        super("Call");
    }

    public static Message getInstance() {
        return null;
    }

    public void sendMessage(Contact recipient, Location location, String customMessage) {

    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    // Makes an audio call through Android's native service.
    private void call(String phoneNumber) {
        try {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse(phoneNumber));
            getApplicationContext().startActivity(callIntent);
        } catch (ActivityNotFoundException anfe) {
            Log.e("Android calling", "Call failed", anfe);
            anfe.printStackTrace();
        }
    }
}
