package cse403.homesafe;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class EditLocationActivity extends ActionBarActivity {
    Button discardChange;
    ImageView saveLocation;
    Button deleteLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_location);
        Intent intent = getIntent();
        String name = intent.getStringExtra("NAME");
        String address = intent.getStringExtra("ADDRESS");
        //TODO auto fill later
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.actionbar_custom, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title);
        mTitleTextView.setText("Edit location");
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        discardChange = (Button)findViewById(R.id.discard);
        saveLocation = (ImageView) findViewById(R.id.save_menu_item);
        setUpButton();
    }

    private void setUpButton(){
        discardChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditLocationActivity.this, FavLocationsActivity.class);
                startActivity(i);
                finish();
            }
        });
        saveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditLocationActivity.this, ContactsActivity.class);
                Toast.makeText(EditLocationActivity.this, "Edited Location", Toast.LENGTH_SHORT).show();
                startActivity(i);
                finish();
            }
        });
//        deleteLocation.setOnClickListener();
    }
}
