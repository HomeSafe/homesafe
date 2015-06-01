//package MessagingTest;
//
//import junit.framework.TestCase;
//
//import static org.mockito.Mockito.*;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.robolectric.shadows.ShadowSmsManager;
//import org.robolectric.Robolectric;
//
//import cse403.homesafe.Data.Contact;
//import cse403.homesafe.Data.Contacts;
//import cse403.homesafe.Messaging.Email;
//import cse403.homesafe.Messaging.Messenger;
//
//import android.content.Context;
//import android.location.Location;
//
///**
// *
// */
//public class EmailTest extends TestCase {
//
//    Context context;
//    Contact testRecipient;
//    Location location;
//    Email emailInstance;
//
//    @Before
//    public void setUp() {
//        context = mock(Context.class);
//        emailInstance = Email.getInstance();
//        testRecipient = new Contact("Johnny", "spethous@gmail.com", "4259884882", Contacts.Tier.ONE);
//        location = new Location("Google Maps");
//    }
//
//    @Test
//    public void testSendMessage() {
//        emailInstance.sendMessage(testRecipient, location, "Hello", context, Messenger.MessageType.DANGER);
//
//    }
//}
