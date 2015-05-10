package cse403.homesafe.CardViewUtility;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import cse403.homesafe.R;

public class ContactViewHolder extends RecyclerView.ViewHolder {

    protected TextView titleText;
    protected TextView phone;
    protected TextView email;

    public ContactViewHolder(View itemView) {
        super(itemView);
        titleText = (TextView) itemView.findViewById(R.id.name);
        phone = (TextView) itemView.findViewById(R.id.phone);
        email = (TextView) itemView.findViewById(R.id.email);
    }
}