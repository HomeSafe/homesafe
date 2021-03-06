package cse403.homesafe;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import cse403.homesafe.Contacts.ContactsActivity;
import cse403.homesafe.Data.Contacts;
import cse403.homesafe.Data.DbFactory;
import cse403.homesafe.Data.Destinations;
import cse403.homesafe.Data.HomeSafeDbHelper;
import cse403.homesafe.Destinations.DestinationsActivity;
import cse403.homesafe.Messaging.Messenger;
import cse403.homesafe.Settings.SettingsActivity;
import cse403.homesafe.Util.GoogleGPSUtils;

//This class is for Start Screen Activity, where it handles the side bar menu and start trip events
public class StartActivity extends ActionBarActivity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayAdapter<String> mAdapter;
    private Button buttonStart;
    private HomeSafeDbHelper mDbHelper;

    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = "StartScreenActivity";    // for logcat purposes
    private Location mLastLocation;

    GoogleGPSUtils gpsUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        buttonStart = (Button)findViewById(R.id.button);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.navList);

        // Retrieve data from database
        Contacts.getInstance().clearContacts();
        Destinations.getInstance().clearDestinations();
        mDbHelper = new HomeSafeDbHelper(this);
        DbFactory.retrieveFromDb(mDbHelper);

        addDrawerItems();
        setupDrawer();
        setupButton();

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        gpsUtils = GoogleGPSUtils.getInstance(getApplicationContext());
        gpsUtils.start();
    }

    // return true if user can connect to internet
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //add the menu items to the drawer side bar
    private void addDrawerItems() {
        final String[] menuListItems = getResources().getStringArray(R.array.menu_array);
        mAdapter = new ArrayAdapter<String>(this, R.layout.drawer_list_item, menuListItems);
        mDrawerList.setAdapter(mAdapter);

        //set each up each menu item where it goes
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //A good example of how to make a toast
//            Toast.makeText(StartScreenActivity.this, menuListItems[position], Toast.LENGTH_SHORT).show();
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i;
                if (position == 0) {
                    i = new Intent(StartActivity.this, ContactsActivity.class);
                    startActivity(i);
                } else if (position == 1) {
                    i = new Intent(StartActivity.this, DestinationsActivity.class);
                    startActivity(i);
                } else {
                    // i = new Intent(StartScreenActivity.this, SettingsActivity.class);
                    i = new Intent(getApplicationContext(), PasswordActivity.class);
                    String time = "90";
                    String numChances = "3";
                    String confirmButtonMessage = "Enter";
                    i.putExtra("passwordParams", new ArrayList<String>(Arrays.asList(time, numChances, confirmButtonMessage, "")));
                    startActivityForResult(i, 1);
                }
            }
        });
    }

    //check if personal information of homesafe user is set up
    public static boolean isAccountSetUp(Context ctx) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        String password = preferences.getString("pin", null);
        String userFirstName = preferences.getString("firstName", null);
        String userLastName = preferences.getString("lastName", null);

        if (password == null || userFirstName == null || userLastName == null)
            return false;

        return userFirstName.length() * userLastName.length() * password.length() != 0;
    }

    //set up start trip button
    private void setupButton() {
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Contacts.getInstance().getNumContactInInstance() != 0) {
                    Intent i = new Intent(StartActivity.this, TripSettingActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(StartActivity.this, "Set up emergency contact before starting a trip", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //set up the drawer open and close
    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_start_screen, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch(item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }


    /**
     * Displays a dialog of instructions and explanations of HomeSafe app.
     */
    public void displayHelpDialog(MenuItem item) {
        AlertDialog.Builder alert = new AlertDialog.Builder(StartActivity.this);
        alert.setTitle("Help");
        alert.setMessage(R.string.startScreen_help);

        alert.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.show();
    }

    //*************** After returning from PasswordActivity *********************

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null && data.getExtras().containsKey("retval")) {
            Serializable retcode = data.getExtras().getSerializable("retval");
            if (retcode.equals(PasswordActivity.RetCode.SUCCESS)) {
                startActivity(new Intent(StartActivity.this, SettingsActivity.class));
            } else if (retcode.equals(PasswordActivity.RetCode.SPECIAL)) {
                Location lastLocation = gpsUtils.getLastLocation();
                if(gpsUtils.isReady() && lastLocation != null) {
                    Messenger.sendNotifications(Contacts.Tier.ONE, lastLocation, this, Messenger.MessageType.DANGER);
                    Messenger.sendNotifications(Contacts.Tier.TWO, lastLocation, this, Messenger.MessageType.DANGER);
                    Messenger.sendNotifications(Contacts.Tier.THREE, lastLocation, this, Messenger.MessageType.DANGER);
                } else {
                    Log.e(TAG, "Last known location is null.");
                }

                startActivity(new Intent(StartActivity.this, SettingsActivity.class));
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        TextView mainTxt = (TextView) findViewById(R.id.textView);
        if (!isNetworkAvailable()) {
            mainTxt.setText("No Network Access");
            buttonStart.setEnabled(false);
            mDrawerToggle.setDrawerIndicatorEnabled(false);
            return;
        } else {
            mainTxt.setText("Home Safe");
            buttonStart.setEnabled(true);
            mDrawerToggle.setDrawerIndicatorEnabled(true);

        }

        if (!isAccountSetUp(getApplicationContext())) {
            Toast.makeText(getApplicationContext(),
                    "Set a first/last name and passcode.", Toast.LENGTH_LONG)
                    .show();
            Intent i = new Intent(StartActivity.this, SettingsActivity.class);
            startActivity(i);
        }
    }
}
