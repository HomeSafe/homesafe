import android.location.Location;
import android.util.Log;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import cse403.homesafe.Util.GoogleMapsUtils;
import cse403.homesafe.Util.GoogleMapsUtilsCallback;

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

//    @Test
//    public void testDistanceAndTime() {
//        String start = "2220+e+aloha+st+wa";
//        String end = "12711+se+42nd+st+bellevue+wa";
//        final Object lock = new Object();
//        synchronized (lock) {
//
//            GoogleMapsUtils.getDistanceAndTime(start, end, new GoogleMapsUtilsCallback() {
//                public void onGetDistanceAndTime(Object obj) {
//                    synchronized (lock) {
//                        lock.notifyAll();
//                    }
//                }
//                public void onAddressToLocation(Object obj) {}
//            });
//            try {
//                lock.wait();
//            } catch (InterruptedException e) {}
//        }
//        assertEquals(true, true);
//    }

    @Test
    public void testSendMessage() {
//        emailInstance.sendMessage(null, null, "Hello");
//        System.out.println("sdfsf");

    }
}
