package cse403.homesafe;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cse403.homesafe.Data.Contacts;
import cse403.homesafe.Messaging.Messenger;
import cse403.homesafe.Util.GoogleGPSUtils;

/**
 * This class represents the main activity for the duration of a trip. It holds the timer and
 * logic for a single trip, including the security mechanisms to disallow exiting until either
 * the user ends the trip or time runs out.
 */
public class TripActivity extends ActionBarActivity {

    private static final String TAG = "TripActivity";  // for logcat purposes

    private CountDownTimer timer;   // representation for a timer clock
    private long countDownPeriod;   // how much time is left in the timer

    // On-screen components
    private ProgressBar pb;         // the timer animation
    private Button btnAdd;          // the button for adding time
    private Button btnEnd;          // the button for ending the trip early
    private Spinner timeOptions;    // the different amounts of time that can be added to timer
    private TextView txtTimer;      // textual representation of the time left in timer

    private long currentTimeMillis; // the time left on the timer in millis

    private GoogleGPSUtils gpsUtils;
    private GoogleApiClient mGoogleApiClient;

    private static final int END_TRIP_PASSWORD_REQUEST = 1;
    private static final int ADD_TIME_PASSWORD_REQUEST = 2;
    private static final long TIME_BUFFER = 5;

    @Override
    public void onBackPressed() {
        // Override so that nothing occurs when user presses back.
        // To exit this activity, user must end trip manually or
        // time must run out.
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hstimer);
        getSupportActionBar().setTitle("Trip in Progress");

        currentTimeMillis = 0;

        // creates the views for the on-screen components
        initProgressBar();
        populateTimeOptions();

        txtTimer = (TextView) findViewById(R.id.textTimer);
        txtTimer.setText("00:00");

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnEnd = (Button) findViewById(R.id.btnEnd);

        // Attaches the listeners for the two buttons
        addToTimer();
        endTimer();

        // Begin the clock with full time and set the stand-in for the text representation.
        txtTimer.setText("00:00");
        countDownPeriod = getIntent().getExtras().getLong("timefromuser");
        if (countDownPeriod == 0) {
            Toast.makeText(TripActivity.this, "You entered 0 time!", Toast.LENGTH_SHORT).show();
            finish();
        }
        pb.setMax((int) countDownPeriod / 1000);
        createTimer();

