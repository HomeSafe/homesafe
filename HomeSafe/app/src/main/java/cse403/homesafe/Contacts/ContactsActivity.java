package cse403.homesafe.Contacts;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.melnykov.fab.FloatingActionButton;

import cse403.homesafe.ContactTabs.SlidingTabLayout;
import cse403.homesafe.ContactTabs.ViewPagerAdapter;
import cse403.homesafe.Data.HomeSafeDbHelper;
import cse403.homesafe.R;

/**
 * This class is used as the main user interface for displaying saved contacts and
 * their information. ContactsActivity retrieves the three tiers of contacts and
 * displays them in their respective tabs.
 */
public class ContactsActivity extends ActionBarActivity {
    private FloatingActionButton fab;
    private ViewPager pager;
    private ViewPagerAdapter adapter;
    private SlidingTabLayout tabs;
    private HomeSafeDbHelper mDbHelper;
    private CharSequence Titles[]={"Tier 1","Tier 2", "Tier 3"};
    private int tabNum;

    // Number of tabs to display, equal to the number of tiers available.
    private static final int NUM_TABS = 3;

    public static final String TAB = "TAB";
    public static final String TAB_2 = "TAB2";
    public static final String TAB_3 = "TAB3";
    public static final String ACTIVITY = "ACTIVITY";
    public static final String ADD = "ADD";

    /**
     * Sets up the main components of the contacts user interface, including displaying
     * the contacts card view
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        mDbHelper = new HomeSafeDbHelper(this);

        // Creating The Toolbar and setting it as the Toolbar for the activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new ViewPagerAdapter(getSupportFragmentManager(), Titles, NUM_TABS);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assigning the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.fabColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);

        fab = (FloatingActionButton) findViewById(R.id.contacts_fab);
        setUpFab();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contacts, menu);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getIntent().getStringExtra(TAB) != null) {
            if (getIntent().getStringExtra(TAB).equals(TAB_2)) {
                pager.setCurrentItem(1, true);
            } else if (getIntent().getStringExtra(TAB).equals(TAB_3)) {
                pager.setCurrentItem(2, true);
            } else {
                pager.setCurrentItem(0, true);
            }
        }
    }

    // Floating add button navigates to add contact activity
    private void setUpFab(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ContactsActivity.this, EditContactActivity.class);
                tabNum = pager.getCurrentItem();
                tabNum++;
                i.putExtra(TAB, String.valueOf(tabNum));
                i.putExtra(ACTIVITY, ADD);
                startActivity(i);
                finish();
            }
        });
    }
}
