package DataTest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.util.List;

import cse403.homesafe.Data.Contact;
import cse403.homesafe.Data.Contacts;

/**
 *  This class tests the Contact class
 */
//@RunWith(PowerMockRunner.class)
@PrepareForTest(Contacts.class)
public class ContactsTest {
    Contacts contacts;

    @Before
    public void setUp() {
        contacts = null;
    }

    @Test
    public void testGetInstance() {
        contacts = Contacts.getInstance();
        Assert.assertNotNull(contacts);
    }

    @Test
    public void testAddContact() {
        Contact contact1 = new Contact("Amy");

        contacts = Contacts.getInstance();

        boolean result = contacts.addContact(contact1, Contacts.Tier.ONE);
        Assert.assertTrue(result);
    }

    @Test
    public void testRemoveContact() {
        Contact contact1 = new Contact("Amy");

        contact1.setCid(44);

        contacts = Contacts.getInstance();


        boolean result = contacts.addContact(contact1, Contacts.Tier.ONE);
        Assert.assertTrue(result);

        result = contacts.removeContact(44);
        Assert.assertTrue(result);
    }

    @Test
    public void testEditContact() {
        Contact contact1 = new Contact("Amy");

        contact1.setCid(44);

        contacts = Contacts.getInstance();

        boolean result = contacts.addContact(contact1, Contacts.Tier.ONE);
        Assert.assertTrue(result);

        contacts.editContact(44, "Bob", "9999999999", "test@email.com", Contacts.Tier.TWO);

        Assert.assertEquals(contact1.getName(), "Bob");
        Assert.assertEquals(contact1.getPhoneNumber(), "9999999999");
        Assert.assertEquals(contact1.getEmail(), "test@email.com");
        Assert.assertEquals(contact1.getTier(), Contacts.Tier.TWO);
    }

    @Test
    public void testGetContactsInTier() {
        Contact contact1 = new Contact("Amy");
        Contact contact2 = new Contact("Amy");
        Contact contact3 = new Contact("Amy");
        Contact contact4 = new Contact("Amy");

        contact1.setCid(40);
        contact2.setCid(50);
        contact3.setCid(60);
        contact4.setCid(45);

        contact1.setTier(Contacts.Tier.ONE);
        contact2.setTier(Contacts.Tier.TWO);
        contact3.setTier(Contacts.Tier.THREE);
        contact4.setTier(Contacts.Tier.ONE);

        contacts = Contacts.getInstance();

        boolean result = contacts.addContact(contact1, Contacts.Tier.ONE);
        Assert.assertTrue(result);
        result = contacts.addContact(contact2, Contacts.Tier.TWO);
        Assert.assertTrue(result);
        result = contacts.addContact(contact3, Contacts.Tier.THREE);
        Assert.assertTrue(result);
        result = contacts.addContact(contact4, Contacts.Tier.ONE);
        Assert.assertTrue(result);

        List<Contact> resultContacts = contacts.getContactsInTier(Contacts.Tier.ONE);

        // It should contain contact 1 and 4 because they are tier 1
        Assert.assertTrue(resultContacts.contains(contact1));
        Assert.assertTrue(resultContacts.contains(contact4));

        // It should not contain 2 and 3 because they are the wrong tier
        Assert.assertFalse(resultContacts.contains(contact2));
        Assert.assertFalse(resultContacts.contains(contact3));
    }

    @Test
    public void testClearContacts() {
        Contact contact1 = new Contact("Amy");
        Contact contact2 = new Contact("Amy");
        Contact contact3 = new Contact("Amy");
        Contact contact4 = new Contact("Amy");

        contact1.setCid(40);
        contact1.setCid(50);
        contact1.setCid(60);
        contact1.setCid(45);

        contacts = Contacts.getInstance();

        boolean result = contacts.addContact(contact1, Contacts.Tier.ONE);
        Assert.assertTrue(result);
        result = contacts.addContact(contact2, Contacts.Tier.TWO);
        Assert.assertTrue(result);
        result = contacts.addContact(contact3, Contacts.Tier.THREE);
        Assert.assertTrue(result);
        result = contacts.addContact(contact4, Contacts.Tier.ONE);
        Assert.assertTrue(result);

        contacts.clearContacts();

        List<Contact> resultContacts = contacts.getContactsInTier(Contacts.Tier.ONE);

        // It should contain none of these contacts since it has been cleared
        Assert.assertFalse(resultContacts.contains(contact1));
        Assert.assertFalse(resultContacts.contains(contact4));
        Assert.assertFalse(resultContacts.contains(contact2));
        Assert.assertFalse(resultContacts.contains(contact3));
    }
}
