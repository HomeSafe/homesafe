//package cse403.homesafe.test;
//
//import android.content.Intent;
//import android.test.ActivityUnitTestCase;
//import android.test.suitebuilder.annotation.MediumTest;
//
//import cse403.homesafe.StartScreenActivity;
//
//import android.util.Log;
//
//import cse403.homesafe.Util.GoogleMapsUtils;
//import cse403.homesafe.Util.GoogleMapsUtilsCallback;
//
//
///**
// * Test case for StartScreenActivity
// */
//public class StartScreenActivityTest extends ActivityUnitTestCase<StartScreenActivity> {
//
//    private StartScreenActivity startScreenActivity;
//    private Intent startIntent;
//
//    public StartScreenActivityTest() {
//        super(StartScreenActivity.class);
//    }
//
//    @Override
//    protected void setUp() throws Exception {
//        super.setUp();
//        startScreenActivity = getActivity();
//        startIntent = new Intent(Intent.ACTION_MAIN);
//    }
//
//    @MediumTest
//    public void testAddressToLocation() throws InterruptedException{
//        Log.i("StartScreenActivityTest","Starting testAddressToLocation");
//        String input = "2220_e_aloha_st_wa";
//        final Object lock = new Object();
//        synchronized (lock) {
//
//            GoogleMapsUtils.addressToLocation(input, new GoogleMapsUtilsCallback() {
//                @Override
//                public void callback(Object obj) {
//                    Log.d("StartScreenActivityTest", obj.toString());
//                    synchronized (lock) {
//                        lock.notifyAll();
//                    }
//                }
//            });
//            lock.wait();
//        }
//        assertEquals(true, true);
//    }
//
//    @MediumTest
//    public void testPreconditions() {
//        assertEquals(true, false);
//    }
//
//    @MediumTest
//    public void testRandom() {
//        assertEquals(1, 0);
//        assertEquals(true, false);
//    }
//
//}
