package cse403.homesafe;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ArrivalScreenAdapter extends RecyclerView.Adapter<ArrivalScreenAdapter.ViewHolder> {
    private String[] dataset;
    // TODO private List<ContactInfo> contacts;

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

    // Provide a suitable constructor (depends on the kind of dataset)
    public ArrivalScreenAdapter(String[] dataset) {
        this.dataset = dataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ArrivalScreenAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.arrival_screen_card_view, parent, false);
        // set the view's size, margins, paddings, and layout parameters

        return new ArrivalScreenAdapter.ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ArrivalScreenAdapter.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.name.setText(dataset[position] + " has been notified");
    }

    @Override
    public int getItemCount() {
        return dataset.length;
    }
}
