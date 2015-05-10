package cse403.homesafe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.EditText;
import android.location.Location;
import android.widget.TimePicker;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import cse403.homesafe.Data.Contacts;
import cse403.homesafe.Data.SecurityData;
import cse403.homesafe.Messaging.Messenger;

/**
 * Holds methods to perform various activities during the trip.
 */
public class Trip extends ActionBarActivity {

    private Location endLocation;
    private HSTimer timer;
    private boolean arrived;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip);
        this.endLocation = null;
        this.timer = new HSTimer(0, this);
        this.arrived = false;
        promptForExtendTime();
    }

    /* This should be called when the timer elapses. */
    public void timerEndAction() {
        promptForPassword();
    }

    /**
     * Prompt user for password. Send entered password to appropriate callback.
     */
    private void promptForPassword() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Unlock");
        alert.setMessage("Please Enter Password");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        input.setBackgroundColor(0xFFAAAAAA);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String enteredPassword = input.getText().toString();
                if (SecurityData.getInstance().checkPwdRegular(enteredPassword)) {
                    promptForExtendTime();
                } else {
                    promptForPassword();
                }
            }
        });

        alert.show();
    }

    /**
     * Prompt user to input a time to extend trip for.
     * Timer will be extended this amount of time.
     */
    private void promptForExtendTime() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Trip Time");
        alert.setMessage("How much longer do you need?");

        // Set an EditText view to get user input
        final TimePicker input = new TimePicker(this);
        input.setBackgroundColor(0xFFAAAAAA);
        input.setIs24HourView(true);
        input.setCurrentHour(0);
        input.setCurrentMinute(0);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                int minutes = input.getCurrentMinute();
                int hours = input.getCurrentHour();
                timer.extendTimer(1000 * (60 * minutes + 3600 * hours));
                if (!timer.hasStarted()) {
                    timer.startTimer();
                }
            }
        });

        alert.show();
    }
}
