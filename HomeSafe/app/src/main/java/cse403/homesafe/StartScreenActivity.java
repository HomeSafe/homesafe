package cse403.homesafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import cse403.homesafe.Data.Contacts;
import cse403.homesafe.Data.DbFactory;
import cse403.homesafe.Data.Destinations;
import cse403.homesafe.Data.HomeSafeDbHelper;
import cse403.homesafe.Settings.SettingsActivity;
import cse403.homesafe.Util.ContextHolder;

//This class is for Start Screen Activity, where it handles the side bar menu and start trip events
public class StartScreenActivity extends ActionBarActivity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayAdapter<String> mAdapter;
    private Button buttonStart;
    private HomeSafeDbHelper mDbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (!isAccountSetUp()) {
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

        buttonStart = (Button)findViewById(R.id.button);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.navList);

        addDrawerItems();
        setupDrawer();
        setupButton();

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        ContextHolder.setContext(getApplicationContext());
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
                } else if(position == 1){
                    i = new Intent(StartScreenActivity.this, FavLocationsActivity.class);
                } else {
                    i = new Intent(StartScreenActivity.this, SettingsActivity.class);
                }
                startActivity(i);
            }
        });
    }

    //check if personal information of homesafe user is set up
    private boolean isAccountSetUp() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
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
}
