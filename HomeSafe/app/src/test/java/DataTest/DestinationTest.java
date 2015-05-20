package DataTest;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;

import cse403.homesafe.Data.Contact;
import cse403.homesafe.Data.Destination;

/**
 * Created by Alex on 5/19/15.
 * Class to test Destination.java
 */
@PrepareForTest(Contact.class)
public class DestinationTest {
    Destination dest1;
    Destination dest2;
    Destination dest3;

    @Before
    public void setUp() { dest1 = null; dest2 = null; dest3 = null; }

    @Test
    public void testNameConstructor() {
        dest1 = new Destination("TestName");

        Assert.assertEquals("Dest name was not properly set", "TestName", dest1.getName());
        Assert.assertNull("Location should be null", dest1.getLocation());
        Assert.assertNull("Address should be null", dest1.getAddress());
        Assert.assertFalse("State should be NO_LOCATION", dest1.isReady());
    }

    /**
     * Tests that the two-argument constructor works and calculates a location from
     * the Google Maps API using a valid address.
     */
    @Test
    public void testNameAddressConstructorValid() {
        dest1 = new Destination("TestName", "2220 E Aloha St, Seattle, WA");  // Use real address

        Assert.assertTrue("Location should've been calculated", dest1.isReady());
        Assert.assertNotNull("Address shouldn't be null", dest1.getAddress());
        Assert.assertNotNull("Location should've been set", dest1.getLocation());
        Assert.assertEquals("Dest name was not properly set", "TestName", dest1.getName());
    }

    /**
     * Tests that the two-argument constructor works and calculates a location from
     * the Google Maps API using an invalid address.
     */
    @Test
    public void testNameAddressConstructorInvalid() {
        dest1 = new Destination("TestName", "Invalid address, should not be recognized by Google Maps API");

        Assert.assertFalse("Location shouldn't have been recognized", dest1.isReady());
        Assert.assertNotNull("Address shouldn't be null", dest1.getAddress());
        Assert.assertNotNull("Location should've been set", dest1.getLocation());
        Assert.assertEquals("Dest name was not properly set", "TestName", dest1.getName());
    }

    @After
    public void tearDown() { dest1 = null; dest2 = null; dest3 = null; }
}
