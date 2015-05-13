package cse403.homesafe;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cse403.homesafe.Data.Destination;
import cse403.homesafe.Data.Destinations;
import cse403.homesafe.Util.DistanceAndTime;
import cse403.homesafe.Util.GoogleMapsUtils;
import cse403.homesafe.Util.GoogleMapsUtilsCallback;


public class TripSettingActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMapsUtilsCallback {

    Button destinations;
    TimePicker ETA;
    Button startTrip;
    Location destination;
    Button newLocation;

    DistanceAndTime distAndTime;

    int PLACE_PICKER_REQUEST = 1;

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_setting);

        final TripSettingActivity that = this;
        destinations = (Button) findViewById(R.id.favoriteLocationButton);
        destinations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(that);

                alert.setTitle("Set Destination");
                alert.setMessage("Choose a destination from favorites:");

                // Set an EditText view to get user input
                final Spinner input = new Spinner(that);
                List<Destination> destinationList = Destinations.getInstance().getDestinations();
                Map<String, Location> nameToLocation = new HashMap<String, Location>();

                ArrayList<String> stringList = new ArrayList<String>();
                for (Destination dest : destinationList) {
                    stringList.add(dest.getName());
                    nameToLocation.put(dest.getName(), dest.getLocation());
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(that,
                        android.R.layout.simple_spinner_item, stringList);
                input.setAdapter(dataAdapter);
                input.setBackgroundColor(0xFFAAAAAA);
                alert.setView(input);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // TODO
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });

                alert.show();
            }
        });

        newLocation = (Button) findViewById(R.id.button4);
        newLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int PLACE_PICKER_REQUEST = 1;
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                Context context = getApplicationContext();
                try {
                    startActivityForResult(builder.build(context), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        ETA = (TimePicker) findViewById(R.id.spinnerETA);
        ETA.setIs24HourView(true);
        ETA.setCurrentHour(0);
        ETA.setCurrentMinute(0);

        startTrip = (Button) findViewById(R.id.startTripButton);
        startTrip.setEnabled(false);
        startTrip.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View w) {
                        long time = 1000 * (ETA.getCurrentHour() * 3600 + ETA.getCurrentMinute() * 60);
                        Intent i = new Intent(getApplicationContext(), HSTimerActivity.class);
                        i.putExtra("timefromuser", time);
                        startActivity(i);
                    }

                }
        );
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                //TODO actually do something with the
                Place place = PlacePicker.getPlace(data, this);
                destination = new Location("lolwut?");
                destination.setLatitude(place.getLatLng().latitude);
                destination.setLongitude(place.getLatLng().longitude);
            }
        }
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

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            GoogleMapsUtils.getDistanceAndTime(mLastLocation, destination, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        // TODO

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // TODO
    }

    @Override
    public void onGetDistanceAndTime(Object obj) {
        if (obj instanceof DistanceAndTime) {
            distAndTime = (DistanceAndTime) obj;
            int hours = (int) distAndTime.getTime() % 3600;
            int minutes = ((int) distAndTime.getTime() - hours*3600) / 60;
            ETA.setCurrentHour(hours);
            ETA.setCurrentMinute(minutes);

            if (distAndTime != null && mLastLocation != null) {
                startTrip.setEnabled(true);
            }
        }
    }

    @Override
    public void onAddressToLocation(Object obj) { }
}
