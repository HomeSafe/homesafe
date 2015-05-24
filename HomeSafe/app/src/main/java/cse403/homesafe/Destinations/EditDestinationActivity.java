package cse403.homesafe;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cse403.homesafe.Data.DbFactory;
import cse403.homesafe.Data.Destination;
import cse403.homesafe.Data.Destinations;
import cse403.homesafe.Data.HomeSafeDbHelper;


/**
 * EditLocationActivity manages the adding and editing of a single contact,
 * depending on the intent information passed in.
 * It supports changing the name, phone number, email, and tier.
 *
 * EditLocation Screen will auto populate the location information on editing activity
 * , user needs to modify based on the original information.
 * Incomplete information will not be processed
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
        String activityType = intent.getStringExtra("ACTIVITY");


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
        if(activityType.equals("EDIT")) {
            edit = true;
            String name = intent.getStringExtra("NAME");
            String address = intent.getStringExtra("ADDRESS");
            String[] splitAddr = address.split(", ");
            did = intent.getLongExtra("DID", 0);

            mEditName.setText(name);
            mEditStAddr.setText(splitAddr[0]);
            mEditCity.setText(splitAddr[1]);
            mEditState.setText(splitAddr[2]);
            mTitleTextView.setText("Edit location");

            deleteLocation = new Button(this);
            deleteLocation.setText("Delete Contact");
            deleteLocation.setBackground(getResources().getDrawable(R.drawable.ripple));
            RelativeLayout.LayoutParams layoutParam = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            layoutParam.addRule(RelativeLayout.ALIGN_PARENT_END);
            layoutParam.setMargins(10, 0, 10, 0);
            ((RelativeLayout) findViewById(R.id.edit_layout)).addView(deleteLocation, layoutParam);
        } else {
            mTitleTextView.setText("Add location");
            add = true;
        }

        mTitleTextView.setTextColor(Color.WHITE);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        discardChange = (Button)findViewById(R.id.discard);

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
                    Destination newDes = new Destination(nameStr, finalAddr);
                    if(edit) {
                        newDes.setDid(did);
                    }
                    if (!newDes.isReady()) {
                        Toast.makeText(EditLocationActivity.this, "Please enter a valid address", Toast.LENGTH_SHORT).show();
                    } else {
                        String toastStr;
                        if(edit) {
                            mDesList.removeDestination(did);
                            toastStr = "Edited Location";
                            DbFactory.updateDestination(newDes, mDbHelper);
                        } else {
                            toastStr = "Added Location";
                            DbFactory.addDestinationToDb(newDes, mDbHelper);
                        }
                        mDesList.addDestination(newDes);
                        Toast.makeText(EditLocationActivity.this, toastStr, Toast.LENGTH_SHORT).show();
                        startActivity(i);
                        finish();
                    }
                } else {
                    Toast.makeText(EditLocationActivity.this, "Missing Information", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(EditLocationActivity.this, "Location Deleted", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(EditLocationActivity.this, FavLocationsActivity.class);
                    startActivity(i);
                    finish();
                }
            });
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
}
