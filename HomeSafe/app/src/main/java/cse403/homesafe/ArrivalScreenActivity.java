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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import cse403.homesafe.Data.Contact;
import cse403.homesafe.Data.Contacts;
import cse403.homesafe.Messaging.Email;
import cse403.homesafe.Messaging.Messenger;

/**
 * Manages interaction with user on the Arrival Screen -- when the trip has ended.
 * Offers functionality to return to homescreen.
 *
 * If the user specified in settings that they
 * would like to have contacts notified on safe arrival, these messages are sent out
 * after contacting google play services.
 */
public class ArrivalScreenActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    // on screen components
    private final String TAG = "ArrivalScreenActivity";
    private RecyclerView.Adapter rvAdapter;
    private RecyclerView contactsView;
    private Button homescreenBtn;
    private final ArrayList<Contact> contacts = new ArrayList<Contact>(Contacts.getInstance().getContactsInTier(Contacts.Tier.ONE));

    private GoogleApiClient mGoogleApiClient;
    private Location lastKnownLocation;

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrival_screen);
        getSupportActionBar().setTitle("SAFE ARRIVAL");
        contactsView = (RecyclerView) findViewById(R.id.contactsView);
        homescreenBtn = (Button) findViewById(R.id.homescreenBtn);

        // set listener for back to start screen
        setBackToStartScreenListener();

        // use this setting to improve performance if you know that changes
        // in content to do change the layout size of the RecyclerView
        contactsView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager rvLayoutManager = new LinearLayoutManager(this);
        contactsView.setLayoutManager(rvLayoutManager);

        if (checkPlayServices()) {
            buildGoogleApiClient();
            onStart();
        } else {
            Log.e(TAG, "Google Play Services is not installed");
        }
    }

    /* Ends the timer for the trip, taking the user automatically to the arrival screen.
* */
    private void setBackToStartScreenListener() {
        homescreenBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // When this button is clicked, clear the activity stack and start fresh from
                // the start screen.
                startActivity(new Intent(getApplicationContext(),
                        StartScreenActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        if (mGoogleApiClient != null) {
            Log.e(TAG, "Build Complete");
        } else {
            Log.e(TAG, "Build Incomplete");
        }
    }

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
                        getApplicationContext(), Messenger.MessageType.HOMESAFE);
                rvAdapter = new ArrivalScreenAdapter(contacts);
            } else {
                // Don't contact anyone
                ArrayList<Contact> c = new ArrayList<Contact>();
                c.add(new Contact("No one", "", "", Contacts.Tier.ONE));
                rvAdapter = new ArrivalScreenAdapter(c);
            }
            contactsView.setAdapter(rvAdapter);

        } else {  // FusedLocationApi returned null, meaning current location is not currently available
            Log.e(TAG, "Current location is not available!");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG, "Connection Suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "Connection Failed");
    }

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
    public void onBackPressed() {
        return;
    }
}
