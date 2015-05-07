package cse403.homesafe;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

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

    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    HomeSafeDbHelper mDbHelper;
    CharSequence Titles[]={"Tier 1","Tier 2", "Tier 3"};
    int Numboftabs = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        mDbHelper = new HomeSafeDbHelper(this);

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        List<Contact> contactList = retrieveFromDb();
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contacts, menu);
        return true;
    }

    private List<Contact> retrieveFromDb() {
        List<Contact> contactList = new ArrayList<Contact>();

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                ContactEntry._ID,
                ContactEntry.COLUMN_NAME,
                ContactEntry.COLUMN_EMAIL,
                ContactEntry.COLUMN_PHONE,
                ContactEntry.COLUMN_TIER,
        };

        Cursor c = db.query(
                ContactEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        while(!c.isLast()) {
            String name = c.getString(c.getColumnIndexOrThrow(ContactEntry.COLUMN_NAME));
            String email = c.getString(c.getColumnIndexOrThrow(ContactEntry.COLUMN_EMAIL));
            String phone = c.getString(c.getColumnIndexOrThrow(ContactEntry.COLUMN_PHONE));
            String TierString = c.getString(c.getColumnIndexOrThrow(ContactEntry.COLUMN_TIER));
            Contacts.Tier tier = Contacts.Tier.valueOf(TierString);
            contactList.add(new Contact(name, email, phone, tier));
        }

        return contactList;

    }

    private boolean saveContactToDb(Contact contact) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ContactEntry.COLUMN_NAME, contact.getName());
        values.put(ContactEntry.COLUMN_EMAIL, contact.getEmail());
        values.put(ContactEntry.COLUMN_PHONE, contact.getPhoneNumber());
        values.put(ContactEntry.COLUMN_TIER, contact.getTier().name());

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                ContactEntry.TABLE_NAME,
                null,
                values);
        return true;
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
