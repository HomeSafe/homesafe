package cse403.homesafe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

import cse403.homesafe.Data.Contact;
import cse403.homesafe.Data.Contacts;
import cse403.homesafe.Messaging.Messenger;
import cse403.homesafe.Util.GoogleGPSUtils;

/**
 * Manages interaction with user on the Arrival Screen -- when the trip has ended.
 * Offers functionality to start a new trip.
 *
 * If the user specified in settings that they
 * would like to have contacts notified on safe arrival, these messages are sent out
 * after contacting google play services, and the contact names that have been notified will be
 * displayed on screen.
 */
public class ArrivalActivity extends ActionBarActivity {

    private RecyclerView contactsView;  // Provides a view of contacts that have been notified

    // Provides a binding from a list of contacts to be displayed within RecyclerView "contactsView"
    private RecyclerView.Adapter rvAdapter;

    private Button homeScreenBtn; // Button to start a new trip

    private Location lastKnownLocation; // Current location

    private Messenger.MessageType messageType;  // The message alert type

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000; // Google play services resolution request
    private final String TAG = "ArrivalActivity";   // For logcat debugging purposes
    private GoogleGPSUtils gpsUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Context ctx = this;

        // initialize gpsUtils
        gpsUtils = new GoogleGPSUtils(ctx);
        gpsUtils.start();

        setContentView(R.layout.activity_arrival_screen);
        ActionBar bar = getSupportActionBar();
        if (bar != null)
            bar.setTitle("SAFE ARRIVAL");

        String alertType = getIntent().getStringExtra("AlertType");
        if (alertType.equals("DANGER")) {
            messageType = Messenger.MessageType.DANGER;
        } else {
            messageType = Messenger.MessageType.HOMESAFE;
        }

        contactsView = (RecyclerView) findViewById(R.id.contactsView);
        homeScreenBtn = (Button) findViewById(R.id.homescreenBtn);

        // Sets listener for back to start screen
        setBackToStartScreenListener();

        // Use this setting to improve performance if you know that changes
        // in content to do change the layout size of the RecyclerView
        contactsView.setHasFixedSize(true);

        // Uses a linear layout manager
        RecyclerView.LayoutManager rvLayoutManager = new LinearLayoutManager(this);
        contactsView.setLayoutManager(rvLayoutManager);

        if(gpsUtils.isReady()) {
            lastKnownLocation = gpsUtils.getLastLocation();
        }
        if (lastKnownLocation != null) {
            // Only send out messages on safe arrival if the user has specified this.
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            boolean shouldNotify = prefs.getBoolean("homeSafeAlert", false);
            if (shouldNotify || messageType == Messenger.MessageType.DANGER) {
                // Notify contacts on safe arrival
                if (messageType == Messenger.MessageType.DANGER) {
                    Messenger.sendNotifications(Contacts.Tier.ONE, lastKnownLocation,
                            getApplicationContext(), messageType);
                    Messenger.sendNotifications(Contacts.Tier.TWO, lastKnownLocation,
                            getApplicationContext(), messageType);
                    Messenger.sendNotifications(Contacts.Tier.THREE, lastKnownLocation,
                            getApplicationContext(), messageType);
                } else {
                    Messenger.sendNotifications(Contacts.Tier.ONE, lastKnownLocation,
                            getApplicationContext(), messageType);
                }

                if (shouldNotify) {
                    rvAdapter = new ArrivalAdapter(new ArrayList<Contact>(Contacts.getInstance().getContactsInTier(Contacts.Tier.ONE)));
                }
            } else {
                // Don't contact anyone
                ArrayList<Contact> c = new ArrayList<Contact>();
                c.add(new Contact("No one", "", "", Contacts.Tier.ONE));
                rvAdapter = new ArrivalAdapter(c);
            }
            contactsView.setAdapter(rvAdapter);

        } else {
            // FusedLocationApi returned null, meaning current location is not currently available
            Log.e(TAG, "Current location is not available!");
        }
    }

    /**
     * Ends the timer for the trip, taking the user automatically to the arrival screen.
     */
    private void setBackToStartScreenListener() {
        homeScreenBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // When this button is clicked, clear the activity stack and start fresh from
                // the start screen.
                startActivity(new Intent(getApplicationContext(),
                        StartActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
    }

    /**
     * Do nothing when the back button is pressed.
     */
    @Override
    public void onBackPressed() {
        return;
    }

    /**
     * Returns true if GooglePlayServices is installed on the device otherwise
     * false.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();

        // disconnect utils
        if(gpsUtils != null) {
            gpsUtils.disconnect();
        }
    }

}
