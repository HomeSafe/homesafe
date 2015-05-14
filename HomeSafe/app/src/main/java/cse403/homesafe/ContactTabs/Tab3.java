package cse403.homesafe.ContactTabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cse403.homesafe.CardViewUtility.ContactRecyclerAdapter;
import cse403.homesafe.Data.Contacts;
import cse403.homesafe.R;

/**
 * Created by luluyi on 5/10/15.
 */
public class Tab3 extends Fragment {
    Contacts mContactsList;
    RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_card, container, false);
        final FragmentActivity c = getActivity();
        this.mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerList);
        mContactsList = mContactsList.getInstance();
        LinearLayoutManager layoutManager = new LinearLayoutManager(c);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        this.mRecyclerView.setLayoutManager(layoutManager);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.mRecyclerView.setAdapter(
                new ContactRecyclerAdapter(mContactsList.getContactsInTier(Contacts.Tier.THREE))
        );
    }
}