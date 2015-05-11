package cse403.homesafe;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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


public class EditContactActivity extends ActionBarActivity {
    Button discardChange;
    ImageView saveContact;
    HomeSafeDbHelper mDbHelper;
    Contacts mContactList;
    Button deleteContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String name = intent.getStringExtra("NAME");
        String phone = intent.getStringExtra("PHONE");
        String email = intent.getStringExtra("EMAIL");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);
        EditText mEditName = (EditText)findViewById(R.id.name_text);
        mEditName.setText(name);
        EditText mEditPhone = (EditText)findViewById(R.id.phone_text);
        mEditPhone.setText(phone);
        EditText mEditEmail = (EditText)findViewById(R.id.email_text);
        mEditEmail.setText(email);
        mDbHelper = new HomeSafeDbHelper(this);
        mContactList = Contacts.getInstance();
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.actionbar_custom, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title);
        mTitleTextView.setText("Edit contact");
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        discardChange = (Button)findViewById(R.id.discard);
        saveContact = (ImageView) findViewById(R.id.save_menu_item);
        deleteContact = (Button)findViewById(R.id.delete);
        setUpButton();
    }

    private void setUpButton(){
        discardChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditContactActivity.this, ContactsActivity.class);
                startActivity(i);
                finish();
            }
        });
        saveContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText mEditName = (EditText)findViewById(R.id.name_text);
                EditText mEditPhone = (EditText)findViewById(R.id.phone_text);
                EditText mEditEmail = (EditText)findViewById(R.id.email_text);
                EditText mEditTier = (EditText)findViewById(R.id.tier_text);
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
                mContactList.addContact(contact, tier);
                DbFactory.addContactToDb(contact, mDbHelper);
                Intent i = new Intent(EditContactActivity.this, ContactsActivity.class);
                Toast.makeText(EditContactActivity.this, "Edited Contact " + mEditName.getText(), Toast.LENGTH_SHORT).show();
                startActivity(i);
                finish();
            }
        });
//        deleteContact.setOnClickListener();
    }
}
