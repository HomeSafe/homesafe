import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import cse403.homesafe.HSTimer;
import cse403.homesafe.Trip;

/**
 * Created by dliuxy94 on 5/12/15.
 */
public class HSTimerTest extends TestCase {

    private HSTimer hsTimer;
    private long estimatedTimeArrival;
    private TestTrip currentTrip;
    public int testCount;

    @Before
    public void setUp() {
        testCount = 0;
        estimatedTimeArrival = 60000; // in milliseconds
        currentTrip = new TestTrip(this);
        hsTimer = new HSTimer(estimatedTimeArrival, currentTrip);
        testCount += 1;
    }

    @Test
    public void testConstructor() {
        hsTimer = null;
        hsTimer = new HSTimer(estimatedTimeArrival, currentTrip);
        assertNotNull(hsTimer);
    }

    // ***************** Helper class ************************************

    public class TestTrip extends Trip {
        private HSTimerTest testObj;

        public TestTrip(HSTimerTest testObj) {
            super();
            this.testObj = testObj;
        }

        @Override
        public void timerEndAction() {
            testObj.testCount = testObj.testCount - 1;
        }
    }
}
