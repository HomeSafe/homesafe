package cse403.homesafe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.EditText;
import android.location.Location;

import cse403.homesafe.Data.Contacts;
import cse403.homesafe.Data.SecurityData;
import cse403.homesafe.Messaging.Messenger;

/**
 * Holds methods to perform various activities during the trip.
 */
public class Trip extends ActionBarActivity {

    private int tier;
    private Location startLocation;
    private Location endLocation;
    private HSTimer timer;
    private boolean arrived;

    private String enteredPassword;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    /**
     * Constructor
     * @param endLocation   Destination location
     * @param delay         Estimated time to arrive at destination
     */
    public Trip(Location endLocation, long delay) {
        this.endLocation = null;
        timer = new HSTimer(delay, this);
        arrived = false;
        timerEndAction();
    }

    /**
     * Start trip using existing settings
     */
    public void startTrip() {
        timer.startTimer();
    }

    /**
     * End trip, program prompt for password to end trip.
     * @return  True if successfully ended trip
     */
    public boolean endTrip() {
        return timer.endTimer();
    }

    /**
     * Callback action for timer when time is up. Prompt for extend or end trip
     */
    public void timerEndAction() {
        //while (true) {
            boolean correct = verifyPassword();
            if (correct) {
                extendTime();
        //        break;
            }
        //}
    }

    /**
     * Extend time to arrive at destination
     */
    public void extendTime() {
        timer.extendTimer(promptForExtendTime());
    }

    /**
     * Actions to execute when user ignores timer. Notify contacts.
     */
    public void userIgnoredTimerAction() {

    }

    /**
     * Prompt user for password.
     * @return password entered by user
     */
    private String promptForPassword() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Unlock");
        alert.setMessage("Please Enter Password");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                enteredPassword = input.getText().toString();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                enteredPassword = null;
            }
        });

        alert.show();
        return enteredPassword;
    }

    /**
     * Verify password given against stored passwords
     * @return          True if given password matches stored password, false otherwise
     */
    private boolean verifyPassword() {
        String pw = promptForPassword();
        return SecurityData.getInstance().checkPwdRegular(pw);
    }

    /**
     * Prompt user to input a time to extend trip for.
     * @return  Time to extend trip for in milliseconds
     */
    private long promptForExtendTime() {
        return 0;
    }
}
