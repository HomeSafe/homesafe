package cse403.homesafe.Util;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * If an activity need to use GoogleGPSUtils, it should construct a GoogleGPSUtils in
 * its onStart(), and call disconnect() in onStop()
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
                Log.i(TAG, "Starting Connect");
                this.wait(2000);
            }
        } else {
            Log.i(TAG, "Build Incomplete");
        }
    }

    public Location getLastLocation() {
        Log.e(TAG, "getLastLocation, checking is ready: " + isReady);
        if (!isReady) {
            return null;
        }

        Log.e(TAG, "getting last location");
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        return mLastLocation;
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

    public void disconnect() {
        mGoogleApiClient.disconnect();
        Log.i(TAG, "Disconnected");
    }
}
