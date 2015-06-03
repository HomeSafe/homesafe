import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLocationManager;

import cse403.homesafe.Util.DistanceAndTime;
import cse403.homesafe.Util.GoogleMapsUtils;
import cse403.homesafe.Util.GoogleMapsUtilsCallback;

/**
 * Unit test case for the various static methods in GoogleMapsUtils.
 */
@Config(manifest=Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class GoogleMapsUtilsTest extends TestCase {

    boolean condition;
    Location receivedLocation;
    DistanceAndTime distanceAndTime;

    @Before
    public void setUp() {
        condition = false;
        receivedLocation = null;
        distanceAndTime = null;
        System.out.println("SETUP");
    }

    /**
     * Tests that GoogleMapsUtils.addressToLocation() executes and -- given a valid address -- returns a
     * Location object with correct attributes.
     */
    @Test
    public void testAddressToLocation() {
        System.out.println("Starting testAddressToLocation");
        String input = "2220_e_aloha_st_wa";

        LocationManager locationManager = (LocationManager)
                Robolectric.application.getSystemService(Context.LOCATION_SERVICE);
        ShadowLocationManager shadowLocationManager = Robolectric.shadowOf(locationManager);

        GoogleMapsUtilsCallback instance = new GoogleMapsUtilsCallback() {

            @Override
            public void onGetDistanceAndTime(Object obj) {}

            @Override
            public void onAddressToLocation(Object obj) {
                if (obj instanceof Location) {
                    condition = true;
                    receivedLocation = (Location) obj;
                } else
                    condition = false;
            }
        };

        GoogleMapsUtils.addressToLocation(input, instance);
        try {
            Thread.sleep(2000);
            assertTrue(condition);
            assertTrue(receivedLocation != null);
            assertTrue(receivedLocation.getLatitude() > 47.0 && receivedLocation.getLongitude() < 48.0);
            assertTrue(receivedLocation.getLongitude() > -123.0 && receivedLocation.getLongitude() < -122.0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * Tests that GoogleMapsUtils.getDistanceAndTime() executes and -- given two valid locations --
     * returns a DistanceAndTime object with the correct attributes.
     */
    @Test
    public void testDistanceAndTime() {
        LocationManager locationManager = (LocationManager)
                Robolectric.application.getSystemService(Context.LOCATION_SERVICE);
        ShadowLocationManager shadowLocationManager = Robolectric.shadowOf(locationManager);

        Location start = location("start", 47.653811, -122.305912);
        Location end = location("end", 47.6269714, -122.3027312);

        // Ensure Location mocking is working properly
        assertTrue(start.getLatitude() != 0.0);

        GoogleMapsUtilsCallback instance = new GoogleMapsUtilsCallback() {
            @Override
            public void onGetDistanceAndTime(Object obj) {
                System.out.println("Starting onGetDistanceAndTime");
                if (obj instanceof DistanceAndTime) {
                    condition = true;
                    distanceAndTime = (DistanceAndTime) obj;
                    DistanceAndTime dAndT = (DistanceAndTime) obj;
                    System.out.println(dAndT.getDistance());
                    System.out.println(dAndT.getTime());
                }
            }

            @Override
            public void onAddressToLocation(Object obj) {}
        };

        GoogleMapsUtils.getDistanceAndTime(start, end, instance);
        try {
            Thread.sleep(2000);
            assertTrue(condition);
            assertTrue(distanceAndTime != null);
            assertEquals(distanceAndTime.getDistance(), 3281.0);
            assertEquals(distanceAndTime.getTime(), 2814.0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Constructs and returns a Location object with the specified parameters
    private Location location(String provider, double latitude, double longitude) {
        Location location = new Location(provider);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location.setTime(System.currentTimeMillis());
        return location;
    }
}
