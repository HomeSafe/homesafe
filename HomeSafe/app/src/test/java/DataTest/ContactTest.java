package DataTest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;

import cse403.homesafe.Data.Contact;
import cse403.homesafe.Data.Contacts;

/**
 *  This class tests the Contact class
 */
//@RunWith(PowerMockRunner.class)
@PrepareForTest(Contact.class)
public class ContactTest {
    Contact contact1;

    @Before
    public void setUp() {
        contact1 = null;
    }

    /**
     * Test constructors
     */

    @Test
    public void testSimpleConstructor() {
        contact1 = new Contact("name");
        Assert.assertNotNull(contact1);
    }

    @Test
    public void testSecondConstructor() {
        contact1 = new Contact("name", "1234567890", Contacts.Tier.ONE);
        Assert.assertNotNull(contact1);
    }

    @Test
    public void testThirdConstructor() {
        contact1 = new Contact("name", "test@example.com", "1234567890", Contacts.Tier.ONE);
        Assert.assertNotNull(contact1);
    }

    /**
    * Test getters and setters
     */

    @Test
    public void testSetAndGetCid() {
        contact1 = new Contact("name");
        contact1.setCid(5);
        Assert.assertEquals(contact1.getCid(), 5);
    }

    @Test
    public void testSetAndGetTier() {
        contact1 = new Contact("name");
        contact1.setTier(Contacts.Tier.TWO);
        Assert.assertEquals(contact1.getTier(), Contacts.Tier.TWO);
    }

    @Test
    public void testSetAndGetName() {
        contact1 = new Contact("nameOld");
        contact1.setName("nameNEW");
        Assert.assertEquals(contact1.getName(), "nameNEW");
    }

    @Test
    public void testSetAndGetEmail() {
        contact1 = new Contact("name", "old@example.com", "1234567890", Contacts.Tier.ONE);
        contact1.setEmail("new@newExample.com");
        Assert.assertEquals(contact1.getEmail(), "new@newExample.com");
    }

    @Test
    public void testSetAndGetPhoneNumber() {
        contact1 = new Contact("name", "old@example.com", "1234567890", Contacts.Tier.ONE);
        contact1.setPhoneNumber("9999999999");
        Assert.assertEquals(contact1.getPhoneNumber(), "9999999999");
    }

    /**
     * test toString
     */

    @Test
    public void testToString() {
        contact1 = new Contact("name", "old@example.com", "1234567890", Contacts.Tier.ONE);
        contact1.setCid(555);
        String result = contact1.toString();
        Assert.assertEquals(result, "[name (555) Tier: " + Contacts.Tier.ONE +
                " | old@example.com" +
                " | 1234567890]");
    }
}
