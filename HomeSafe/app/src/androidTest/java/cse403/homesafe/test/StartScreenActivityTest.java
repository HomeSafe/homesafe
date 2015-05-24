package cse403.homesafe.test;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;

import cse403.homesafe.StartActivity;


/**
 * Test case for StartScreenActivity
 */
public class StartScreenActivityTest extends ActivityUnitTestCase<StartActivity> {

    private StartActivity startActivity;
    private Intent startIntent;

    public StartScreenActivityTest() {
        super(StartActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        startActivity = getActivity();
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
