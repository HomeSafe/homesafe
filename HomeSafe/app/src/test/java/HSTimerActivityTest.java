import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import cse403.homesafe.HSTimerActivity;

/**
 * Created by dliuxy94 on 5/12/15.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(HSTimerActivity.class)
public class HSTimerActivityTest {

    HSTimerActivity timerActivity;

    @Before
    public void setUp() {
        timerActivity = null;
    }

    @Test
    public void testCreation() {
        timerActivity = null;
        timerActivity = new HSTimerActivity();
        Assert.assertNotNull(timerActivity);
    }

    @Test
    public void testInitProgressBar() throws Exception {
        timerActivity = PowerMockito.spy(new HSTimerActivity());
        PowerMockito.doNothing().when(timerActivity); //.initProgressBar(); // "initProgressBar", new Object[0]);
        PowerMockito.verifyPrivate(timerActivity).invoke("initProgressBar", new Object[0]);
    }

    @Test
    public void testCreateTimer() throws Exception {
        timerActivity = PowerMockito.spy(new HSTimerActivity());
        PowerMockito.doNothing().when(timerActivity, "createTimer", new Object[0]);
        PowerMockito.verifyPrivate(timerActivity).invoke("createTimer", new Object[0]);
    }
}
