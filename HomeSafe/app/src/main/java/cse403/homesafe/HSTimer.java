package cse403.homesafe;

import android.os.CountDownTimer;

import java.util.concurrent.TimeUnit;

import cse403.homesafe.Data.Contacts;

/**
 * A HSTimer object that represents a count down timer.
 */
public class HSTimer {

    public static final long COUNT_DOWN_INTERVAL = 1000;

    private CountDownTimer timer;

    /**
     * Constructs a new HSTimer object.
     *
     * @param estimatedTimeArrival :
     */
    public HSTimer(long estimatedTimeArrival) {
       timer = setTimer(estimatedTimeArrival);
    }

    /**
     * Starts the timer.
     *
     * @return true if timer successfully started otherwise false.
     */
    public boolean startTimer() {
        timer.start();
        return timer != null;
    }

    /**
     * Ends the timer.
     *
     * @return true if the timer successfully ended otherwise false.
     */
    public boolean endTimer() {
        timer.onFinish();
        return timer == null;
    }

    /**
     * Extends the timer.
     *
     * @return true if the timer has been extended otherwise false.
     */
    public boolean extendTimer(long extraTime) {
        // timer = setTimer(Long.parseLong(myTextField) * 1000 + extraTime);
        return timer != null;
    }

    /**
     * Switches tier level.
     *
     * @param tier : the tier level.
     * @return true if tier has been swtiched otherwise false.
     */
    public boolean switchTier(int tier) {
        return true;
    }

    /**
     * Returns a CountDownTimer that is set to a specified time.
     *
     * @param estimatedTimeArrival : the starting time of the timer.
     * @return a CountDownTimer object set to the specified time.
     */
    private CountDownTimer setTimer(long estimatedTimeArrival) {
        CountDownTimer newTimer = new CountDownTimer(estimatedTimeArrival, COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;
                String hms =  String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                        TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MICROSECONDS.toMinutes(millis)));
                System.out.println(hms);
                /**
                 * Waiting for XML fields
                 * myTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                 */
            }

            @Override
            public void onFinish() {
                /**
                 * Waiting for XML fields
                 * Reset Timer if User does not enter password
                 *
                 */
            }
        };
        return newTimer;
    }

}
