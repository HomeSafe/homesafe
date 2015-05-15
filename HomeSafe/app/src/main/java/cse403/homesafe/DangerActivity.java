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
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

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

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        input.setBackgroundColor(0xFFAAAAAA);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String enteredPassword = input.getText().toString();
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String pin = preferences.getString("pin", null);

                if (pin == null)
                    Log.e(TAG, "Password wasn't stored or accessed correctly");

                int pincode = Integer.parseInt(pin);

                if (pincode == Integer.parseInt(enteredPassword)) {
                    dialog.cancel();
                    timer.cancel();
                    Toast.makeText(getApplicationContext(), "Correct Pincode", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), ArrivalScreenActivity.class));
                } else {
                    numAttempts++;
                    if (numAttempts == 3) {

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
        timer = new CountDownTimer(600, 1) {
            @Override
            public void onTick(long millisUntilFinished) {
//                countDownPeriod = millisUntilFinished;  // update how much time is left in the timer
                long seconds = millisUntilFinished / 1000;
//                int barVal = (int) (millisUntilFinished/ 1000);  // update progress bar animation
//                pb.setProgress(barVal);

                // re-calculate the text display for the timer
//                String hrs = String.format("%02d", seconds / 3600);
//                seconds %= 3600;       // strip off seconds that got converted to hours
//                String mins = String.format("%02d", seconds / 60);
//                seconds %= 60;         // strip off seconds that got converted to minutes
//                String secs = String.format("%02d", seconds % 60);
//                txtTimer.setText(hrs + ":" + mins + ":" + secs);
            }

            @Override
            public void onFinish() {
                timer.cancel();
//                pb.setProgress(0);
//                Toast.makeText(HSTimerActivity.this, "Your Trip Has Ended", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(HSTimerActivity.this, ArrivalScreenActivity.class));
                // TODO: Remove once Contacts is properly populated and Messenger becomes functional
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
            Messenger.sendNotifications(Contacts.Tier.ONE, mLastLocation, getApplicationContext(), Messenger.MessageType.DANGER);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

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
