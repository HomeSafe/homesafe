package cse403.homesafe.CardViewUtility;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import cse403.homesafe.Destinations.EditDestinationActivity;
import cse403.homesafe.R;

/**
 * Created by luluyi on 5/10/15.
 */
//holder to contain one destination
public class LocationViewHolder extends RecyclerView.ViewHolder {

    protected TextView name;
    protected TextView address;
    protected long did;

    public LocationViewHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.name);
        address = (TextView) itemView.findViewById(R.id.address);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent i = new Intent(v.getContext(), EditDestinationActivity.class);
                i.putExtra("NAME", name.getText().toString());
                i.putExtra("ADDRESS", address.getText().toString());
                i.putExtra("DID", did);
                //trigger EditLocationActivity
                Activity activity = (Activity) v.getContext();
                activity.startActivity(i);
                //finish current activity
                activity.finish();
            }
        });
    }
}