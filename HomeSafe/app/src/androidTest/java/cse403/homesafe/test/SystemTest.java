package cse403.homesafe.test;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import cse403.homesafe.R;
import cse403.homesafe.StartActivity;

/**
 * Created by dliuxy94 on 5/14/15.
 */
public class SystemTest extends ActivityInstrumentationTestCase2<StartActivity> {
    private Solo solo;
    public SystemTest() {
        super(StartActivity.class);
    }

    public static final String PASSWORD = "1111";
    public static final String SPECIAL_PASSWORD = "2222";

    protected void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
        setActivityInitialTouchMode(false);
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }

    //weird name but sure this will get executed first
    public void testAASetupAccount(){
        Context context = getActivity();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString("firstName", "TestFirstName").apply();
        prefs.edit().putString("lastName", "TestLastName").apply();
        prefs.edit().putString("pin", PASSWORD).apply();
        prefs.edit().putString("pin_mock", SPECIAL_PASSWORD).apply();
    }

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
        solo.pressSpinnerItem(0, 2);
        solo.clickOnButton("Add More Time");


        // wait to switch screens
        synchronized (lock) {
            lock.wait(500);
        }
        solo.typeText(0, PASSWORD);
        solo.clickOnButton(0);
        // success
    }

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
        // success
    }

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