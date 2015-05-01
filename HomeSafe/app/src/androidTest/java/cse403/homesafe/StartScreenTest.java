package cse403.homesafe;

import android.test.ActivityInstrumentationTestCase2;
import android.test.ActivityTestCase;
import android.widget.TextView;

/**
 * Test case for StartScreen
 */
public class StartScreenTest extends ActivityInstrumentationTestCase2<StartScreen> {

    private StartScreen startScreen;
    private TextView textView;

    public StartScreenTest() {
        super(StartScreen.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        startScreen = getActivity();
        textView =
                (TextView) startScreen
                        .findViewById(R.id.textView);
    }

    public void randomTest() {
        assertEquals(true, false);
    }

}
