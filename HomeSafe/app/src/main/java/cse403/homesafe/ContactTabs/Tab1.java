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

import java.util.ArrayList;

import cse403.homesafe.CardViewUtility.MyRecyclerAdapter;
import cse403.homesafe.Data.Contact;
import cse403.homesafe.R;

public class Tab1 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_card, container, false);
        final FragmentActivity c = getActivity();
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(c);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new MyRecyclerAdapter(generateContacts()));


        return view;
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_test);
//
//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerList);
//        LinearLayoutManager llm = new LinearLayoutManager(this);
//        llm.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(llm);
//
//        recyclerView.setAdapter(new MyRecyclerAdapter(generateContacts()));
//    }

    private ArrayList<Contact> generateContacts() {
        ArrayList<Contact> palettes = new ArrayList<>();
        palettes.add(new Contact("Luyi"));
        palettes.add(new Contact("Luyi"));
        palettes.add(new Contact("Luyi"));
        palettes.add(new Contact("Luyi"));
        palettes.add(new Contact("Luyi"));
        palettes.add(new Contact("Luyi"));
        palettes.add(new Contact("Luyi"));
        return palettes;
    }
}
