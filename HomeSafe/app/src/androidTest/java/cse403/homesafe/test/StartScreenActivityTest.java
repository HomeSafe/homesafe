package cse403.homesafe.test;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;

import cse403.homesafe.StartScreenActivity;

import android.util.Log;

import cse403.homesafe.Util.GoogleMapsUtils;
import cse403.homesafe.Util.GoogleMapsUtilsCallback;


/**
 * Test case for StartScreenActivity
 */
public class StartScreenActivityTest extends ActivityUnitTestCase<StartScreenActivity> {

    private StartScreenActivity startScreenActivity;
    private Intent startIntent;

    public StartScreenActivityTest() {
        super(StartScreenActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        startScreenActivity = getActivity();
        startIntent = new Intent(Intent.ACTION_MAIN);
    }

    @MediumTest
    public void testPreconditions() {
        assertEquals(true, false);
    }

    @MediumTest
    public void testRandom() {
        assertEquals(1, 0);
        assertEquals(true, false);
    }

}
