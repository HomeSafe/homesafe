package cse403.homesafe.CardViewUtility;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import cse403.homesafe.Data.Contact;
import cse403.homesafe.R;

/*
 * This is the Adapter class that holds the ContactViewHolder
 */
public class ContactRecyclerAdapter extends RecyclerView.Adapter<ContactViewHolder> {
    private List<Contact> contacts; //the contacts to fill

    public ContactRecyclerAdapter(List<Contact> contacts) {
        this.contacts = new ArrayList<Contact>();
        this.contacts.addAll(contacts);
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.contact_card_view, viewGroup, false);
        return new ContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        Contact contact = contacts.get(i);
        contactViewHolder.tier = contact.getTier();
        contactViewHolder.name.setText(contact.getName());
        contactViewHolder.phone.setText(contact.getPhoneNumber());
        contactViewHolder.email.setText(contact.getEmail());
        contactViewHolder.cid = contact.getCid();
        contactViewHolder.position = i;
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }
}