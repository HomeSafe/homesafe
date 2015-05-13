package cse403.homesafe.Util;

/**
 * Created by Vivek on 5/10/15.
 * Interface for receiving callbacks from GoogleMapsUtils when their network
 * connection results finish.
 */
public interface GoogleMapsUtilsCallback {
    void onGetDistanceAndTime(Object obj);
    void onAddressToLocation(Object obj);
}
