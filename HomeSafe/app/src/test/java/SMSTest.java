import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.Robolectric;
import org.robolectric.shadows.ShadowSmsManager;

import cse403.homesafe.Data.Contact;
import cse403.homesafe.Data.Contacts;
import cse403.homesafe.Messaging.Messenger;
import cse403.homesafe.Messaging.SMS;

import android.content.Context;
import android.location.Location;
import android.telephony.SmsManager;

import static org.mockito.Mockito.*;

/**
 * Test suite for SMS.java using robolectric and mockito
 */
public class SMSTest extends TestCase {

    private SMS sms;
    private Contact recipient;
    private Location location;
    private String customMessage;
    Context context;

    @Before
    public void setUp() {
        context = mock(Context.class);

        sms = SMS.getInstance();
        recipient = new Contact("Foo", "placeholder", "4252812879", Contacts.Tier.ONE);
        location = new Location("CSE");
        customMessage = "Hello";
    }

    @Test
    public void testSendTextMessage() {

        sms.sendMessage(recipient, location, customMessage, context, Messenger.MessageType.DANGER);

        ShadowSmsManager shadowSmsManager = Robolectric.shadowOf(SmsManager.getDefault());
        ShadowSmsManager.TextSmsParams lastSentTextMessageParams = shadowSmsManager.getLastSentTextMessageParams();

        assertTrue(lastSentTextMessageParams.getDestinationAddress().equals("+14259884882"));
    }
}
