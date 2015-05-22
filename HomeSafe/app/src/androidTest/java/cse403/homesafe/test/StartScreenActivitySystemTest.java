package cse403.homesafe.test;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import cse403.homesafe.R;
import cse403.homesafe.StartScreenActivity;

/**
 * Created by dliuxy94 on 5/14/15.
 */
public class StartScreenActivitySystemTest extends ActivityInstrumentationTestCase2<StartScreenActivity> {
    private Solo solo;
    public StartScreenActivitySystemTest() {
        super(StartScreenActivity.class);
    }

    public static final String PASSWORD = "1111";
    public static final String SPECIAL_PASSWORD = "2222";

    protected void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
        setActivityInitialTouchMode(false);
        Context context = getActivity();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString("firstName", "TestFirstName").apply();
        prefs.edit().putString("lastName", "TestLastName").apply();
        prefs.edit().putString("pin", PASSWORD).apply();
        prefs.edit().putString("pin_mock", SPECIAL_PASSWORD).apply();
        solo.goBack();
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
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
}