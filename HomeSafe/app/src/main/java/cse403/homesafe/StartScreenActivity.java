package cse403.homesafe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import cse403.homesafe.Data.Contacts;
import cse403.homesafe.Data.DbFactory;
import cse403.homesafe.Data.Destinations;
import cse403.homesafe.Data.HomeSafeDbHelper;
import cse403.homesafe.Messaging.Messenger;
import cse403.homesafe.Settings.SettingsActivity;
import cse403.homesafe.Util.ContextHolder;

//This class is for Start Screen Activity, where it handles the side bar menu and start trip events
public class StartScreenActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayAdapter<String> mAdapter;
    private Button buttonStart;
    private HomeSafeDbHelper mDbHelper;

    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = "StartScreenActivity";    // for logcat purposes
    private Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        buttonStart = (Button)findViewById(R.id.button);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.navList);

        setupDrawer();

        if (!isNetworkAvailable()) {
            TextView mainTxt = (TextView) findViewById(R.id.textView);
            mainTxt.setText("No Network Access");
            return;
        }

        if (!isAccountSetUp(getApplicationContext())) {
            Toast.makeText(getApplicationContext(),
                    "Set a first/last name and passcode.", Toast.LENGTH_LONG)
                    .show();
            Intent i = new Intent(StartScreenActivity.this, SettingsActivity.class);
            startActivity(i);
        }

        // Retrieve data from database
        Contacts.getInstance().clearContacts();
        Destinations.getInstance().clearDestinations();
        mDbHelper = new HomeSafeDbHelper(this);
        DbFactory.retrieveFromDb(mDbHelper);

        addDrawerItems();
        setupButton();

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        ContextHolder.setContext(getApplicationContext());
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
                //TODO hardcoded case, needs a switch to go to three different screens
                Intent i;
                if(position == 0){
                    i = new Intent(StartScreenActivity.this, ContactsActivity.class);
                    startActivity(i);
                } else if(position == 1){
                    i = new Intent(StartScreenActivity.this, FavLocationsActivity.class);
                    startActivity(i);
                } else {
                    // i = new Intent(StartScreenActivity.this, SettingsActivity.class);
                    i = new Intent(getApplicationContext(), PasswordActivity.class);
                    String time = "90";
                    String message = "Please enter your password to access settings";
                    String numChances = "3";
                    String confirmButtonMessage = "Enter";
                    i.putExtra("passwordParams", new ArrayList<String>(Arrays.asList(time, message, numChances, confirmButtonMessage)));
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
                Intent i = new Intent(StartScreenActivity.this, TripSettingActivity.class);
                startActivity(i);
            }
        });
    }

    //set up the drawer open and close
    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Menu");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle("HomeSafe");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
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

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    //*************** After returning from PasswordActivity *********************

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null && data.getExtras().containsKey("retval")) {
            Serializable retcode = data.getExtras().getSerializable("retval");
            if (retcode.equals(PasswordActivity.RetCode.SUCCESS)) {
                startActivity(new Intent(StartScreenActivity.this, SettingsActivity.class));
            } else if (retcode.equals(PasswordActivity.RetCode.SPECIAL)) {
                buildGoogleApiClient();
                onStart();
                startActivity(new Intent(StartScreenActivity.this, SettingsActivity.class));
            }
        }
    }

    /**
     * Starts the Google API Client
     */
    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            Log.e(TAG, "Connection Started");
            mGoogleApiClient.connect();
        }
    }

    /**
     * Builds the Google API Client
     */
    protected synchronized boolean buildGoogleApiClient() {
        boolean result = false;

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        if (mGoogleApiClient != null) {
            Log.e(TAG, "Build Complete");
            result = true;
        } else {
            Log.e(TAG, "Build Incomplete");
        }

        return result;
    }

    /**
     * Callback method of Google API Client if connected
     */
    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            Log.i(TAG, "Connected!");
            Messenger.sendNotifications(Contacts.Tier.ONE, mLastLocation, getApplicationContext(), Messenger.MessageType.DANGER);
            Toast.makeText(StartScreenActivity.this, "Contacts have been notified", Toast.LENGTH_SHORT).show();
        } else {
            Log.e(TAG, "Failed on getting last location");
        }
    }

    /**
     * Callback method for Google API Client if connection is suspended
     */
    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG, "Connection Suspended");
    }

    /**
     * Callback method for Google API Client if connection fails
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "Connection Failed");
    }
}
