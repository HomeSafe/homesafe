package cse403.homesafe.CardViewUtility;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import cse403.homesafe.Data.Destination;
import cse403.homesafe.R;


/*
 * This is the Adapter class that holds the LocationViewHolder
 */
public class LocationRecyclerAdapter extends RecyclerView.Adapter<LocationViewHolder> {
    private List<Destination> destinations; //the destinations to fill

    public LocationRecyclerAdapter(List<Destination> locations) {
        this.destinations = new ArrayList<Destination>();
        this.destinations.addAll(locations);
    }

    @Override
    public LocationViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.location_card_view, viewGroup, false);
        return new LocationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LocationViewHolder locationViewHolder, int i) {
        Destination destination = destinations.get(i);
        locationViewHolder.name.setText(destination.getName());
        locationViewHolder.address.setText(destination.getAddress());
        locationViewHolder.did = destination.getDid();
    }

    @Override
    public int getItemCount() {
        return destinations.size();
    }
}