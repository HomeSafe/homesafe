import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import cse403.homesafe.Messaging.Messenger;

/**
 * Test class for Messenger.java
 */
public class MessengerTest extends TestCase {

    @Before
    public void setUp() {

    }

    @Test
    public void testSendNotifications() {
        Messenger.sendNotifications();
        // assertEquals();
    }
}

