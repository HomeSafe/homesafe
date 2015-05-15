package cse403.homesafe;

import android.app.AlertDialog;
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
import android.widget.EditText;
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
 * This activity displays a password prompt to the user.
 * If the user fails to correctly enter the password within
 * some amount of time then the phone will contact the emergency
 * contacts.
 */
public class DangerActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "DangerActivity";
    private CountDownTimer timer;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private int numAttempts;

    private RecyclerView contactsView;

    private RecyclerView.Adapter rvAdapter;

    private int currentTier;

    private final ArrayList<Contact> contacts1 = new ArrayList<Contact>(Contacts.getInstance().getContactsInTier(Contacts.Tier.ONE));
    private final ArrayList<Contact> contacts2 = new ArrayList<Contact>(Contacts.getInstance().getContactsInTier(Contacts.Tier.TWO));
    private final ArrayList<Contact> contacts3 = new ArrayList<Contact>(Contacts.getInstance().getContactsInTier(Contacts.Tier.THREE));

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danger);

        getSupportActionBar().setTitle("DANGER ZONE");

        contactsView = (RecyclerView) findViewById(R.id.contactsView);


        RecyclerView.LayoutManager rvLayoutManager = new LinearLayoutManager(this);
        contactsView.setLayoutManager(rvLayoutManager);
        contactsView.setHasFixedSize(true);

        createTimer();
        numAttempts = 0;
        promptForPassword();
    }

    /**
     * Prompt user for password. Send entered password to appropriate callback.
     */
    private void promptForPassword() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Enter Passcode");
        alert.setMessage("To Stop Alerts, Enter Correct Passcode");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        input.setTextColor(0xFFFFFFFF);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
        input.setGravity(Gravity.CENTER_HORIZONTAL);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String enteredPassword = input.getText().toString();
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String pin = preferences.getString("pin", null);

                if (pin == null)
                    Log.e(TAG, "Password wasn't stored or accessed correctly");

                if (pin.equals(enteredPassword)) {
                    dialog.cancel();
                    timer.cancel();
                    Toast.makeText(DangerActivity.this, "Correct Pincode", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DangerActivity.this, ArrivalScreenActivity.class));
                } else {
                    numAttempts++;
                    if (numAttempts == 3) {
                        numAttempts = 0;
                        timer.cancel();
                        if (checkPlayServices()) {
                            Log.e(TAG, "Google Play Services is installed");
                            buildGoogleApiClient();
                            onStart();
                        } else {
                            Log.e(TAG, "Google Play Services is not installed");
                        }
                    } else {
                        promptForPassword();
                    }
                }
            }
        });

        alert.show();
    }

    /* Creates and starts a new immutableCountDownTimer object.
     * @effect creates a new CountDownTimer. The caller is responsible for canceling the
     *         old timer.
     * */
    private void createTimer() {
        timer = new CountDownTimer(60000, 1) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                timer.cancel();
                if (checkPlayServices()) {
                    Log.e(TAG, "Google Play Services is installed");
                    buildGoogleApiClient();
                    onStart();
                } else {
                    Log.e(TAG, "Google Play Services is not installed");
                }
            }

        }.start();
    }

    @Override
    public void onConnected(Bundle bundle) {
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
            currentTier++;

            // TODO: Implement more levels of notification by creating additional timers and password prompts
//            createTimer();
        }
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
}
