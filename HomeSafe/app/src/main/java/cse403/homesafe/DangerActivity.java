package cse403.homesafe;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import cse403.homesafe.Data.Contact;
import cse403.homesafe.Data.Contacts;
import cse403.homesafe.Messaging.Messenger;


/**
 * This activity displays a password prompt to the user.
 * If the user fails to correctly enter the password within
 * some amount of time then the phone will contact the emergency
 * contacts.
 */
public class DangerActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "DangerActivity";
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;

    private RecyclerView contactsView;

    private RecyclerView.Adapter rvAdapter;

    private int currentTier;

    private final ArrayList<Contact> contacts1 = new ArrayList<Contact>(Contacts.getInstance().getContactsInTier(Contacts.Tier.ONE));
    private final ArrayList<Contact> contacts2 = new ArrayList<Contact>(Contacts.getInstance().getContactsInTier(Contacts.Tier.TWO));
    private final ArrayList<Contact> contacts3 = new ArrayList<Contact>(Contacts.getInstance().getContactsInTier(Contacts.Tier.THREE));

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private Button homescreenBtnDanger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danger);

        getSupportActionBar().setTitle("DANGER ZONE");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);

        contactsView = (RecyclerView) findViewById(R.id.contactsView);
        homescreenBtnDanger = (Button) findViewById(R.id.homescreenBtnDanger);

        // set listener for back to start screen
        setBackToStartScreenListener();

        RecyclerView.LayoutManager rvLayoutManager = new LinearLayoutManager(this);
        contactsView.setLayoutManager(rvLayoutManager);
        contactsView.setHasFixedSize(true);
        currentTier = 1;

        buildGoogleApiClient();
        onStart();

        promptForPassword();
    }

    /* Ends the timer for the trip, taking the user automatically to the arrival screen.
* */
    private void setBackToStartScreenListener() {
        homescreenBtnDanger.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // When this button is clicked, clear the activity stack and start fresh from
                // the start screen.
                startActivity(new Intent(getApplicationContext(),
                        StartScreenActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
    }

    /**
     * Prompt user for password. Send entered password to appropriate callback.
     */
    private void promptForPassword() {
        Log.i(TAG, "PROMPT FOR PASSWORD WITH currentTier is " + currentTier);
        Log.i(TAG, Log.getStackTraceString(new Exception()));
        Intent i = new Intent(getApplicationContext(), PasswordActivity.class);
        String time;
        if(currentTier == 1) {
            time = "12"; // 2 minutes
        } else if (currentTier == 2) {
            time = 5 * 6 + "";
        } else { // tier 3+
            time = 30 * 60 + "";
        }
        String message = "Please enter your password to end-trip.";
        String numChances = "3";
        String confirmButtonMessage = "End Trip";
        String enableCancelButton = "false";
        i.putExtra("passwordParams", new ArrayList<String>(Arrays.asList(time, message, numChances, confirmButtonMessage, enableCancelButton)));
        startActivityForResult(i, 1);
    }

    private void onCorrectPinCode() {
        Toast.makeText(DangerActivity.this, "Correct Pincode", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(DangerActivity.this, ArrivalScreenActivity.class));
    }

    /**
     * Callback for PasswordActivity. If success, moves to end trip. If failure,
     * notifies currentTier contacts, then
     * increments currentTier up to a maximum of three.
     *
     * @param requestCode the request code
     * @param resultCode the result code
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data.getExtras().containsKey("retval")) {
            Serializable retcode = data.getExtras().getSerializable("retval");
            if (retcode.equals(PasswordActivity.RetCode.SUCCESS)) {
                onCorrectPinCode();
            } else if (retcode.equals(PasswordActivity.RetCode.FAILURE)) {
                // Send alerts to currentTier contacts
                alertContacts();

                if (currentTier < 3) {
                    currentTier++;
                }
                promptForPassword(); // keep asking
            } else if (retcode.equals(PasswordActivity.RetCode.SPECIAL)) {
                alertContacts();
                startActivity(new Intent(DangerActivity.this, ArrivalScreenActivity.class));
            } else {
                // nothing to do here. Have a lovely day. assert(false);
            }
        }

    }

    public void alertContacts() {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            Log.e(TAG, "Connected!");
            Contacts.Tier tier;
            ArrayList<Contact> contacts;
            switch (currentTier){
                case 1 :    tier = Contacts.Tier.ONE;
                            contacts = contacts1;
                            break;
                case 2 :    tier = Contacts.Tier.TWO;
                            contacts = contacts2;
                            break;
                case 3 :    tier = Contacts.Tier.THREE;
                            contacts = contacts3;
                            break;
                default :   tier = Contacts.Tier.ONE;
                            contacts = contacts1;
            }

            Messenger.sendNotifications(tier, mLastLocation, getApplicationContext(), Messenger.MessageType.DANGER);
            rvAdapter = new ArrivalScreenAdapter(contacts);
            contactsView.setAdapter(rvAdapter);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

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

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            Log.e(TAG, "Connection Started");
            mGoogleApiClient.connect();
        }
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
    public void onResume() {
        super.onResume();
        String ns = getApplicationContext().NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(ns);
        nMgr.cancelAll();
    }

    // Do nothing when the back button is pressed
    @Override
    public void onBackPressed() {
        return;
    }
}
