package cse403.homesafe.Contacts;

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

import cse403.homesafe.Data.Contact;
import cse403.homesafe.Data.Contacts;
import cse403.homesafe.Data.DbFactory;
import cse403.homesafe.Data.HomeSafeDbHelper;
import cse403.homesafe.R;

/**
 * EditContactActivity manages the editing of a single contact,
 * which supports changing the name, phone number, email, and tier.
 *
 * EditContact Screen will auto populate the contact information, user needs
 * to modify based on the original information. Incomplete information will not
 * be processed
 */
public class EditContactActivity extends ActionBarActivity {
    public static final String EMPTY_STR = "";
    private static final String TAG = "EditContactActivity";
    Button discardChange;
    ImageView saveContact;
    HomeSafeDbHelper mDbHelper;
    Contacts mContactList;
    Button deleteContact;
    long cid;
    EditText mEditName;
    EditText mEditPhone;
    EditText mEditEmail;
    EditText mEditTier;
    String tier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        //these information always come from the recyclerview
        String name = intent.getStringExtra("NAME");
        String phone = intent.getStringExtra("PHONE");
        String email = intent.getStringExtra("EMAIL");
        tier = intent.getStringExtra("TIER");
        cid = intent.getLongExtra("CID", 0);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);
        this.mEditName = (EditText)findViewById(R.id.name_text);
        mEditName.setText(name);
        this.mEditPhone = (EditText)findViewById(R.id.phone_text);
        mEditPhone.setText(phone);
        this.mEditEmail = (EditText)findViewById(R.id.email_text);
        mEditEmail.setText(email);
        this.mEditTier = (EditText)findViewById(R.id.tier_text);
        mEditTier.setText(tier);
        mDbHelper = new HomeSafeDbHelper(this);
        mContactList = Contacts.getInstance();
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayHomeAsUpEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.actionbar_custom, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title);
        mTitleTextView.setText("Edit contact");
        mTitleTextView.setTextColor(Color.WHITE);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        discardChange = (Button)findViewById(R.id.discard);
        saveContact = (ImageView) findViewById(R.id.save_menu_item);
        deleteContact = (Button)findViewById(R.id.delete);
        setUpButton();
    }

    //set up the button listener
    private void setUpButton(){
        //discard current change, navigate to contact list screen
        discardChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditContactActivity.this, ContactsActivity.class);
                i.putExtra("TAB", "TAB" + tier);
                Log.e(TAG, "TAB" + tier);
                startActivity(i);
                finish();
            }
        });
        //save contact information based on the text change
        saveContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameStr = mEditName.getText().toString();
                String phoneStr = mEditPhone.getText().toString();
                String emailStr = mEditEmail.getText().toString();
                String tierStr = mEditTier.getText().toString();
                if(!nameStr.equals(EMPTY_STR) && !phoneStr.equals(EMPTY_STR) && !emailStr.equals(EMPTY_STR) && !tierStr.equals(EMPTY_STR)) {
                    int tierNum = Integer.parseInt(mEditTier.getText().toString());
                    Contacts.Tier tier;
                    if(tierNum == 1){
                        tier = Contacts.Tier.ONE;
                    } else if(tierNum == 2){
                        tier = Contacts.Tier.TWO;
                    } else {
                        tier = Contacts.Tier.THREE;
                    }
                    Contact contact = new Contact(mEditName.getText().toString(), mEditEmail.getText().toString(), mEditPhone.getText().toString(), tier);
                    contact.setCid(cid);
                    mContactList.editContact(cid, mEditName.getText().toString(), mEditPhone.getText().toString(), mEditEmail.getText().toString(), tier);
                    DbFactory.updateContact(contact, mDbHelper);
                    Intent i = new Intent(EditContactActivity.this, ContactsActivity.class);
                    i.putExtra("TAB", "TAB" + tierNum);
                    Toast.makeText(EditContactActivity.this, "Edited Contact " + mEditName.getText(), Toast.LENGTH_SHORT).show();
                    startActivity(i);
                    finish();
                } else {
                    //information incomplete
                    Toast.makeText(EditContactActivity.this, "Missing Information", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //delete the contact and navigate back to contact screen
        deleteContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContactList.removeContact(cid);
                DbFactory.deleteContactFromDb(cid, mDbHelper);
                Toast.makeText(EditContactActivity.this, "Contact Deleted", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(EditContactActivity.this, ContactsActivity.class);
                i.putExtra("TAB", "TAB" + tier);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        discardChange.callOnClick();    // red is fine since we have min at 21
    }
}
