package cse403.homesafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;
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

/**
 * Manages interaction with user on the Arrival Screen -- when the trip has ended.
 * Offers functionality to start a new trip.
 *
 * If the user specified in settings that they
 * would like to have contacts notified on safe arrival, these messages are sent out
 * after contacting google play services, and the contact names that have been notified will be
 * displayed on screen.
 */
public class ArrivalActivity extends ActionBarActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private RecyclerView contactsView;  // Provides a view of contacts that have been notified

    // Provides a binding from a list of contacts to be displayed within RecyclerView "contactsView"
    private RecyclerView.Adapter rvAdapter;

    private Button homeScreenBtn; // Button to start a new trip

    private Location lastKnownLocation; // Current location
    private GoogleApiClient mGoogleApiClient; // Google API Client to retrieve current location

    private Messenger.MessageType messageType;  // The message alert type

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000; // Google play services resolution request
    private final String TAG = "ArrivalActivity";   // For logcat debugging purposes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrival_screen);
        getSupportActionBar().setTitle("SAFE ARRIVAL");

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

        if (checkPlayServices()) {
            Log.e(TAG, "Google Play Services is installed");
            buildGoogleApiClient();
            onStart();
        } else {
            Log.e(TAG, "Google Play Services is not installed");
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
     * Builds the Google API Client.
     */
    protected synchronized void buildGoogleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        if (mGoogleApiClient != null) {
            Log.i(TAG, "Build Complete");
        } else {
            Log.i(TAG, "Build Incomplete");
        }
    }

    /**
     * Attempts to start Google API Client. Connection to Google API Client
     * can be successful, failure, or suspended.
     */
    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            Log.e(TAG, "Connection Started");
            mGoogleApiClient.connect();
        }
    }

    /**
     * Callback method used by Google Play Services to notify that Location Services is ready
     * @param bundle Bundle
     */
    @Override
    public void onConnected(Bundle bundle) {
        lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (lastKnownLocation != null) {
            // Only send out messages on safe arrival if the user has specified this.
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            boolean shouldNotify = prefs.getBoolean("homeSafeAlert", false);
            if (shouldNotify) {
                // Notify contacts on safe arrival
                Messenger.sendNotifications(Contacts.Tier.ONE, lastKnownLocation,
                        getApplicationContext(), messageType);
                rvAdapter = new ArrivalAdapter(new ArrayList<Contact>(Contacts.getInstance().getContactsInTier(Contacts.Tier.ONE)));
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
     * Callback method if connection to Google API Client is suspended.
     */
    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection to Google API Client is suspended");
    }

    /**
     * Callback method if connection to Google API Client is failed.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection to Google API Client failed");
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

}
