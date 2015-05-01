package cse403.homesafe;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;


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

    }

    @MediumTest
    public void randomTest() {

        assertEquals(true, false);
    }

}
