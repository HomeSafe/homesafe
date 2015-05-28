package cse403.homesafe;

import android.app.NotificationManager;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cse403.homesafe.Data.Contact;
import cse403.homesafe.Data.Contacts;
import cse403.homesafe.Messaging.Messenger;

/**
 * This activity displays a password prompt to the user.
 * If the user fails to correctly enter the password within
 * some amount of time then the phone will contact the emergency
 * contacts.
 */
public class DangerActivity extends ActionBarActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private Location mLastLocation; // Current location
    private GoogleApiClient mGoogleApiClient; // Google API Client to retrieve current location

    private Button homeScreenBtnDanger; // Button to start a new trip

    private RecyclerView contactsView;  // Provides a view of contacts that have been notified

    // Provides a binding from a list of contacts to be displayed within RecyclerView "contactsView"
    private RecyclerView.Adapter rvAdapter;

    private Contacts.Tier currentTier; // Current tier level

    private static final int PASSWORD_PROMPT_TIME = 30; // Time allowed for user to enter password
    private static final int NUM_PASSWORD_ATTEMPTS = 3; // Num of password attempts the user has

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 1000; // Google play services resolution request
    private final String TAG = "DangerActivity"; // For logcat debugging purposes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_danger);

        getSupportActionBar().setTitle("DANGER ZONE");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);

        contactsView = (RecyclerView) findViewById(R.id.contactsView);
        homeScreenBtnDanger = (Button) findViewById(R.id.homescreenBtnDanger);

        // Sets listener for back to start screen
        setBackToStartScreenListener();

        RecyclerView.LayoutManager rvLayoutManager = new LinearLayoutManager(this);
        contactsView.setLayoutManager(rvLayoutManager);
        contactsView.setHasFixedSize(true);
        currentTier = Contacts.Tier.ONE;

        if (checkPlayServices()) {
            Log.e(TAG, "Device has Google Play Services installed");
            buildGoogleApiClient();
            onStart();
        } else {
            Log.e(TAG, "Device does not have Google Play Services installed");
        }
        promptForPassword();
    }

    /**
     * Callback for PasswordActivity. If success, moves to end trip. If failure,
     * notifies currentTier contacts, then
     * increments currentTier up to a maximum of three.
     *
     * @param requestCode : the request code
     * @param resultCode : the result code
     * @param data : intent object containing any data returned by the sub-activity
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data.getExtras().containsKey("retval")) {
            Serializable retcode = data.getExtras().getSerializable("retval");
            if (retcode.equals(PasswordActivity.RetCode.SUCCESS)) {
                onCorrectPinCode();

            } else if (retcode.equals(PasswordActivity.RetCode.FAILURE)) {
                // Send alerts to currentTier contacts
                alertContacts();

                if (currentTier == Contacts.Tier.THREE)
                    return;

                incrementTier();

                // Keep prompting the user to input their password.
                promptForPassword();
            } else if (retcode.equals(PasswordActivity.RetCode.SPECIAL)) {
                alertContacts();
                Intent intent = new Intent(getApplicationContext(), ArrivalActivity.class);
                intent.putExtra("AlertType", "DANGER");
                startActivity(intent);
                startActivity(new Intent(DangerActivity.this, ArrivalActivity.class));
            }
        }
    }

    /**
     * Send danger alerts to the current tier contacts.
     */
    public void alertContacts() {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            Log.e(TAG, "Connected!");
            List<Contact> contacts = Contacts.getInstance().getContactsInTier(currentTier);
            Messenger.sendNotifications(currentTier, mLastLocation, getApplicationContext(), Messenger.MessageType.DANGER);
            rvAdapter = new ArrivalAdapter(contacts);
            contactsView.setAdapter(rvAdapter);
        }
    }

    private void incrementTier() {
        switch (currentTier) {
            case ONE:   currentTier = Contacts.Tier.TWO;
                        break;
            case TWO:   currentTier = Contacts.Tier.THREE;
                        break;
            case THREE:
                        break;
            default:
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        String ns = getApplicationContext().NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(ns);
        nMgr.cancelAll();
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
     * Callback method if connection to Google API Client is successful.
     */
    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Connected to Google API Client");
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
     * Do nothing when the back button is pressed.
     */
    @Override
    public void onBackPressed() {
        return;
    }

    /**
     * Prompt user for password. Send entered password to appropriate callback.
     */
    private void promptForPassword() {
        Log.i(TAG, "PROMPT FOR PASSWORD WITH currentTier is " + currentTier);
        Log.i(TAG, Log.getStackTraceString(new Exception()));
        Intent i = new Intent(getApplicationContext(), PasswordActivity.class);
        String time = PASSWORD_PROMPT_TIME + "";
        String numChances = NUM_PASSWORD_ATTEMPTS  + "";
        String confirmButtonMessage = "End Trip";
        String enableCancelButton = "false";
        i.putExtra("passwordParams", new ArrayList<String>(Arrays.asList(time, numChances, confirmButtonMessage, enableCancelButton)));
        startActivityForResult(i, 1);
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

    /**
     * Displays a message that the user inputted a correct pincode and directs the user
     * to the safe arrival screen.
     */
    private void onCorrectPinCode() {
        Toast.makeText(DangerActivity.this, "Correct Pincode", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), ArrivalActivity.class);
        intent.putExtra("AlertType", "HOMESAFE");
        startActivity(intent);
    }

    /**
     * Ends the timer for the trip, taking the user automatically to the arrival screen.
     */
    private void setBackToStartScreenListener() {
        homeScreenBtnDanger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // When this button is clicked, clear the activity stack and start fresh from
                // the start screen.
                startActivity(new Intent(getApplicationContext(),
                        StartActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
    }

}
