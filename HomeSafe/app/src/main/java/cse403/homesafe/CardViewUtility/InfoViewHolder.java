package cse403.homesafe.CardViewUtility;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import cse403.homesafe.R;

public class InfoViewHolder extends RecyclerView.ViewHolder {

    protected TextView titleText;
    protected TextView contentText;
    protected CardView card;

    public InfoViewHolder(View itemView) {
        super(itemView);
        titleText = (TextView) itemView.findViewById(R.id.name);
        //add more attribute here
        card = (CardView) itemView;
    }
}