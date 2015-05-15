package cse403.homesafe;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

import cse403.homesafe.CardViewUtility.LocationRecyclerAdapter;
import cse403.homesafe.Data.Destinations;


public class FavLocationsActivity extends ActionBarActivity {
    FloatingActionButton fab;
    RecyclerView recyclerView;
    Destinations mDesList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_locations);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Favorite Locations");

        this.recyclerView = (RecyclerView) findViewById(R.id.recyclerList);
        LinearLayoutManager linearLM = new LinearLayoutManager(this);
        linearLM.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLM);
        mDesList = Destinations.getInstance();
        for(int i = 0; i < mDesList.getDestinations().size(); i++){
            System.out.println(mDesList.getDestinations().get(i).getAddress());
        }
        recyclerView.setAdapter(new LocationRecyclerAdapter(Destinations.getInstance().getDestinations()));
        fab = (FloatingActionButton) findViewById(R.id.location_fab);
        setUpFab();
    }

//    @Override
//    public void onResume(){
//        super.onResume();
//        System.out.println(Destinations.getInstance().getDestinations());
//
//    }

    private void setUpFab(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FavLocationsActivity.this, AddLocationActivity.class);
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
