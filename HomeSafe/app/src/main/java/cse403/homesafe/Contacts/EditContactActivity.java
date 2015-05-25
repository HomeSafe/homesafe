package cse403.homesafe.Contacts;

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

import cse403.homesafe.Data.Contact;
import cse403.homesafe.Data.Contacts;
import cse403.homesafe.Data.DbFactory;
import cse403.homesafe.Data.HomeSafeDbHelper;
import cse403.homesafe.R;

/**
 * EditContactActivity manages the adding and editing of a single contact,
 * depending on the intent information passed in.
 * It supports changing the name, phone number, email, and tier.
 *
 * EditContact Screen will auto populate the contact information on editing activities
 * , user needs to modify based on the original information. Incomplete information will not
 * be processed
 * Tier information can be autofilled
 */
public class EditContactActivity extends ActionBarActivity {
    public static final String EMPTY_STR = "";
    private static final String TAG = "EditContactActivity";
    Button discardChange;
    Button saveButton;
    ImageView saveContact;
    HomeSafeDbHelper mDbHelper;
    Contacts mContactList;
    Button deleteContact;
    Button discardEdit;
    long cid;
    EditText mEditName;
    EditText mEditPhone;
    EditText mEditEmail;
    EditText mEditTier;
    String tier;
    boolean edit;
    boolean add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        //these information always come from the recyclerview
        String activityType = intent.getStringExtra("ACTIVITY");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        mDbHelper = new HomeSafeDbHelper(this);
        mContactList = Contacts.getInstance();
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayHomeAsUpEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.actionbar_custom, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title);
        this.mEditName = (EditText)findViewById(R.id.name_text);
        this.mEditPhone = (EditText)findViewById(R.id.phone_text);
        this.mEditEmail = (EditText)findViewById(R.id.email_text);
        this.mEditTier = (EditText)findViewById(R.id.tier_text);

        if(activityType.equals("ADD")){
            mTitleTextView.setText("Add Contact");
            EditText mEditTier = (EditText)findViewById(R.id.tier_text);
            tier = getIntent().getStringExtra("TAB");
            mEditTier.setText(tier);
            saveButton = new Button(this);
            saveButton.setText("Save");
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
        } else {
            String name = intent.getStringExtra("NAME");
            String phone = intent.getStringExtra("PHONE");
            String email = intent.getStringExtra("EMAIL");
            tier = intent.getStringExtra("TIER");
            cid = intent.getLongExtra("CID", 0);
            mEditName.setText(name);
            mEditPhone.setText(phone);
            mEditEmail.setText(email);
            mEditTier.setText(tier);
            mTitleTextView.setText("Edit Contact");
            deleteContact = new Button(this);
            deleteContact.setText("Delete Contact");
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                deleteContact.setBackground(getResources().getDrawable(R.drawable.ripple));
            }
            RelativeLayout layout = ((RelativeLayout) findViewById(R.id.edit_layout));
            RelativeLayout.LayoutParams layoutParam = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            layoutParam.addRule(RelativeLayout.ALIGN_PARENT_END);
            layoutParam.setMargins(10, 0, 10, 0);
            layout.addView(deleteContact, layoutParam);
            edit = true;
        }
        mTitleTextView.setTextColor(Color.WHITE);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        discardChange = (Button)findViewById(R.id.discard);
        saveContact = (ImageView) findViewById(R.id.save_menu_item);
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
                    } else if(tierNum == 3) {
                        tier = Contacts.Tier.THREE;
                    } else {
                        Toast.makeText(EditContactActivity.this, "Please enter either 1, 2 or 3 as tier level", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Contact contact = new Contact(nameStr, emailStr, phoneStr, tier);
                    if(add){
                        //add contact to in memory cache
                        mContactList.addContact(contact, tier);
                        //add contact to db
                        DbFactory.addContactToDb(contact, mDbHelper);
                        Toast.makeText(EditContactActivity.this, "Added Contact " + mEditName.getText(), Toast.LENGTH_SHORT).show();
                    } else {
                        contact.setCid(cid);
                        mContactList.editContact(cid, mEditName.getText().toString(), mEditPhone.getText().toString(), mEditEmail.getText().toString(), tier);
                        DbFactory.updateContact(contact, mDbHelper);
                        Toast.makeText(EditContactActivity.this, "Edited Contact " + nameStr, Toast.LENGTH_SHORT).show();
                    }
                    Intent i = new Intent(EditContactActivity.this, ContactsActivity.class);
                    i.putExtra("TAB", "TAB" + tierNum);

                    startActivity(i);
                    finish();
                } else {
                    //information incomplete
                    Toast.makeText(EditContactActivity.this, "Missing Fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //delete the contact and navigate back to contact screen
        if(edit) {
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
    }

    @Override
    public void onBackPressed() {
        discardChange.callOnClick();    // red is fine since we have min at 21
    }
}
