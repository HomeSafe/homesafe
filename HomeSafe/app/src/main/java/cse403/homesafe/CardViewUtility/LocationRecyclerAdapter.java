package cse403.homesafe.CardViewUtility;

import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cse403.homesafe.R;


/**
 * Created by luluyi on 5/8/15.
 */
public class LocationRecyclerAdapter extends RecyclerView.Adapter<LocationViewHolder> {

    private List<Location> locations;

    public LocationRecyclerAdapter(List<Location> locations) {
        this.locations = new ArrayList<Location>();
        this.locations.addAll(locations);
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
        Location location = locations.get(i);
        //TODO I need the location name
        locationViewHolder.name.setText(location.getProvider());
//        contactViewHolder.contentText.setText(contact.getPhoneNumber());
//        contactViewHolder.card.setCardBackgroundColor(contact.getIntValue());
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }
}