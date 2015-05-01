package cse403.homesafe.test;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.SingleLaunchActivityTestCase;
import android.test.suitebuilder.annotation.MediumTest;

import cse403.homesafe.StartScreen;


/**
 * Test case for StartScreen
 */
public class StartScreenTest extends ActivityUnitTestCase<StartScreen> {

    private StartScreen startScreen;
    private Intent startIntent;

    public StartScreenTest() {
        super(StartScreen.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        startScreen = getActivity();
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
