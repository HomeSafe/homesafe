package DataTest;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;


import cse403.homesafe.Data.Destination;
import cse403.homesafe.Data.Destinations;

/**
 * Created by Ethan on 5/19/15.
 */
@PrepareForTest(Destinations.class)
public class DestinationsTest {
    Destinations destinations;
    Destination uw;
    Destination cse;

    @Before
    public void setUp() {
        destinations = null;
        uw = new Destination("UW");
        uw.setDid(1);
        cse = new Destination("CSE");
        cse.setDid(2);
    }

    @Test
    public void testGetInstance() {
        destinations = Destinations.getInstance();
        Assert.assertNotNull(destinations);
    }

    @Test
    public void testClearDestinations() {
        destinations = Destinations.getInstance();
        destinations.addDestination(uw);
        destinations.addDestination(cse);
        destinations.clearDestinations();
        Assert.assertEquals(0, destinations.getSize());
    }

    @Test
    public void testAddDestination() {
        destinations = Destinations.getInstance();
        destinations.clearDestinations();
        Assert.assertTrue(destinations.addDestination(uw));
        Assert.assertEquals(1, destinations.getSize());
        destinations.addDestination(cse);
        Assert.assertEquals(2, destinations.getSize());
    }

    @Test
    public void testRemoveContact() {
        destinations = Destinations.getInstance();
        destinations.clearDestinations();
        destinations.addDestination(uw);
        destinations.addDestination(cse);
        Assert.assertEquals(2, destinations.getSize());
        destinations.removeDestination(1);
        Assert.assertEquals(1, destinations.getSize());
        destinations.removeDestination(2);
        Assert.assertEquals(0, destinations.getSize());
    }


}
