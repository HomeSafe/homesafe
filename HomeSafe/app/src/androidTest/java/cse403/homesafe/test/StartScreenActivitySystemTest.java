package cse403.homesafe.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.ViewAsserts;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cse403.homesafe.HSTimerActivity;
import cse403.homesafe.R;
import cse403.homesafe.StartScreenActivity;
import cse403.homesafe.TripSettingActivity;

/**
 * Created by dliuxy94 on 5/14/15.
 */
public class StartScreenActivitySystemTest extends ActivityInstrumentationTestCase2<StartScreenActivity> {
    private StartScreenActivity activity;

    public StartScreenActivitySystemTest() {
        super(StartScreenActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setActivityInitialTouchMode(false);
        activity = getActivity();
    }

    
    public void testStartTripSettingActivity() throws Exception {
        Instrumentation mInstrumentation = getInstrumentation();
        // add monitor to check for the second activity
        ActivityMonitor monitor =
                getInstrumentation().
                        addMonitor(TripSettingActivity.class.getName(), null, false);

        // find button and click it
        Button view = (Button) activity.findViewById(R.id.button);
//
        // TouchUtils handles the sync with the main thread internally
        TouchUtils.clickView(this, view);
//
//        // to click on a click, e.g., in a listview
//        // listView.getChildAt(0);
//
//        // wait 2 seconds for the start of the activity
        TripSettingActivity startedActivity = (TripSettingActivity) monitor
                .waitForActivityWithTimeout(2000);
        assertNotNull(startedActivity);

        Button startTripButton = (Button) startedActivity.findViewById(R.id.startTripButton);
        TouchUtils.clickView(this, startTripButton);

        getInstrumentation().removeMonitor(monitor);
        monitor = getInstrumentation().addMonitor(HSTimerActivity.class.getName(), null, false);

        HSTimerActivity currentActivity = (HSTimerActivity) monitor.waitForActivityWithTimeout(2000);

        Button endTripButton = (Button) currentActivity.findViewById(R.id.btnEnd);
        TouchUtils.clickView(this, endTripButton);

    }
}