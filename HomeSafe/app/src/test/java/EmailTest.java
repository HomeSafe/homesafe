import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import cse403.homesafe.Data.Contact;
import cse403.homesafe.Data.Contacts;
import cse403.homesafe.Messaging.Email;

import android.location.Location;

/**
 *
 */
public class EmailTest extends TestCase {

    Contact testRecipient;
    Location location;
    Email emailInstance;

    @Before
    public void setUp() {
        emailInstance = Email.getInstance();
        testRecipient = new Contact("Johnny", "spethous@gmail.com", "4259884882", Contacts.Tier.ONE);
        location = new Location("Google Maps");
    }

    @Test
    public void testSendMessage() {
        emailInstance.sendMessage(testRecipient, location, "Hello");

    }
}
