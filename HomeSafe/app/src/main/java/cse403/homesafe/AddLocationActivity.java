package cse403.homesafe;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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
 * AddLocationActivity manages the adding of a single location,
 * which consists of a name, address, city, and state.
 *
 * In order to add a location, all four must not be entered and the address should be verified
 * via google. If not,
 * toasts that these must be filled.
 */
public class AddLocationActivity extends ActionBarActivity {
    public static final String EMPTY_STR = "";
    Button discardChange;
    ImageView saveLocation;
    Destinations mDesList;
    HomeSafeDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        mDbHelper = new HomeSafeDbHelper(this);
        mDesList = Destinations.getInstance();
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.actionbar_custom, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title);
        mTitleTextView.setText("Add new location");
        mTitleTextView.setTextColor(Color.WHITE);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        discardChange = (Button)findViewById(R.id.discard);
        saveLocation = (ImageView) findViewById(R.id.save_menu_item);
        setUpButton();
    }

    //setting button listeners
    private void setUpButton(){
        //discard current change, navigate to last screen
        discardChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddLocationActivity.this, FavLocationsActivity.class);
                startActivity(i);
                finish();
            }
        });
        //save contact information based on the text input
        saveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddLocationActivity.this, FavLocationsActivity.class);
                EditText mEditName = (EditText)findViewById(R.id.name_text);
                String nameText = mEditName.getText().toString();
                EditText mEditAddress = (EditText) findViewById(R.id.st_address_text);
                String stAddrText = mEditAddress.getText().toString();
                EditText mEditCity = (EditText) findViewById(R.id.city_text);
                String cityText = mEditCity.getText().toString();
                EditText mEditState = (EditText) findViewById(R.id.state_text);
                String stateText = mEditState.getText().toString();

                // Check that all fields are non-empty
                if(!nameText.equals(EMPTY_STR) && !stAddrText.equals(EMPTY_STR) && !cityText.equals(EMPTY_STR) && !stateText.equals(EMPTY_STR)){
                    String finalAddr = capitalize(stAddrText) + ", " + capitalize(cityText) + ", " + capitalize(stateText);
                    Destination newDes = new Destination(nameText, finalAddr);
                    //address is not valid
                    if (!newDes.isReady()) {
                        Toast.makeText(AddLocationActivity.this, "Please enter a valid address", Toast.LENGTH_SHORT).show();
                    } else {
                        //add destination to cache list
                        mDesList.addDestination(newDes);
                        //add to database
                        DbFactory.addDestinationToDb(newDes, mDbHelper);
                        Toast.makeText(AddLocationActivity.this, "Added Location", Toast.LENGTH_SHORT).show();
                        startActivity(i);
                        finish();
                    }
                } else {
                    Toast.makeText(AddLocationActivity.this, "Missing Information", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Quick helper method for capitalizing the first letter of each word in user input.
    private String capitalize(final String word) {
        String[] arr = word.split(" ");
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < arr.length; i++) {
            sb.append(Character.toUpperCase(arr[i].charAt(0)))
                    .append(arr[i].substring(1)).append(" ");
        }
        return sb.toString().trim();
    }
}
