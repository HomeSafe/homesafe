package cse403.homesafe;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cse403.homesafe.Data.Contact;
import cse403.homesafe.Data.Contacts;
import cse403.homesafe.Data.DbFactory;
import cse403.homesafe.Data.HomeSafeDbHelper;


public class AddContactActivity extends ActionBarActivity {
    Button discardChange;
    ImageView saveContact;
    HomeSafeDbHelper mDbHelper;
    Contacts mContactList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        mDbHelper = new HomeSafeDbHelper(this);
        mContactList = mContactList.getInstance();
        //TODO setting button discard changes
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.actionbar_custom, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title);
        mTitleTextView.setText("Add new contact");
        //TODO version of Edit contact
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        discardChange = (Button)findViewById(R.id.discard);
        saveContact = (ImageView) findViewById(R.id.save_menu_item);
        setUpButton();
    }

    private void setUpButton(){
        discardChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddContactActivity.this, ContactsActivity.class);
                startActivity(i);
                finish();
            }
        });
        saveContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Contact contact = null;
                Contacts.Tier tier = null;
                mContactList.addContact(contact, tier);
                DbFactory.addContactToDb(contact, mDbHelper);
                Intent i = new Intent(AddContactActivity.this, ContactsActivity.class);
                Toast.makeText(AddContactActivity.this, "Added Contact", Toast.LENGTH_SHORT).show();
                startActivity(i);
                finish();
            }
        });
    }
}
