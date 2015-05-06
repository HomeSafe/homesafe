package cse403.homesafe.CardViewUtility;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cse403.homesafe.Data.Contact;
import cse403.homesafe.R;

public class MyRecyclerAdapter extends RecyclerView.Adapter<ContactViewHolder> {

    private List<Contact> contacts;

    public MyRecyclerAdapter(List<Contact> contacts) {
        this.contacts = new ArrayList<Contact>();
        this.contacts.addAll(contacts);
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_view, viewGroup, false);

        return new ContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        Contact contact = contacts.get(i);
        contactViewHolder.titleText.setText(contact.getName());
//        contactViewHolder.contentText.setText(contact.getPhoneNumber());
//        contactViewHolder.card.setCardBackgroundColor(contact.getIntValue());
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }
}