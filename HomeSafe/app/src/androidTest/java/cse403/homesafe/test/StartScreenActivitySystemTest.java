package cse403.homesafe.test;

import android.app.Activity;
import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.ViewAsserts;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
//
        // search for the textView
        Button favButton = (Button) startedActivity.findViewById(R.id.favoriteLocationButton);
        TouchUtils.clickView(this, favButton);

//        assertTrue(false);

//        // wait 2 seconds for the start of the activity
//        TripSettingActivity startedActivity = (TripSettingActivity) monitor
//                .waitForActivityWithTimeout(2000);
//        assertNotNull(startedActivity);
//
//        // check that the TextView is on the screen
//        ViewAsserts.assertOnScreen(startedActivity.getWindow().getDecorView(),
//                textView);
//        // validate the text on the TextView
//        assertEquals("Text incorrect", "Started", textView.getText().toString());
//
//        // press back and click again
//        this.sendKeys(KeyEvent.KEYCODE_BACK);
//
//        TouchUtils.clickView(this, view);
    }
}