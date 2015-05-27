package cse403.homesafe.CardViewUtility;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import cse403.homesafe.Data.Contacts;
import cse403.homesafe.Contacts.EditContactActivity;
import cse403.homesafe.R;

//holder to contain one contact
public class ContactViewHolder extends RecyclerView.ViewHolder {
    protected TextView name;
    protected TextView phone;
    protected TextView email;
    protected long cid;
    protected Contacts.Tier tier;
    protected int position;

    public ContactViewHolder(final View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.name);
        phone = (TextView) itemView.findViewById(R.id.phone);
        email = (TextView) itemView.findViewById(R.id.email);

        //click listener on this item, cardview listener
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), EditContactActivity.class);
                i.putExtra("CID", cid);
                i.putExtra("NAME", name.getText().toString());
                i.putExtra("PHONE", phone.getText().toString());
                i.putExtra("EMAIL", email.getText().toString());
                i.putExtra("POSITION", position);
                String tierNum;
                if(tier.equals(Contacts.Tier.ONE)){
                    tierNum = "1";
                } else if(tier.equals(Contacts.Tier.TWO)){
                    tierNum = "2";
                } else {
                    tierNum = "3";
                }
                i.putExtra("TIER", tierNum);
                //trigger EditContactActivity
                Activity activity = (Activity) v.getContext();
                i.putExtra("ACTIVITY", "EDIT");
                activity.startActivity(i);
                //finish current activity
                activity.finish();
            }
        });
    }
}