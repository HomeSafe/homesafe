package cse403.homesafe.Util;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * GoogleGPSUtils wraps the functionality of the GoogleApiClient
 * into a synchronous
 * If an activity needs to use GoogleGPSUtils, it should construct a GoogleGPSUtils in
 * its onStart() (in a non-UI thread), and call disconnect() in onStop()
 *
 * For example, in onStart, include:
 *
 * (new Thread() {
     public void run() {
             try {
                 gpsUtils = new GoogleGPSUtils(c);
             } catch (Exception e) {
                e.printStackTrace();
             }
         }
     }).start();

 * Where gpsUtils is a field of the Activity
 */
public class GoogleGPSUtils implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = "GoogleGPSUtils";
    private boolean isReady = false;
    private Context context;

    public GoogleGPSUtils(Context context) {
        this.context = context;

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    /**
     * Asynchronously connects to the GoogleApiClient.
     *
     */
    public void start()  {
        (new Thread() {
            public void run() {
                try {
                    if (mGoogleApiClient != null) {
                        Log.i(TAG, "Build Complete");

                        synchronized (this) {
                            mGoogleApiClient.connect();
                            Log.i(TAG, "Starting Connect");
                            this.wait(2000);
                            Log.i(TAG, "Done waiting");
                        }
                    } else {
                        Log.i(TAG, "Build Incomplete");
                    }
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * returns true if this GoogleGPSUtils object is ready to getLastLocation,
     * @return
     */
    public boolean isReady() {
        return isReady;
    }

    /**
     * Gets and returns the last location of the user.
     * @return an android Location object storing this device's last known location.
     */
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
        Log.i(TAG, "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "onConnectionFailed");
    }

    public void disconnect() {
        mGoogleApiClient.disconnect();
        synchronized (this) {
            this.notifyAll(); // if disconnected, let the constructor terminate
        }
        Log.i(TAG, "Disconnected");
    }
}
