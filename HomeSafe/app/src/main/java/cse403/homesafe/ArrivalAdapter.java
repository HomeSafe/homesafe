package cse403.homesafe;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cse403.homesafe.Data.Contact;

public class ArrivalAdapter extends RecyclerView.Adapter<ArrivalAdapter.ViewHolder> {
     private List<Contact> contacts;

    // Provide a reference to the views for each data item
    // complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView name;
        // TODO icon next to each name?? What about phone number?
        public ViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.txtName);
        }
    }

    /** Makes a new ArrivalAdapter which will display a list of contacts
     * @param contacts The list of contacts to be displayed
     */
    public ArrivalAdapter(List<Contact> contacts) {
        this.contacts = Collections.unmodifiableList(contacts);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ArrivalAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.arrival_screen_card_view, parent, false);
        // set the view's size, margins, paddings, and layout parameters

        return new ArrivalAdapter.ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ArrivalAdapter.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
//        holder.name.setText(dataset[position] + " has been notified");
        holder.name.setText(contacts.get(position).getName() + " has been notified");
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }
}
