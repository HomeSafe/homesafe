package cse403.homesafe;

import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import cse403.homesafe.Data.Contact;
import cse403.homesafe.Data.Contacts;
import cse403.homesafe.Messaging.Email;


public class ArrivalScreenActivity extends ActionBarActivity {
    // TODO Need to populate the contact list from the first tier contacts
    private RecyclerView.Adapter rvAdapter;

    private final String[] contactsList = {"Becky"};
    private Button homescreenBtn;
    private ArrayList<String> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrival_screen);

        RecyclerView contactsView = (RecyclerView) findViewById(R.id.contactsView);
        homescreenBtn = (Button) findViewById(R.id.homescreenBtn);

        backToStartScreen();

        // use this setting to improve performance if you know that changes
        // in content to do change the layout size of the RecyclerView
        contactsView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager rvLayoutManager = new LinearLayoutManager(this);
        contactsView.setLayoutManager(rvLayoutManager);

        // TODO send out all the emails or SMSs??
        Email mailer = Email.getInstance();
//        Contacts c = Contacts.getInstance();     // TODO This needs to be the one contacts list belonging to the user
//        for (Contact contact : c.getContactsInTier(Contacts.Tier.ONE)) {
//            contacts.add(contact.getName());
//            mailer.sendMessage(contact, new Location("providername"), "Becky has arrived successfully at her destination");
//        }
        Contact Becky = new Contact("Becky", "spethous@gmail.com", "7735241294", Contacts.Tier.ONE);
        mailer.sendMessage(Becky, new Location("Google maps"), "Becky has successfully arrived at her destination");

        // specify an adaoter
        rvAdapter = new ArrivalScreenAdapter(contactsList);
        contactsView.setAdapter(rvAdapter);
    }

    /* Ends the timer for the trip, taking the user automatically to the arrival screen.
* */
    private void backToStartScreen() {
        homescreenBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
