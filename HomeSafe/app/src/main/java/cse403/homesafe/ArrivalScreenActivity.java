package cse403.homesafe;

import android.location.Location;
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


public class ArrivalScreenActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    // TODO Need to populate the contact list from the first tier contacts (ALEX): Messenger takes care of that
    private final String TAG = "ArrivalScreenActivity";
    private RecyclerView.Adapter rvAdapter;

    private final String[] contactsList = {"Becky"};
    private Button homescreenBtn;
    private ArrayList<String> contacts;

    private Email mailer;  // TODO (ALEX): Remove once Messenger is fully functional. See below

    private GoogleApiClient mGoogleApiClient;
    private Location lastKnownLocation;

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrival_screen);

        RecyclerView contactsView = (RecyclerView) findViewById(R.id.contactsView);
        homescreenBtn = (Button) findViewById(R.id.homescreenBtn);

        backToStartScreen();

        mailer = Email.getInstance();  // TODO: Remove this once Messenger can retrieve contacts from Contacts.java. Use Messenger then
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

        // TODO send out all the emails or SMSs?? (ALEX): This is taken care of in the LocationServices callback onConnected() below

        // specify an adaoter
        rvAdapter = new ArrivalScreenAdapter(contactsList);
        contactsView.setAdapter(rvAdapter);
    }

    /* Ends the timer for the trip, taking the user automatically to the arrival screen.
* */
    private void backToStartScreen() {
        homescreenBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
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
            // TODO: Remove the below method of sending notifications once Contacts are properly populated. Use Messenger then
            Contact testContact = new Contact("Alex", "jahrndez@uw.edu", "4259884882", Contacts.Tier.ONE);
            mailer.sendMessage(testContact, lastKnownLocation, "Test custom message", getApplicationContext(), Messenger.MessageType.HOMESAFE);
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
}
