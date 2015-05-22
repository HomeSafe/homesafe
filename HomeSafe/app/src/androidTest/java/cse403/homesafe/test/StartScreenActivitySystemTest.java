package cse403.homesafe.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.content.Context;
import android.content.SharedPreferences;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.UiThreadTest;
import android.test.ViewAsserts;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.robotium.solo.Solo;

import cse403.homesafe.HSTimerActivity;
import cse403.homesafe.R;
import cse403.homesafe.StartScreenActivity;
import cse403.homesafe.TripSettingActivity;

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

        SharedPreferences prefs = this.getActivity().getSharedPreferences("cse403.homesafe.app", Context.MODE_PRIVATE);
        String first = prefs.getString("cse403.homesafe.app.firstName", new String());
        prefs.edit().putString("cse403.homesafe.app.firstName", "Luyi").apply();
        prefs.edit().putString("cse403.homesafe.app.lastName", "Lu").apply();
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