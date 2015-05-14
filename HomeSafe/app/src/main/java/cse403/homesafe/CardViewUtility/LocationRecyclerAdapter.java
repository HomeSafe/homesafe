package cse403.homesafe.CardViewUtility;

import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cse403.homesafe.Data.Destination;
import cse403.homesafe.R;


/**
 * Created by luluyi on 5/8/15.
 */
public class LocationRecyclerAdapter extends RecyclerView.Adapter<LocationViewHolder> {

    private List<Destination> destinations;

    public LocationRecyclerAdapter(List<Destination> locations) {
        this.destinations = new ArrayList<Destination>();
        this.destinations.addAll(destinations);
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
        //TODO I need the location name
        locationViewHolder.name.setText(destination.getName());
//        locationViewHolder.address.setText(destination);
    }

    @Override
    public int getItemCount() {
        return destinations.size();
    }
}