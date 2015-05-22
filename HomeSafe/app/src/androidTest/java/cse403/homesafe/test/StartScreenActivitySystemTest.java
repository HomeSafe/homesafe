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

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
        setActivityInitialTouchMode(false);

        SharedPreferences prefs = this.getActivity().getSharedPreferences("cse403.homesafe.app", Context.MODE_PRIVATE);
        String first = prefs.getString("cse403.homesafe.app.firstName", new String());
        prefs.edit().putString("cse403.homesafe.app.firstName", "Luyi").apply();
        prefs.edit().putString("cse403.homesafe.app.lastName", "Lu").apply();
    }

    public void testStartTripSettingActivity() throws Exception {
        solo.clickOnButton("Start Trip");
        solo.pressSpinnerItem(0, 1);
        solo.clickOnButton("Start Trip");
        solo.clickOnButton("End Trip");

    }
}