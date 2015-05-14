import android.content.Context;
import android.location.Location;
import android.telephony.SmsManager;

import static org.mockito.Mockito.*;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.shadows.ShadowSmsManager;
import org.robolectric.Robolectric;

import cse403.homesafe.Data.Contact;
import cse403.homesafe.Data.Contacts;
import cse403.homesafe.Messaging.Messenger;
import cse403.homesafe.Utility.ContextHolder;

/**
 * Test class for Messenger.java
 */
public class MessengerTest extends TestCase {

    Context context;
    Contact testContact1;
    Contact testContact2;

    @Before
    public void setUp() {
        context = mock(Context.class);
        ContextHolder.setContext(context);

        testContact1 = new Contact("Alex", "jahrndez@uw.edu", "4259884882", Contacts.Tier.ONE);
        testContact2 = new Contact("John Doe", "jahs.herndez@gmail.com", "4259884882", Contacts.Tier.TWO);
    }

    @Test
    public void testSendNotifications() {
        Messenger.sendNotifications(Contacts.Tier.ONE, new Location("provider"), context, Messenger.MessageType.DANGER);
    }
}

