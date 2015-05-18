package cse403.homesafe;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cse403.homesafe.Data.DbFactory;
import cse403.homesafe.Data.Destination;
import cse403.homesafe.Data.Destinations;
import cse403.homesafe.Data.HomeSafeDbHelper;


/**
 * EditLocationActivity manages the editing of a single contact,
 * which supports changing the name, phone number, email, and tier.
 *
 * EditLocation Screen will auto populate the location information, user needs
 * to modify based on the original information. Incomplete information will not
 * be processed
 */
public class EditLocationActivity extends ActionBarActivity {
    public static final String EMPTY_STR = "";
    Button discardChange;
    ImageView saveLocation;
    Button deleteLocation;
    EditText mEditName;
    EditText mEditStAddr;
    EditText mEditCity;
    EditText mEditState;
    HomeSafeDbHelper mDbHelper;
    Destinations mDesList;
    Long did;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_location);
        mDesList = Destinations.getInstance();
        mDbHelper = new HomeSafeDbHelper(this);
        Intent intent = getIntent();
        String name = intent.getStringExtra("NAME");
        String address = intent.getStringExtra("ADDRESS");
        String[] splitAddr = address.split(", ");
        did = intent.getLongExtra("DID", 0);
        this.mEditName = (EditText) findViewById(R.id.name_text);
        this.mEditStAddr = (EditText) findViewById(R.id.st_address_text);
        this.mEditCity = (EditText) findViewById(R.id.city_text);
        this.mEditState = (EditText) findViewById(R.id.state_text);
        mEditName.setText(name);
        mEditStAddr.setText(splitAddr[0]);
        mEditCity.setText(splitAddr[1]);
        mEditState.setText(splitAddr[2]);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.actionbar_custom, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title);
        mTitleTextView.setText("Edit location");
        mTitleTextView.setTextColor(Color.WHITE);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        discardChange = (Button)findViewById(R.id.discard);
        deleteLocation = (Button)findViewById(R.id.delete);
        saveLocation = (ImageView) findViewById(R.id.save_menu_item);
        setUpButton();
    }

    private void setUpButton(){
        //discard current change, navigate to location list screen
        discardChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditLocationActivity.this, FavLocationsActivity.class);
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
                    Intent i = new Intent(EditLocationActivity.this, FavLocationsActivity.class);
                    String finalAddr = capitalize(stAddrStr) + ", " + capitalize(cityStr) + ", " + stateStr.toUpperCase();
                    Log.e("EditLocation: ", finalAddr);
                    Destination newDes = new Destination(nameStr, finalAddr);
                    newDes.setDid(did);
                    if (!newDes.isReady()) {
                        Toast.makeText(EditLocationActivity.this, "Please enter a valid address", Toast.LENGTH_SHORT).show();
                    } else {
                        mDesList.removeDestination(did);
                        mDesList.addDestination(newDes);
                        DbFactory.updateDestination(newDes, mDbHelper);
                        Toast.makeText(EditLocationActivity.this, "Edited Location", Toast.LENGTH_SHORT).show();
                        startActivity(i);
                        finish();
                    }
                } else {
                    Toast.makeText(EditLocationActivity.this, "Missing Information", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //delete the location and navigate back to location screen
        deleteLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDesList.removeDestination(did);
                DbFactory.deleteDestinationFromDb(did, mDbHelper);
                Toast.makeText(EditLocationActivity.this, "Location Deleted", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(EditLocationActivity.this, FavLocationsActivity.class);
                startActivity(i);
                finish();
            }
        });
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
}
