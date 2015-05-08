import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import cse403.homesafe.Data.Contact;
import cse403.homesafe.Data.Contacts;
import cse403.homesafe.Messaging.SMS;
import android.location.Location;

/**
 * Created by dliuxy94 on 5/5/15.
 */
public class SMSTest extends TestCase {

    private SMS sms;
    private Contact recipient;
    private Location location;
    private String customMessage;

    @Before
    public void setUp() {
        sms = SMS.getInstance();
        recipient = new Contact("Foo", null, "4252812879", Contacts.Tier.ONE);
        location = new Location("CSE");
    }

    @Test
    public void testSendTextMessage() {
        sms.sendMessage(recipient, location, customMessage);
    }
}
