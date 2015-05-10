package cse403.homesafe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.util.ArrayList;


public class TripSettingActivity extends ActionBarActivity {

    Button destinations;
    TimePicker ETA;
    Button startTrip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_setting);

        final TripSettingActivity that = this;
        destinations = (Button) findViewById(R.id.spinnerDestination);
        destinations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(that);

                alert.setTitle("Destination");
                alert.setMessage("Choose a destination from the map or from favorites:");

                // Set an EditText view to get user input
                final Spinner input = new Spinner(that);
                ArrayList<String> list = new ArrayList<String>();
                list.add("The HUB");
                list.add("Grandma's House");
                list.add("The Bar");
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(that,
                        android.R.layout.simple_spinner_item, list);
                input.setAdapter(dataAdapter);
                input.setBackgroundColor(0xFFAAAAAA);
                alert.setView(input);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //TODO
                    }
                });

                alert.show();
            }
        });

        ETA = (TimePicker) findViewById(R.id.spinnerETA);
        ETA.setIs24HourView(true);
        ETA.setCurrentHour(0);
        ETA.setCurrentMinute(0);

        startTrip = (Button) findViewById(R.id.startTripButton);
        startTrip.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View w) {
                        String allocatedTime = ETA.getCurrentMinute() + "";
                        // TODO : mapping from destination name to actual Location object?
                        //Trip currentTrip = new Trip(destinations.getSelectedItem(), Long.parseLong(allocatedTime));
                    }

                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_trip_setting, menu);
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