        gpsUtils = new GoogleGPSUtils(getApplicationContext());
        gpsUtils.start();
    }

    // Helper method to initializes the progress bar.
    // Makes sure the progress bar is view-able, and that it's facing the right
    // way to make the animation make sense to the user.
    private boolean initProgressBar() {
        pb = (ProgressBar) findViewById(R.id.progressBar);
        // rotates pb when starting up so that the progress bar starts from the bottom of
        // the circle rather than from the side.
        Animation an = new RotateAnimation(0f, 0f, 0f, 0f);
        an.setFillAfter(true);
        pb.startAnimation(an);
        return true;
    }

    // Creates and starts a new immutableCountDownTimer object.
    private boolean createTimer() {
        timer = new CountDownTimer(countDownPeriod, 1) {
            @Override
            public void onTick(long millisUntilFinished) {
                currentTimeMillis = millisUntilFinished;
                countDownPeriod = millisUntilFinished;  // update how much time is left in the timer
                long seconds = millisUntilFinished / 1000;
                if (seconds == TIME_BUFFER) {
                    btnEnd.setEnabled(false);
                }
                int barVal = (int) (millisUntilFinished / 1000);  // update progress bar animation
                pb.setProgress(barVal);

                // Re-calculate the text display for the timer
                String hrs = String.format("%02d", seconds / 3600);
                seconds %= 3600;  // Strip off seconds that got converted to hours
                String mins = String.format("%02d", seconds / 60);
                seconds %= 60;  // Strip off seconds that got converted to minutes
                String secs = String.format("%02d", seconds % 60);
                txtTimer.setText(hrs + ":" + mins + ":" + secs);
            }

            @Override
            public void onFinish() {
                // Buzz to alert user
                Vibrator vib = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(500);

                // Notification to further alert user
                NotificationCompat.Builder builder =
                        new NotificationCompat.Builder(getApplicationContext())
                                .setSmallIcon(R.drawable.ic_done_white_24dp)
                                .setContentTitle("Time Running Out")
                                .setContentText("Enter password or alert will be sent out!");

                Intent notificationIntent = new Intent(getApplicationContext(), DangerActivity.class);
                PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(contentIntent);

                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(0, builder.build());

                // Ask for password
                timer.cancel();
                pb.setProgress(0);
                Toast.makeText(TripActivity.this, "Your Trip Has Ended", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(TripActivity.this, DangerActivity.class));
            }

        }.start();
        return true;
    }

    // Adds more time to the timer depending on how much has been selected in the timeOption
    // drop down menu.
    private boolean addToTimer() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PasswordActivity.class);
                String time = "90";
                if ((currentTimeMillis / 1000) < 90) {
                    time = String.format("%d", currentTimeMillis / 1000);
                }
                Log.d(TAG, String.format("%d", currentTimeMillis));
                String numChances = "3";
                String confirmButtonMessage = "Extend timer";
                i.putExtra("passwordParams", new ArrayList<String>(Arrays.asList(time, numChances, confirmButtonMessage)));
                startActivityForResult(i, ADD_TIME_PASSWORD_REQUEST);
            }

        });
        return true;
    }

    // Ends the timer for the trip, taking the user automatically to the arrival screen.
    private boolean endTimer() {
        btnEnd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PasswordActivity.class);
                String time = "90";
                if ((currentTimeMillis / 1000) < 90) {
                    time = String.format("%d", currentTimeMillis / 1000);
                }
                Log.d(TAG, String.format("%d", currentTimeMillis));
                String numChances = "3";
                String confirmButtonMessage = "End Trip";
                i.putExtra("passwordParams", new ArrayList<String>(Arrays.asList(time, numChances, confirmButtonMessage)));
                startActivityForResult(i, END_TRIP_PASSWORD_REQUEST);
            }
        });
        return true;
    }

    /**
     * Converts a string in the format "[int] [time unit]" to milliseconds to be
     * fed into a timer.
     *
     * @param time the string to be parsed
     * @return -1 if the string was not in the correct format, or a positive number if
     * it was correctly parsed.
     * @requires time to be in the correct format of a number followed by a space followed
     * by a unit of time: "sec" for seconds, "min" for minutes, and "hr" for hours.
     */
    public static long parseTimeString(String time) {
        long millis = 0;
        if (time == null)
            return -1;
        String tokens[] = time.split(" ");
        if (tokens.length != 2)
            return -1;
        try {
            millis = Long.parseLong(tokens[0]);  // throws an exception if this doesn't work
            switch (tokens[1]) {
                case "sec": millis *= 1000;
                            break;
                case "min": millis *= (1000 * 60);
                            break;
                case "hr":  millis *= (1000 * 60 * 60);
                            break;
                default:    return -1;  // invalid time unit
            }
        } catch (NumberFormatException e) {
            return -1;
        }
        return millis;
    }

    // Helper method to populate the drop down menu with options to pick from
    // These times must be in the format [number] [time unit (sec, min, hr)] or
    // else parseTimeString cannot read them as proper amounts of time.
    private void populateTimeOptions() {
        timeOptions = (Spinner) findViewById(R.id.timeOptions);
        List<String> list = new ArrayList<String>();
        list.add("1 min");
        list.add("5 min");
        list.add("10 min");
        list.add("30 min");
        list.add("1 hr");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(R.layout.spinner_drop_down);
        timeOptions.setAdapter(dataAdapter);
    }

    /**
     * Callback method that receives information from PasswordActivity based on
     *
     * @param requestCode Identifying code for the event that requested the password screen.
     * @param resultCode  Determines the success or failure
     * @param data        Intent in which the result is stored
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data.getExtras().containsKey("retval")) {
            Serializable retcode = data.getExtras().getSerializable("retval");

            if (requestCode == END_TRIP_PASSWORD_REQUEST) {
                // Triggered from end trip
                Log.d(TAG, "Password activity call back from end trip");

                if (retcode.equals(PasswordActivity.RetCode.SUCCESS)) {
                    timer.cancel();
                    Intent intent = new Intent(getApplicationContext(), ArrivalActivity.class);
                    intent.putExtra("AlertType", "HOMESAFE");
                    startActivity(intent);
                } else if (retcode.equals(PasswordActivity.RetCode.FAILURE)) {
                    startActivity(new Intent(TripActivity.this, DangerActivity.class));
                } else if (retcode.equals(PasswordActivity.RetCode.SPECIAL)) {
                    timer.cancel();
                    Intent intent = new Intent(getApplicationContext(), ArrivalActivity.class);
                    intent.putExtra("AlertType", "DANGER");
                    startActivity(intent);
                }
                // Else Cancel was pressed, do nothing
            } else {
                // Triggered from extend timer
                Log.d(TAG, "Password activity call back from extend timer");

                if (retcode == PasswordActivity.RetCode.SUCCESS) {
                    // We'll extend the timer.
                    // Each timer object is immutable so we must cancel the old one to create
                    // a new timer object with more time in it.
                    if (timer != null)
                        timer.cancel();

                    // Get the selected time in drop-down menu
                    String selectedTime = timeOptions.getSelectedItem().toString();
                    countDownPeriod += parseTimeString(selectedTime);
                    // The maximum capacity for the progress bar must be increased
                    pb.setMax((int) (countDownPeriod / 1000));
                    createTimer();
                    Toast.makeText(TripActivity.this, "Added " + selectedTime,
                            Toast.LENGTH_SHORT).show();
                } else if (retcode == PasswordActivity.RetCode.SPECIAL) {
                    Location lastLocation = gpsUtils.getLastLocation();
                    if (lastLocation == null) {
                        // Fail fast. Location shouldn't be null.
                        Log.e(TAG, "Location was null when retrieved from GoogleGPSUtils");
                        return;
                    }
                    Messenger.sendNotifications(Contacts.Tier.ONE, lastLocation,
                            getApplicationContext(), Messenger.MessageType.DANGER);

                    // Extend timer
                    // Each timer object is immutable so we must cancel the old one to create
                    // a new timer object with more time in it.
                    if (timer != null)
                        timer.cancel();

                    // the selected time in the drop down menu
                    String selectedTime = timeOptions.getSelectedItem().toString();
                    countDownPeriod += parseTimeString(selectedTime);
                    // The maximum capacity for the progress bar must be increased
                    pb.setMax((int) (countDownPeriod / 1000));
                    createTimer();
                    Toast.makeText(TripActivity.this, "Added " + selectedTime,
                            Toast.LENGTH_SHORT).show();
                }
                // Else Cancel was pressed or wrong password was input, do nothing
            }
        }
    }

    @Override
    protected void onStop() {
        gpsUtils.disconnect();
    }
}