package cse403.homesafe.test;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import cse403.homesafe.R;
import cse403.homesafe.StartActivity;

/**
 * This class is the Black-box UI test for Homesafe app
 * It covers most of the use cases, find more comments in the class body
 * Created by dliuxy94 on 5/14/15.
 */
public class SystemTest extends ActivityInstrumentationTestCase2<StartActivity> {
    private Solo solo;  //robotium test util
    public SystemTest() {
        super(StartActivity.class);
    } //test starts from start activity

    public static final String PASSWORD = "1111";
    public static final String SPECIAL_PASSWORD = "2222";

    //set up the test utilities
    protected void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
        setActivityInitialTouchMode(false);
    }

    //clean up the resources and actvities
    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }

    //It has a weird name but this will make sure it gets executed first
    //inject settings at fresh install so that the system knows the pw to check against
    public void testAASetupAccount(){
        Context context = getActivity();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString("firstName", "TestFirstName").apply();
        prefs.edit().putString("lastName", "TestLastName").apply();
        prefs.edit().putString("pin", PASSWORD).apply();
        prefs.edit().putString("pin_mock", SPECIAL_PASSWORD).apply();
    }

    //test for setting a trip's destination and time
    public void testStartTripSettingActivity() throws Exception {
        solo.clickOnButton("Start Trip");
        solo.pressSpinnerItem(0, 1);
        Object lock = new Object();

        // wait for request to finish
        synchronized (lock) {
            lock.wait(500);
        }

        // on emulator, this bypasses the problem where location tracking doesnt work,
        // so we can't estimate time.
        solo.setTimePicker(0, 0, 1);
        solo.clickOnButton("Start Trip");

        // wait to switch screens
        synchronized (lock) {
            lock.wait(500);
        }
        solo.clickOnButton("End Trip");
    }

    //test for extend the timer during a trip
    public void testExtendTimeActivity() throws Exception {
        solo.clickOnButton("Start Trip");
        solo.pressSpinnerItem(0, 1);
        Object lock = new Object();

        // wait for request to finish
        synchronized (lock) {
            lock.wait(500);
        }

        // on emulator, this bypasses the problem where location tracking doesnt work,
        // so we can't estimate time.
        solo.setTimePicker(0, 0, 1);
        solo.clickOnButton("Start Trip");

        // wait to switch screens
        synchronized (lock) {
            lock.wait(500);
        }
        solo.clickOnButton("Add More Time");
        solo.typeText(0, PASSWORD);
        solo.clickOnButton(0);

        solo.setTimePicker(0, 0, 1);
        solo.clickOnButton("OK");

        // wait to switch screens
        synchronized (lock) {
            lock.wait(500);
        }

        assertTrue(solo.waitForText("Added 0 hours and 1 minute", 1, 3000));
        // success
    }

    //test for special password utility
    public void testSecretPasswordEndTrip() throws Exception {
        solo.clickOnButton("Start Trip");
        solo.pressSpinnerItem(0, 1);
        Object lock = new Object();

        // wait for request to finish
        synchronized (lock) {
            lock.wait(500);
        }

        // on emulator, this bypasses the problem where location tracking doesnt work,
        // so we can't estimate time.
        solo.setTimePicker(0, 0, 1);
        solo.clickOnButton("Start Trip");

        // wait to switch screens
        synchronized (lock) {
            lock.wait(500);
        }
        solo.clickOnButton("End Trip");
        // wait to switch screens
        synchronized (lock) {
            lock.wait(500);
        }
        solo.typeText(0, SPECIAL_PASSWORD);
        solo.clickOnButton(0);

        synchronized (lock) {
            lock.wait(500);
        }

        assertTrue(solo.waitForText("testName has been notified", 1, 3000));
        // success
    }

    //test for adding an emergency contact
    public void testAddContact() throws Exception {
        solo.clickOnActionBarHomeButton();
        solo.clickOnMenuItem("Emergency Contacts");
        Object lock = new Object();

        // wait for request to finish
        synchronized (lock) {
            lock.wait(500);
        }

        solo.clickOnView(solo.getView(R.id.contacts_fab)); // floatingActionButton
        synchronized (lock) {
            lock.wait(500);
        }
        solo.typeText(0, "testName");
        solo.typeText(1, "9999999999");
        solo.typeText(2, "test@example.com");
        solo.clickOnButton("Save");
    }

    //test for adding a location
    public void testAddDestination() throws Exception {
        solo.clickOnActionBarHomeButton();
        solo.clickOnMenuItem("Favorite Locations");
        Object lock = new Object();

        synchronized (lock) {
            lock.wait(500);
        }

        solo.clickOnView(solo.getView(R.id.location_fab));
        synchronized(lock) {
            lock.wait(500);
        }

        solo.typeText(0, "UW CSE");
        solo.typeText(1, "185 Stevens Way");
        solo.typeText(2, "Seattle");
        solo.typeText(3, "WA");
        solo.clickOnButton("Save");
    }
}