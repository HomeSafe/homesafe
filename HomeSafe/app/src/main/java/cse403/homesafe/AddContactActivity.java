package cse403.homesafe;

import android.content.Intent;
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

import cse403.homesafe.Data.Contact;
import cse403.homesafe.Data.Contacts;
import cse403.homesafe.Data.DbFactory;
import cse403.homesafe.Data.HomeSafeDbHelper;


public class AddContactActivity extends ActionBarActivity {
    public static final String EMPTY_STR = "";
    Button discardChange;
    ImageView saveContact;
    HomeSafeDbHelper mDbHelper;
    Contacts mContactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        mDbHelper = new HomeSafeDbHelper(this);
        mContactList = Contacts.getInstance();
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayHomeAsUpEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.actionbar_custom, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title);
        mTitleTextView.setText("Add new contact");
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
                EditText mEditName = (EditText)findViewById(R.id.name_text);
                String nameStr = mEditName.getText().toString();
                EditText mEditPhone = (EditText)findViewById(R.id.phone_text);
                String phoneStr = mEditPhone.getText().toString();
                EditText mEditEmail = (EditText)findViewById(R.id.email_text);
                String emailStr = mEditEmail.getText().toString();
                EditText mEditTier = (EditText)findViewById(R.id.tier_text);
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
                    Contact contact = new Contact(nameStr, emailStr, phoneStr, tier);
                    mContactList.addContact(contact, tier);
                    DbFactory.addContactToDb(contact, mDbHelper);
                    Intent i = new Intent(AddContactActivity.this, ContactsActivity.class);
                    Toast.makeText(AddContactActivity.this, "Added Contact " + mEditName.getText(), Toast.LENGTH_SHORT).show();
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(AddContactActivity.this, "Missing Information", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
