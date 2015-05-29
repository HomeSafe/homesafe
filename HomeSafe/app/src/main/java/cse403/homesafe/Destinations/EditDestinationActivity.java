package cse403.homesafe.Destinations;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import cse403.homesafe.Data.DbFactory;
import cse403.homesafe.Data.Destination;
import cse403.homesafe.Data.Destinations;
import cse403.homesafe.Data.HomeSafeDbHelper;
import cse403.homesafe.R;
import cse403.homesafe.Util.GoogleMapsUtilsCallback;


/**
 * EditLocationActivity manages the adding and editing of a single contact,
 * depending on the intent information passed in.
 * It supports changing the name, phone number, email, and tier.
 *
 * EditLocation Screen will auto populate the location information on editing activity
 * , user needs to modify based on the original information.
 * Incomplete information will not be processed
 */
public class EditDestinationActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMapsUtilsCallback{
    public static final String EMPTY_STR = "";
    public static final String ACTIVITY = "ACTIVITY";
    public static final String EDIT = "EDIT";
    public static final String NAME = "NAME";
    public static final String ADDRESS = "ADDRESS";
    public static final String DID = "DID";
    public static final String EDIT_LOCATION = "Edit Location";
    public static final String DELETE = "Delete";
    public static final String ADD_LOCATION = "Add Location";
    public static final String SAVE = "Save";
    public static final String MISSING_INFO_MSG = "Missing Information";
    public static final String DELETE_MSG = "Location Deleted";
    public static final String ADD_MSG = "Added Location";
    public static final String EDIT_MSG = "Edited Location";
    public static final String INVALID_ADDR_MSG = "Please enter a valid address";
    int PLACE_PICKER_REQUEST = 1;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    String TAG = "EditDestinationActivity";
    GoogleApiClient mGoogleApiClient;
    Button discardChange;
    ImageView saveLocation;
    Button deleteLocation;
    Button saveButton;
    Button selectFromMap;
    EditText mEditName;
    EditText mEditStAddr;
    EditText mEditCity;
    EditText mEditState;
    HomeSafeDbHelper mDbHelper;
    Destinations mDesList;
    Boolean add = false;
    Boolean edit = false;
    Long did;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_location);
        mDesList = Destinations.getInstance();
        mDbHelper = new HomeSafeDbHelper(this);
        Intent intent = getIntent();
        String activityType = intent.getStringExtra(ACTIVITY);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.actionbar_custom, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title);
        this.mEditName = (EditText) findViewById(R.id.name_text);
        this.mEditStAddr = (EditText) findViewById(R.id.st_address_text);
        this.mEditCity = (EditText) findViewById(R.id.city_text);
        this.mEditState = (EditText) findViewById(R.id.state_text);

        if(activityType.equals(EDIT)) {
            edit = true;
            String name = intent.getStringExtra(NAME);
            String address = intent.getStringExtra(ADDRESS);
            String[] splitAddr = address.split(", ");
            did = intent.getLongExtra(DID, 0);

            mEditName.setText(name);
            mEditStAddr.setText(splitAddr[0]);
            mEditCity.setText(splitAddr[1]);
            mEditState.setText(splitAddr[2]);
            mTitleTextView.setText(EDIT_LOCATION);

            deleteLocation = new Button(this);
            deleteLocation.setText(DELETE);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                deleteLocation.setBackground(getResources().getDrawable(R.drawable.ripple));
            }
            RelativeLayout layout = ((RelativeLayout) findViewById(R.id.edit_layout));
            RelativeLayout.LayoutParams layoutParam = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            layoutParam.addRule(RelativeLayout.ALIGN_PARENT_END);
            layoutParam.setMargins(10, 0, 10, 0);

            layout.addView(deleteLocation, layoutParam);

        } else {
            mTitleTextView.setText(ADD_LOCATION);
            saveButton = new Button(this);
            saveButton.setText(SAVE);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                saveButton.setBackground(getResources().getDrawable(R.drawable.ripple));
            }
            RelativeLayout layout = ((RelativeLayout) findViewById(R.id.edit_layout));
            RelativeLayout.LayoutParams layoutParam = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            layoutParam.addRule(RelativeLayout.ALIGN_PARENT_END);
            layoutParam.setMargins(10, 0, 10, 0);

            layout.addView(saveButton, layoutParam);

            add = true;
        }

        mTitleTextView.setTextColor(Color.WHITE);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        discardChange = (Button)findViewById(R.id.discard);
        selectFromMap = (Button)findViewById(R.id.select_from_map);
        saveLocation = (ImageView) findViewById(R.id.save_menu_item);
        setUpButton();
    }

    private void setUpButton(){
        //discard current change, navigate to location list screen
        discardChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditDestinationActivity.this, DestinationsActivity.class);
                startActivity(i);
                finish();
            }
        });
        //save location information based on the text change
        saveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameStr = mEditName.getText().toString();
                String stAddrStr = mEditStAddr.getText().toString();
                String cityStr = mEditCity.getText().toString();
                String stateStr = mEditState.getText().toString();
                if(!nameStr.equals(EMPTY_STR) && !stAddrStr.equals(EMPTY_STR) && !cityStr.equals(EMPTY_STR)
                        && !stateStr.equals(EMPTY_STR)) {
                    Intent i = new Intent(EditDestinationActivity.this, DestinationsActivity.class);
                    String finalAddr = capitalize(stAddrStr) + ", " + capitalize(cityStr) + ", " + stateStr.toUpperCase();
                    Destination newDes = new Destination(nameStr, finalAddr);
                    if(edit) {
                        newDes.setDid(did);
                    }
                    if (!newDes.isReady()) {
                        Toast.makeText(EditDestinationActivity.this, INVALID_ADDR_MSG, Toast.LENGTH_SHORT).show();
                    } else {
                        String toastStr;
                        if(edit) {
                            mDesList.removeDestination(did);
                            toastStr = EDIT_MSG;
                            DbFactory.updateDestination(newDes, mDbHelper);
                        } else {
                            toastStr = ADD_MSG;
                            DbFactory.addDestinationToDb(newDes, mDbHelper);
                        }
                        mDesList.addDestination(newDes);
                        Toast.makeText(EditDestinationActivity.this, toastStr, Toast.LENGTH_SHORT).show();
                        startActivity(i);
                        finish();
                    }
                } else {
                    Toast.makeText(EditDestinationActivity.this, MISSING_INFO_MSG, Toast.LENGTH_SHORT).show();
                }
            }
        });
        if(edit) {
            //delete the location and navigate back to location screen
            deleteLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDesList.removeDestination(did);
                    DbFactory.deleteDestinationFromDb(did, mDbHelper);
                    Toast.makeText(EditDestinationActivity.this, DELETE_MSG, Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(EditDestinationActivity.this, DestinationsActivity.class);
                    startActivity(i);
                    finish();
                }
            });
        }
        if(add){
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveLocation.callOnClick();
                }
            });
        }
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
    }

    /**
     * Listener method for when user's selects a destination from the map. This method
     * autofilled the address of the selected place
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String address = place.getAddress().toString();
                String[] parsedPlace = address.split(",");
                if(!address.equals("")) {
                    mEditName.setText(place.getName());

                    if (parsedPlace.length == 3) {
                        mEditCity.setText(parsedPlace[0].trim());
                        mEditState.setText(parsedPlace[1].substring(0, 3).trim());
                    } else {
                        mEditStAddr.setText(parsedPlace[0].trim());
                        mEditCity.setText(parsedPlace[1].trim());
                        mEditState.setText((parsedPlace[2].substring(0, 3)).trim());
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please select from \"Nearby Places\" ist", Toast.LENGTH_LONG)
                            .show();
                }

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

    // Quick helper method for capitalizing the first letter of each word in user input.
    private String capitalize(final String word) {
        String[] arr = word.split(" ");
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < arr.length; i++) {
            if (arr[i].length() > 1) {
                sb.append(Character.toUpperCase(arr[i].charAt(0)))
                        .append(arr[i].substring(1)).append(" ");
            }
        }
        return sb.toString().trim();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.e(TAG, "Connected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG, "Connection Suspended");
    }

    @Override
    public void onGetDistanceAndTime(Object obj) {

    }

    @Override
    public void onAddressToLocation(Object obj) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "Connection Failed");
    }
}

