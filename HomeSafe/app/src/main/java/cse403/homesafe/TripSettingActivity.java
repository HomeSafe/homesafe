package cse403.homesafe;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
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

/**
 * TripSettingActivity manages setting the destination, displaying the estimated
 * time arrival from the current location to the destination, and starting the trip
 * after destination has been selected.
 */
public class TripSettingActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMapsUtilsCallback {

    public static final int SPINNER_PLACEHOLDER = -100;
    public static final String PLACEHOLDER_STR = "Select From Favorites";
    TimePicker ETA;     // time picker for user to set ETA
    Button startTrip;     // button for starting trip
    Button selectFromMap; // button for selecting location from map
    Location mLastLocation; // the user's last known location (current location)
    Location destination;   // the user's intended destination
    DistanceAndTime distAndTime; // stores the distance and time it takes to arrive at destination
    GoogleApiClient mGoogleApiClient; // access the google api to retrieve last known location
    String TAG = "TripSettingActivity"; // for logcat debugging purposes
    int PLACE_PICKER_REQUEST = 1;

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_setting);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle("Trip Settings");
        Destination defaultDest = new Destination("No Available Destinations");

        Spinner input = (Spinner) findViewById(R.id.favoriteLocationSpinner);
        List<Destination> destinationList = Destinations.getInstance().getDestinations();
        if(destinationList.isEmpty()){
            destinationList.add(defaultDest);
        } else {
            if(destinationList.get(0).getDid() > 0){
                destinationList.add(0, new Destination(PLACEHOLDER_STR, SPINNER_PLACEHOLDER));
            }
        }
        final Map<String, Location> nameToLocation = new HashMap<String, Location>();
        ArrayList<String> stringList = new ArrayList<String>();
        for (Destination dest : destinationList) {
            stringList.add(dest.getName());
            nameToLocation.put(dest.getName(), dest.getLocation());

        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, stringList);
        dataAdapter.setDropDownViewResource(R.layout.spinner_drop_down);
        input.setAdapter(dataAdapter);
        input.setOnItemSelectedListener(new CustomOnItemSelectedListener(nameToLocation));

        selectFromMap = (Button) findViewById(R.id.chooseFromMap);
        selectFromMap.setOnClickListener(new View.OnClickListener() {
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
        TextView estimateTime = (TextView) findViewById(R.id.hint);
        estimateTime.setText("Est. Time in Hour and Minute");

        startTrip = (Button) findViewById(R.id.startTripButton);
        startTrip.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View w) {
                        long time = 1000 * (ETA.getCurrentHour() * 3600 + ETA.getCurrentMinute() * 60);

                        if (time == 0) {
                            Toast.makeText(TripSettingActivity.this,
                                    "You cannot start a trip with no time set",
                                    Toast.LENGTH_LONG).show();
                        } else if (destination == null) {
                            Toast.makeText(TripSettingActivity.this,
                                    "You cannot start a trip with no destination set",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Intent i = new Intent(getApplicationContext(), HSTimerActivity.class);
                            i.putExtra("timefromuser", time);
                            startActivity(i);
                        }
                    }
                }
        );
    }

    /**
     * Listener method for when user's selects a destination from the map. This method
     * displays the estimated time arrival from the current location to the destination
     * that was selected from the map.
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                destination = new Location("New Destination");
                destination.setLatitude(place.getLatLng().latitude);
                destination.setLongitude(place.getLatLng().longitude);
                TextView currentDestinationText = (TextView) findViewById(R.id.currentDestinationText);
                currentDestinationText.setText("Destination: " + place.getName());
                if (checkPlayServices()) {
                    Log.e(TAG, "Google Play Services is installed");
                    buildGoogleApiClient();
                    onStart();
                } else {
                    Log.e(TAG, "Google Play Services is not installed");
                }
            } else {
                Log.e(TAG, "result code was not okay: " + resultCode);
            }
        } else {
            Log.e(TAG, "request code was not PLACE_PICKER_REQUEST: " + requestCode);
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

    /**
     * Builds the Google Api Client for the purpose of retrieving the
     * user's current location.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        if (mGoogleApiClient != null) {
            Log.e(TAG, "Build Complete");
        } else {
            Log.e(TAG, "Build Incomplete");
        }
    }

    /**
     * Callback method of Google API Client if connected.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        Log.e(TAG, "Connected!");
        if (mLastLocation != null) {
            GoogleMapsUtils.getDistanceAndTime(mLastLocation, destination, this);
        }
    }

    /**
     * Starts the Google API Client.
     */
    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            Log.e(TAG, "Connection Started");
            mGoogleApiClient.connect();
        }
    }

    /**
     * Callback method for Google API Client if connection is suspended.
     */
    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG, "Connection Suspended");

    }

    /**
     * Callback method for Google API Client if connection fails.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "Connection Failed");
    }

    /**
     * Callback for when GoogleMapUtils finishes, retrieves the distance and
     * time it takes to arrive to the destination from current location, and sets the
     * time picker to display the retrieved ETA.
     */
    @Override
    public void onGetDistanceAndTime(final Object obj) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "onGetDistanceAndTime was called within TripSettingActivity");
                if (obj instanceof DistanceAndTime) {
                    distAndTime = (DistanceAndTime) obj;
                    int hours = (int) distAndTime.getTime() / 3600;
                    int minutes = ((int) distAndTime.getTime() - hours * 3600) / 60;
                    ETA.setCurrentHour(hours);
                    ETA.setCurrentMinute(minutes);
                    TextView estimateTime = (TextView) findViewById(R.id.hint);
                    estimateTime.setText("Est. Arrival in Hour and Minute");
                    if (distAndTime != null && mLastLocation != null) {
                        startTrip.setEnabled(true);
                        StringBuilder estimationMsg = new StringBuilder();
                        estimationMsg.append("Your estimated time of arrival is ");
                        estimationMsg.append(hours);
                        if (hours == 1 || hours == 0) {
                            estimationMsg.append(" hour and ");
                        } else {
                            estimationMsg.append(" hours and ");
                        }
                        estimationMsg.append(minutes);
                        if (minutes == 1) {
                            estimationMsg.append(" minute.");
                        } else {
                            estimationMsg.append(" minutes.");
                        }
                        Toast.makeText(TripSettingActivity.this, estimationMsg, Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
    }

    @Override
    public void onAddressToLocation(Object obj) { }

    /**
     * Returns true if GooglePlayServices is installed on the device otherwise
     * false.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Listener class for when user's selects a destination from the drop down favorite list.
     * This method displays the estimated time arrival from the current location to the
     * destination that was selected from the map.
     */
    public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener{
        Map<String, Location> nameToLocation;
        public CustomOnItemSelectedListener(Map<String, Location> nameToLocation){
            this.nameToLocation = nameToLocation;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(position != 0) {
                String nameOfDest = parent.getItemAtPosition(position).toString() + "";
                destination = nameToLocation.get(nameOfDest);
                if (checkPlayServices()) {
                    Log.e(TAG, "Google Play Services is installed");
                    buildGoogleApiClient();
                    onStart();
                } else {
                    Log.e(TAG, "Google Play Services is not installed");
                }

                TextView currentDestinationText = (TextView) findViewById(R.id.currentDestinationText);
                currentDestinationText.setText("Destination: " + nameOfDest);
            } else {
                TextView currentDestinationText = (TextView) findViewById(R.id.currentDestinationText);
                currentDestinationText.setText("No Destination Selected");
                TextView estimateTime = (TextView) findViewById(R.id.hint);
                estimateTime.setText("");
                ETA.setCurrentHour(0);
                ETA.setCurrentMinute(0);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
