package cse403.homesafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import cse403.homesafe.Data.Contacts;
import cse403.homesafe.Data.SecurityData;
import cse403.homesafe.Messaging.Messenger;

public class HSTimerActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    // TODO round the corners of the spinner and add a shadow OR do something to make it stand
    // out from the background and obvious that it's a drop down menu.
    private CountDownTimer timer;   // representation for a timer clock
    private long countDownPeriod;   // how much time is left in the timer

    // on-screen components
    private ProgressBar pb;         // the timer animation
    private Button btnAdd;          // the button for adding time
    private Button btnEnd;          // the button for ending the trip early
    private Spinner timeOptions;    // the different amounts of time that can be added to timer
    private TextView txtTimer;      // textual representation of the time left in timer

    private int numAttempts;    // current number of incorrect password entries


    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    private static final int INCORRECT_TRIES = 3;           // max number of incorrect password attempts
    private static final String TAG = "HSTimerActivity";    // for logcat purposes


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hstimer);
        getSupportActionBar().setTitle("Trip in progress");
        numAttempts = 0;

        // creates the views for the on-screen components
        initProgressBar();
        addItemsToTimeOptions();

        txtTimer = (TextView) findViewById(R.id.textTimer);
        txtTimer.setText("00:00");

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnEnd = (Button) findViewById(R.id.btnEnd);

        // attaches the listeners for the two buttons
        addToTimer();
        endTimer();

        // Begin the clock with full time and set the stand-in for the text representation.
        txtTimer.setText("00:00");
        countDownPeriod = getIntent().getExtras().getLong("timefromuser");
        if (countDownPeriod == 0) {
            Toast.makeText(HSTimerActivity.this, "You entered 0 time!", Toast.LENGTH_SHORT).show();
            finish();
        }

        pb.setMax((int) countDownPeriod / 1000);
        createTimer();
    }

    /* Helper method to initializes the progress bar.
    *  Makes sure the progress bar is view-able, and that it's facing the right
    *  way to make the animation make sense to the user.
    * */
    private void initProgressBar() {
        pb = (ProgressBar) findViewById(R.id.progressBar);
        // rotates pb when starting up so that the progress bar starts from the bottom of
        // the circle rather than from the side.
        Animation an = new RotateAnimation(0f, 0f, 0f, 0f);
        an.setFillAfter(true);
        pb.startAnimation(an);
    }

    /* Creates and starts a new immutableCountDownTimer object.
     * @effect creates a new CountDownTimer. The caller is responsible for canceling the
     *         old timer.
     * */
    private void createTimer() {
        timer = new CountDownTimer(countDownPeriod, 1) {
            @Override
            public void onTick(long millisUntilFinished) {
                countDownPeriod = millisUntilFinished;  // update how much time is left in the timer
                long seconds = millisUntilFinished / 1000;
                int barVal = (int) (millisUntilFinished/ 1000);  // update progress bar animation
                pb.setProgress(barVal);

                // re-calculate the text display for the timer
                String hrs = String.format("%02d", seconds / 3600);
                seconds %= 3600;       // strip off seconds that got converted to hours
                String mins = String.format("%02d", seconds / 60);
                seconds %= 60;         // strip off seconds that got converted to minutes
                String secs = String.format("%02d", seconds % 60);
                txtTimer.setText(hrs + ":" + mins + ":" + secs);
            }

            @Override
            public void onFinish() {
                timer.cancel();
                pb.setProgress(0);
                Toast.makeText(HSTimerActivity.this, "Your Trip Has Ended", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(HSTimerActivity.this, DangerActivity.class));
            }

        }.start();
    }

    /** Adds more time to the timer depending on how much has been selected in the timeOption
     *  drop down menu.
     */
    private void addToTimer() {
        btnAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Each timer object is immutable so we must cancel the old one to create
                // a new timer object with more time in it.
                if (timer != null) {
                    timer.cancel();
                }
                // the selected time in the drop down menu
                String selectedTime = timeOptions.getSelectedItem().toString();
                countDownPeriod += parseTimeString(selectedTime);

                // the maximum capacity for the progress bar must be increased
                pb.setMax((int) (countDownPeriod / 1000));
                createTimer();
                Toast.makeText(HSTimerActivity.this, "Added " + selectedTime,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /* Ends the timer for the trip, taking the user automatically to the arrival screen.
    * */
    private void endTimer() {
        btnEnd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptForPassword();
            }
        });
    }

    /* Converts a string in the format "[int] [time unit]" to milliseconds to be
    *  fed into a timer.
    * @param time the string to be parsed
    * @requires time to be in the correct format of a number followed by a space followed
    *           by a unit of time: "sec" for seconds, "min" for minutes, and "hr" for hours.
    * @returns -1 if the string was not in the correct format, or a positive number if
    *          it was correctly parsed.
    * */
    public static long parseTimeString(String time) {
        long millis = 0;
        if (time == null) {
            return -1;
        }
        String tokens[] = time.split(" ");
        if (tokens.length != 2) {
            return -1;
        }
        try {
            millis = Long.parseLong(tokens[0]);     // throws an exception if this doesn't work
            switch (tokens[1]) {
                case "sec": millis *= 1000;
                    break;
                case "min": millis *= (1000 * 60);
                    break;
                case "hr": millis *= (1000 * 60 *60);
                    break;
                default: return -1;         // invalid time unit
            }
        } catch (NumberFormatException e) {
            return -1;
        }
        return millis;
    }

    /* Helper method to populate the drop down menu with options to pick from
    *  These times must be in the format [number] [time unit (sec, min, hr)] or
    *  else parseTimeString cannot read them as proper amounts of time.
    * */
    private void addItemsToTimeOptions() {
        timeOptions = (Spinner) findViewById(R.id.timeOptions);
        List<String> list = new ArrayList<String>();
        list.add("1 min");
        list.add("5 min");
        list.add("10 min");
        list.add("30 min");
        list.add("1 hr");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeOptions.setAdapter(dataAdapter);
    }

    /**
     * A dialog that prompts the user for password. If user enters password
     * correctly, then user will be directed to Arrival Screen, otherwise if the
     * user inputs the password incorrectly 3 times then
     */
    private void promptForPassword() {
        final HSTimerActivity that = this;
        final AlertDialog.Builder alert = new AlertDialog.Builder(that);

        alert.setTitle("Unlock");
        alert.setMessage("Please Enter Password");

        // Set an EditText view to get user input
        final EditText input = new EditText(that);
        input.setTextColor(0xFFFFFFFF);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
        input.setGravity(Gravity.CENTER_HORIZONTAL);
        alert.setView(input);

        alert.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String enteredPassword = input.getText().toString();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String pin = prefs.getString("pin", null);

                if (pin == null)
                    Log.e(TAG, "Password wasn't stored or accessed correctly");

                //int pincode = Integer.parseInt(pin);

                if (pin.equals(enteredPassword)) {
                    if (timer != null) {
                        timer.cancel();
                        countDownPeriod = 0;
                        txtTimer.setText("00:00:00");
                        pb.setProgress(0);
                        numAttempts = 0;
                    }
                    Toast.makeText(HSTimerActivity.this, "Ended Trip", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(HSTimerActivity.this, ArrivalScreenActivity.class));
                } else {
                    numAttempts++;
                    if (numAttempts == INCORRECT_TRIES) {
                        buildGoogleApiClient();
                        onStart();
                        Messenger.sendNotifications(Contacts.Tier.ONE, mLastLocation, getApplicationContext(), Messenger.MessageType.DANGER);
                        Toast.makeText(HSTimerActivity.this, "Contacts have been notified", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(HSTimerActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                        promptForPassword();
                    }
                }
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        alert.show();
    }

    /**
     * Callback method of Google API Client if connected
     */
    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            Log.e(TAG, "Connected!");
        } else {
            Log.e(TAG, "Failed on getting last location");
        }

    }

    /**
     * Callback method for Google API Client if connection is suspended
     */
    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG, "Connection Suspended");
    }

    /**
     * Callback method for Google API Client if connection fails
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "Connection Failed");
    }

    /**
     * Starts the Google API Client
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
     * Builds the Google API Client
     */
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
}