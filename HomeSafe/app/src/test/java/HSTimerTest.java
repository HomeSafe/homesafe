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
    private Trip currentTrip;

    @Before
    public void setUp() {
        estimatedTimeArrival = 60000; // in milliseconds
        currentTrip = new Trip();
        hsTimer = new HSTimer(estimatedTimeArrival, currentTrip);
    }

    @Test
    public void someTest() {
        assertEquals(false, true);
    }
}
