package cse403.homesafe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.widget.EditText;

import cse403.homesafe.Data.Contact;
import cse403.homesafe.Data.Contacts;
import cse403.homesafe.Messaging.Email;
import cse403.homesafe.Messaging.Messenger;

/**
 * This activity displays a password prompt to the user.
 * If the user fails to correctly enter the password within
 * some amount of time then the phone will contact the emergency
 * contacts.
 */
public class DangerActivity extends ActionBarActivity {

    private static final String TAG = "DangerActivity";
    private CountDownTimer timer;

    private int numAttempts;

    AlertDialog.Builder alertBuilder;

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
        alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setTitle("Enter Passcode");
        alertBuilder.setMessage("To Stop Alerts, Enter Correct Passcode");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        input.setBackgroundColor(0xFFAAAAAA);
        input.setTransformationMethod(PasswordTransformationMethod.getInstance());
        alertBuilder.setView(input);

        alertBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String enteredPassword = input.getText().toString();
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String password = preferences.getString("pin", null);

                if (password == null)
                    Log.e(TAG, "Password wasn't stored or accessed correctly");

                int pincode = Integer.parseInt(password);

                if (enteredPassword.equals(password)) {

                } else {
                    promptForPassword();
                }
            }
        });

        alertBuilder.show();
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

            }

        }.start();
    }
}
