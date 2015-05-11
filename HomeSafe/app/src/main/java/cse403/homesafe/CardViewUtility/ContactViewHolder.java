package cse403.homesafe.CardViewUtility;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import cse403.homesafe.EditContactActivity;
import cse403.homesafe.R;

public class ContactViewHolder extends RecyclerView.ViewHolder {

    protected TextView name;
    protected TextView phone;
    protected TextView email;
    protected TextView tier;

    public ContactViewHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.name);
        phone = (TextView) itemView.findViewById(R.id.phone);
        email = (TextView) itemView.findViewById(R.id.email);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // item clicked
                Intent i = new Intent(v.getContext(), EditContactActivity.class);
//                Toast.makeText(v.getContext(), name.getText().toString(), Toast.LENGTH_SHORT).show();
                i.putExtra("NAME", name.getText().toString());
                i.putExtra("PHONE", phone.getText().toString());
                i.putExtra("EMAIL", email.getText().toString());
                v.getContext().startActivity(i);
            }
        });
    }
}