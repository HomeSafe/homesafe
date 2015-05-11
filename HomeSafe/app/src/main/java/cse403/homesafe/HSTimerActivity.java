package cse403.homesafe;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class HSTimerActivity extends ActionBarActivity {

    private CountDownTimer timer;   // representation for a timer clock
    private long countDownPeriod;   // how much time is left in the timer

    // on-screen components
    private ProgressBar pb;         // the timer animation
    private Button btnAdd;          // the button for adding time
    private Button btnEnd;          // the button for ending the trip early
    private Spinner timeOptions;    // the different amounts of time that can be added to timer
    private TextView txtTimer;      // textual representation of the time left in timer

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hstimer);

        // creates the views for the on-screen components
        initProgressBar();
//        initMenuOptions();
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
        pb.setMax(30000 / 1000);
        countDownPeriod = 30000;
        startTimer();
    }

    /* Initializes the progress bar
    *
    *
    * */
    private void initProgressBar() {
        pb = (ProgressBar) findViewById(R.id.progressBar);

        // rotates pb when starting up so that the progress bar starts from the bottom of
        // the circle rather than from the side.
        Animation an = new RotateAnimation(0.0f, 90.0f, 250f, 273f);
        an.setFillAfter(true);
        pb.startAnimation(an);
    }

    /* Starts a new CountDownTimer object (since they are immutable, this is the only
     *  way to add more time).
     *  @param time is the amount of time you want to have this new CountDownTimer count
     *         down from.
     *  @effect creates a new CountDownTimer. The caller is responsible for canceling the
     *          old timer.
     * */
    private void startTimer() {
        timer = new CountDownTimer(countDownPeriod, 1) {
            @Override
            public void onTick(long millisUntilFinished) {
                // update how much time is left in the timer
                countDownPeriod = millisUntilFinished;

                // re-calculate the amount left in progress bar
                long seconds = millisUntilFinished / 1000;
//                int barVal= (int)(startingTime / 1000) - ((int)(seconds/3600*100)+(int)(seconds/60*100)+(int)(seconds%60));
//                int barVal = (int)(((double) millisUntilFinished) / startingTime * 100);
                int barVal = (int) (millisUntilFinished/ 1000);
                pb.setProgress(barVal);

                // re-calulate the text display for the timer
                String hrs = String.format("%02d", seconds / 3600);
                seconds %= 3600;       // strip off seconds that got converted to hours
                String mins = String.format("%02d", seconds / 60);
                seconds %= 60;         // strip off seconds that got converted to minutes
                String secs = String.format("%02d", seconds % 60);
                // format the textview to show the easily readable format
                txtTimer.setText(hrs + ":" + mins + ":" + secs);
            }

            @Override
            public void onFinish() {
                timer.cancel();
                pb.setProgress(0);
                Toast.makeText(HSTimerActivity.this, "Ended Trip", Toast.LENGTH_SHORT).show();
            }

        }.start();
    }

    // Attaches the listener for adding more time to the trip to 'btnAdd'
    private void addToTimer() {
        btnAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (timer != null) {
                    timer.cancel();
                }
                long addedTime = parseTimeString(timeOptions.getSelectedItem().toString());
                countDownPeriod += addedTime; // adding 10 sec to timer
                pb.setMax((int)(countDownPeriod / 1000));
                startTimer();
                Toast.makeText(HSTimerActivity.this, "Added " + timeOptions.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Attaches the listener for ending the trip to 'btnEnd'
    private void endTimer() {
        btnEnd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (timer != null) {
                    timer.cancel();
                    countDownPeriod = 0;
                    txtTimer.setText("00:00:00");
                    pb.setProgress(0);
                }
                Toast.makeText(HSTimerActivity.this, "Ended Trip", Toast.LENGTH_SHORT).show();
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
        if (time == null) {
            return -1;
        }
        String tokens[] = time.split(" ");
        if (tokens.length != 2) {
            return -1;
        }
        try {
            long millis = Long.parseLong(tokens[0]);     // throws an exception if this doesn't work
            switch (tokens[1]) {
                case "sec": millis *= 1000;
                    break;
                case "min": millis *= (1000 * 60);
                    break;
                case "hr": millis *= (1000 * 60 *60);
                    break;
                default: return -1;         // invalid time unit
            }
            return millis;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /* Populates the drop down menu with options to pick from
    *
    *
    * */
    public void addItemsToTimeOptions() {

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
}