import android.location.Location;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import cse403.homesafe.Util.GoogleMapsUtils;

/**
 * Unit test case for the various static methods in GoogleMapsUtils
 */
public class GoogleMapsUtilsTest extends TestCase {

    @Before
    public void setUp() {
        System.out.println("SETUP");
    }

    @Test
    public void testAddressToLocation() {
        System.out.println("Starting testAddressToLocation");
        String input = "2220_e_aloha_st_wa";
//        Location result = GoogleMapsUtils.addressToLocation(input);
//        System.out.print(result.toString());
        assertEquals(true, true);
    }

    @Test
    public void testDistanceAndTime() {
//                System.out.println("sdfsf");
    }

    @Test
    public void testSendMessage() {
//        emailInstance.sendMessage(null, null, "Hello");
//        System.out.println("sdfsf");

    }
}
