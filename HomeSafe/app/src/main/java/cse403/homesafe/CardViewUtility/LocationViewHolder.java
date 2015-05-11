package cse403.homesafe.CardViewUtility;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import cse403.homesafe.EditLocationActivity;
import cse403.homesafe.R;

/**
 * Created by luluyi on 5/10/15.
 */
public class LocationViewHolder extends RecyclerView.ViewHolder {

    protected TextView name;
    protected TextView address;

    public LocationViewHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.name);
        address = (TextView) itemView.findViewById(R.id.address);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent i = new Intent(v.getContext(), EditLocationActivity.class);
                i.putExtra("NAME", name.getText().toString());
                i.putExtra("ADDRESS", name.getText().toString());
                v.getContext().startActivity(i);
            }
        });
    }
}