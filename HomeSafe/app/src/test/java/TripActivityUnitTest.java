import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import cse403.homesafe.TripActivity;

import static org.junit.Assert.assertEquals;

/**
 * Created by dliuxy94 on 5/12/15.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(TripActivity.class)
public class TripActivityUnitTest {

    TripActivity timerActivity;

    @Before
    public void setUp() {
        timerActivity = null;
    }

    @Test
    public void testCreation() {
        timerActivity = null;
        timerActivity = new TripActivity();
        Assert.assertNotNull(timerActivity);
    }

    @Test
    public void testInitProgressBar() throws Exception {
        timerActivity = PowerMockito.spy(new TripActivity());
        PowerMockito.when(timerActivity, "initProgressBar").thenReturn(true);
    }

    @Test
    public void testCreateTimer() throws Exception {
        timerActivity = PowerMockito.spy(new TripActivity());
        PowerMockito.when(timerActivity, "createTimer", new Object[0]).thenReturn(true);
    }

    @Test
    public void testAddToTimer() throws Exception {
        timerActivity = PowerMockito.spy(new TripActivity());
        PowerMockito.when(timerActivity, "addToTimer", new Object[0]).thenReturn(true);
    }

    @Test
    public void testEndTimer() throws Exception {
        timerActivity = PowerMockito.spy(new TripActivity());
        PowerMockito.when(timerActivity, "endTimer", new Object[0]).thenReturn(true);
    }

    @Test
    public void testAddItemsToTimeOptions() throws Exception {
        timerActivity = PowerMockito.spy(new TripActivity());
        PowerMockito.when(timerActivity, "addItemsToTimeOptions", new Object[0]).thenReturn(true);
    }

    @Test
    public void testPromptForPassword() throws Exception {
        timerActivity = PowerMockito.spy(new TripActivity());
        PowerMockito.when(timerActivity, "promptForPassword", new Object[0]).thenReturn(true);
    }

    @Test
    public void testBuildGoogleApiClient() throws Exception {
        timerActivity = PowerMockito.spy(new TripActivity());
        PowerMockito.when(timerActivity, "buildGoogleApiClient", new Object[0]).thenReturn(true);
    }

    // Static method parseTimeString tests
    @Test
    public void testParseTimeStringSec() throws Exception {
        assertEquals(new Long(TripActivity.parseTimeString("1000 sec")), new Long(1000000));
    }

    @Test
    public void testParseTimeStringMin() throws Exception {
        assertEquals(new Long(TripActivity.parseTimeString("10 min")), new Long(600000));
    }

    @Test
    public void testParseTimeStringHr() throws Exception {
        assertEquals(new Long(TripActivity.parseTimeString("2 hr")), new Long(7200000));
    }

    @Test
    public void testParseTimeStringError() throws Exception {
        assertEquals(new Long(TripActivity.parseTimeString("error")), new Long(-1));
    }
}
