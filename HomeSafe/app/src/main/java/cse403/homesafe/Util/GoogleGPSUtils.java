package cse403.homesafe.Util;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

import cse403.homesafe.ArrivalAdapter;
import cse403.homesafe.Data.Contact;
import cse403.homesafe.Data.Contacts;
import cse403.homesafe.Messaging.Messenger;

/**
 * Created by dliuxy94 on 5/24/15.
 */
public class GoogleGPSUtils implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = "GoogleGPSUtils";
    private boolean isReady = false;

    public GoogleGPSUtils(Context context) throws Exception {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        if (mGoogleApiClient != null) {
            Log.i(TAG, "Build Complete");

            synchronized (this) {
                mGoogleApiClient.connect();
//                mGoogleApiClient.wait();
//                this.wait();
                Log.i(TAG, "Starting Connect");
            }
        } else {
            Log.i(TAG, "Build Incomplete");
        }
    }

    public boolean alertContacts(Context context, int currentTier) throws Exception {
        return alertContacts(null, null, context, currentTier);
    }

    public boolean alertContacts(RecyclerView contactsView, RecyclerView.Adapter rvAdapter, Context context, int currentTier) throws Exception {
//        if (!isReady) {
//            mGoogleApiClient.wait();
//        }

        Log.e(TAG, "alerting contacts, starting main method " + isReady);
        if (!isReady) {
            return false;
        }

        Log.e(TAG, "alerting contacts");
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            Log.e(TAG, "Connected!");
            Contacts.Tier tier;
            ArrayList<Contact> contacts;
            switch (currentTier) {
                case 1:
                    tier = Contacts.Tier.ONE;
                    contacts = new ArrayList<>(Contacts.getInstance().getContactsInTier(Contacts.Tier.ONE));
                    break;
                case 2:
                    tier = Contacts.Tier.TWO;
                    contacts = new ArrayList<>(Contacts.getInstance().getContactsInTier(Contacts.Tier.TWO));
                    break;
                case 3:
                    tier = Contacts.Tier.THREE;
                    contacts = new ArrayList<>(Contacts.getInstance().getContactsInTier(Contacts.Tier.THREE));
                    break;
                default:
                    tier = Contacts.Tier.ONE;
                    contacts = new ArrayList<>(Contacts.getInstance().getContactsInTier(Contacts.Tier.ONE));
            }

            Messenger.sendNotifications(tier, mLastLocation, context, Messenger.MessageType.DANGER);

            if (rvAdapter != null && contactsView != null) {
                rvAdapter = new ArrivalAdapter(contacts);
                contactsView.setAdapter(rvAdapter);
            }
        }

        return isReady;
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "onConnect entered");
        synchronized (this) {
            isReady = true;
            this.notifyAll();
            Log.i(TAG, "Connected");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
