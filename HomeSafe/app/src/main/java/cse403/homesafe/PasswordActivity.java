package cse403.homesafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cse403.homesafe.Data.Contacts;
import cse403.homesafe.Messaging.Messenger;

/**
 * Represents a timed password field where a user can type their password to gain extended
 * authority in the HomeSafe App.
 */
public class PasswordActivity extends ActionBarActivity {

    private static final String TAG = "PasswordActivity";
    // Enable for debugging purposes
    private static final boolean DEBUG = false;
    // under-the-hood components
    private CountDownTimer timer;   // how much time the user has left to put in correct password
    private int numchances;         // how many chances the user has to put in correct password

    // on screen components
    private Button cancel;
    private Button confirm;
    private EditText passwordField;         // where user inputs their password
    private TextView timerView;             // how much time is left to correctly input password

    /**
     * Represents the 3 possible user-interactions with the Password screen;
     * a user can either successfully enter their password, fail to put in their password, or
     * cancel the password screen.
     */
    public enum RetCode {
        SUCCESS,
        FAILURE,
        SPECIAL,
        CANCEL
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_password);

        // get the parameters from the previous activity
        ArrayList<String> params = getIntent().getStringArrayListExtra("passwordParams");
        int time = Integer.parseInt(params.get(0));
        numchances = Integer.parseInt(params.get(1));
        String confirmButtonMessage = params.get(2);
        boolean enableCancelButton = true;
        if (params.size() >= 4) {
            enableCancelButton = Boolean.parseBoolean(params.get(3));
        }

        // Make on-screen components visible.
        cancel = (Button) findViewById(R.id.btnCancel);
        confirm = (Button) findViewById(R.id.btnConfirm);
        passwordField = (EditText) findViewById(R.id.passwordField);
        InputFilter lengthFilter = new InputFilter.LengthFilter(4);
        passwordField.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        passwordField.setFilters(new InputFilter[]{lengthFilter});
        showKeyboard();
        timerView = (TextView) findViewById(R.id.timerView);

        // set the button message and the test message
        confirm.setText(confirmButtonMessage);

        cancel.setEnabled(enableCancelButton);

        timer = new CountDownTimer(time * 1000, 1) {
            @Override
            public void onTick(long l) {
                // re-calculate the text display for the timer
                long seconds = l / 1000;
                String hrs = String.format("%02d", seconds / 3600);
                seconds %= 3600;       // strip off seconds that got converted to hours
                String mins = String.format("%02d", seconds / 60);
                seconds %= 60;         // strip off seconds that got converted to minutes
                String secs = String.format("%02d", seconds % 60);
                timerView.setText(hrs + ":" + mins + ":" + secs);
            }

            @Override
            public void onFinish() {
                timer.cancel();
                Intent i = new Intent();
                i.putExtra("retval", RetCode.FAILURE);
                setResult(RESULT_OK, i);
                PasswordActivity.this.finish();
            }
        }.start();

        // make the enter key work the same as the "submit" button
        passwordField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int keyCode, KeyEvent event) {
                if (keyCode == EditorInfo.IME_ACTION_DONE) {
                    // Perform action on key press
                    confirm.performClick();
                    return true;
                }
                return false;
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {

            // Can only confirm if entered something in the password field
            // check that there is something in the password field, otherwise make a
            // toast saying you haven't entered a password yet.
            @Override
            public void onClick(View view) {
                String password = passwordField.getText().toString();   // the entered password
                Intent i = new Intent();
                if (password.length() == 0) {
                    Toast.makeText(PasswordActivity.this, "You have not entered a password", Toast.LENGTH_SHORT).show();
                } else {
                    if (comparePassword("pin", password)) {
                        Log.d(TAG, "Return code Success");
                        if (DEBUG)
                            Toast.makeText(PasswordActivity.this, "You entered correct password", Toast.LENGTH_SHORT).show();
                        i.putExtra("retval", RetCode.SUCCESS);
                        setResult(RESULT_OK, i);
                        finish();
                    } else if (comparePassword("pin_mock", password)) {
                        Log.d(TAG, "Return code Special");
                        if (DEBUG)
                            Toast.makeText(PasswordActivity.this, "You entered your emergency password!", Toast.LENGTH_SHORT).show();
                        i.putExtra("retval", RetCode.SPECIAL);
                        setResult(RESULT_OK, i);
                        finish();
                    } else {
                        Toast.makeText(PasswordActivity.this, "You entered an incorrect password", Toast.LENGTH_SHORT).show();
                        numchances--;
                        if (numchances <= 0) {
                            if (DEBUG)
                                Toast.makeText(PasswordActivity.this, "No more chances!", Toast.LENGTH_SHORT).show();
                            confirm.setEnabled(false);
                            Log.d(TAG, "Return code Special");
                            i.putExtra("retval", RetCode.FAILURE);
                            setResult(RESULT_OK, i);
                            finish();
                        }
                    }
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.putExtra("retval", RetCode.CANCEL);
                setResult(RESULT_OK, i);
                finish();
            }
        });

    }

    private void showKeyboard() {
        (new Handler()).postDelayed(new Runnable() {

            public void run() {
                EditText yourEditText= (EditText) findViewById(R.id.passwordField);
                yourEditText.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
                yourEditText.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP , 0, 0, 0));
            }
        }, 200);
    }

    /**
     * Private helper function to compare a password to the user's stored one
     * @param enteredPassword the password to compare against the stored one; cannot be null.
     * @return true if the two passwords match, false otherwise.
     */
    private boolean comparePassword(String key, String enteredPassword) {
        // get the user's stored data
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String pin = prefs.getString(key, null);
        return enteredPassword.equals(pin);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        i.putExtra("retval", RetCode.CANCEL);
        setResult(RESULT_OK, i);
        finish();
    }
}