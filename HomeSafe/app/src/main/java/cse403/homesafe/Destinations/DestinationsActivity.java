package cse403.homesafe.Destinations;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;

import com.melnykov.fab.FloatingActionButton;

import cse403.homesafe.CardViewUtility.LocationRecyclerAdapter;
import cse403.homesafe.Data.Destinations;
import cse403.homesafe.R;

/**
 * This class is used as the main user interface for displaying saved locations name and address.
 * DestinationsActivity retrieves the list of destinations and
 * displays them in cardview
 */
public class DestinationsActivity extends ActionBarActivity {
    public static final String TITLE = "Favorite Locations";
    public static final String ACTIVITY = "ACTIVITY";
    public static final String ADD = "ADD";
    FloatingActionButton fab; //add button
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_locations);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(TITLE);
        //populate the destination cardview
        this.recyclerView = (RecyclerView) findViewById(R.id.recyclerList);
        LinearLayoutManager linearLM = new LinearLayoutManager(this);
        linearLM.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLM);
        recyclerView.setAdapter(new LocationRecyclerAdapter(Destinations.getInstance().getDestinations()));
        fab = (FloatingActionButton) findViewById(R.id.location_fab);
        //set up button
        setUpFab();
    }

    //set up the floating add button for adding destination
    private void setUpFab(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //switch to EditDestinationActivity for add option
                Intent i = new Intent(DestinationsActivity.this, cse403.homesafe.Destinations.EditDestinationActivity.class);
                i.putExtra(ACTIVITY, ADD);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fav_locations, menu);
        return true;
    }
}
