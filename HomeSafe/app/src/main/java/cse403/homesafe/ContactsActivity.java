package cse403.homesafe;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import cse403.homesafe.ContactTabs.SlidingTabLayout;
import cse403.homesafe.ContactTabs.ViewPagerAdapter;
import cse403.homesafe.Data.Contact;
import cse403.homesafe.Data.Contacts;
import cse403.homesafe.Data.HomeSafeContract;
import cse403.homesafe.Data.HomeSafeDbHelper;

import static cse403.homesafe.Data.HomeSafeContract.*;

public class ContactsActivity extends ActionBarActivity {
    private final String TAG = "ContactsActivity";
    FloatingActionButton fab;

    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    HomeSafeDbHelper mDbHelper;
    Contacts mContactList;
    CharSequence Titles[]={"Tier 1","Tier 2", "Tier 3"};
    int Numboftabs = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        mDbHelper = new HomeSafeDbHelper(this);
        mContactList = mContactList.getInstance();
        // Creating The Toolbar and setting it as the Toolbar for the activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new ViewPagerAdapter(getSupportFragmentManager(), Titles, Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.accent);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);
        tabs.setElevation(8); //red is fine, only for api21

        fab = (FloatingActionButton) findViewById(R.id.contacts_fab);
        setUpFab();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contacts, menu);
        return true;
    }

    private void setUpFab(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ContactsActivity.this, AddContactActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
