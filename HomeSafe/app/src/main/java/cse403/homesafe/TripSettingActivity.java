package cse403.homesafe;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;


public class TripSettingActivity extends ActionBarActivity {

    Spinner destinations;
    Spinner ETA;
    Button startTrip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_setting);

        destinations = (Spinner) findViewById(R.id.spinnerDestination);
        String[] locations = null; // TODO : get the locations
        ArrayAdapter<String> adapterLocation = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, locations);
        adapterLocation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        destinations.setAdapter(adapterLocation);

        ETA = (Spinner) findViewById(R.id.spinnerETA);
        String[] times = {"1", "2", "3"}; // TODO : decide on time intervals
        ArrayAdapter<String> adapterETA = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, times);
        adapterETA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ETA.setAdapter(adapterETA);

        startTrip = (Button) findViewById(R.id.startTripButton);
        startTrip.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View w) {
                        String allocatedTime = ETA.getSelectedItem() + "";
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
