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
    public static final String CID = "CID";
    public static final String NAME = "NAME";
    public static final String PHONE = "PHONE";
    public static final String EMAIL = "EMAIL";
    public static final String POSITION = "POSITION";
    public static final String TIER = "TIER";
    public static final String ACTIVITY = "ACTIVITY";
    public static final String EDIT = "EDIT";
    public static final String ONE = "1";
    public static final String TWO = "2";
    public static final String THREE = "3";
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
                i.putExtra(CID, cid);
                i.putExtra(NAME, name.getText().toString());
                i.putExtra(PHONE, phone.getText().toString());
                i.putExtra(EMAIL, email.getText().toString());
                i.putExtra(POSITION, position);
                String tierNum;
                if(tier.equals(Contacts.Tier.ONE)){
                    tierNum = ONE;
                } else if(tier.equals(Contacts.Tier.TWO)){
                    tierNum = TWO;
                } else {
                    tierNum = THREE;
                }
                i.putExtra(TIER, tierNum);
                //trigger EditContactActivity
                Activity activity = (Activity) v.getContext();
                i.putExtra(ACTIVITY, EDIT);
                activity.startActivity(i);
                //finish current activity
                activity.finish();
            }
        });
    }
}