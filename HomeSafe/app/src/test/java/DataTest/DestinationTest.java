package DataTest;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;

import cse403.homesafe.Data.Destination;

/**
 * Class to test Destination
 */
@PrepareForTest(Destination.class)
public class DestinationTest {
    Destination dest1;
    Destination dest2;
    Destination dest3;

    @Before
    public void setUp() { dest1 = null; dest2 = null; dest3 = null; }

    /**
     * test constructing a destination, ensure all fields filled correctly
     */

    @Test
    public void testNameConstructor() {
        dest1 = new Destination("TestName", 99);

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
        Assert.assertNull("Location should not have been set", dest1.getLocation()); // because address invalid
        Assert.assertEquals("Dest name was not properly set", "TestName", dest1.getName());
    }

    /**
     * Test getters and setters
     */

    @Test
    public void testSetAndGetDid() {
        dest1 = new Destination("name");
        dest1.setDid(5);
        Assert.assertEquals(dest1.getDid(), 5);
    }

    @Test
    public void testSetAndGetAddress() {
        dest1 = new Destination("name");
        dest1.setAddress("2220 E Aloha St, Seattle, WA");
        Assert.assertEquals(dest1.getAddress(), "2220 E Aloha St, Seattle, WA");
    }

    @Test
    public void testSetAndGetName() {
        dest1 = new Destination("nameOld");
        dest1.setName("nameNEW");
        Assert.assertEquals(dest1.getName(), "nameNEW");
    }

    @After
    public void tearDown() { dest1 = null; dest2 = null; dest3 = null; }
}
