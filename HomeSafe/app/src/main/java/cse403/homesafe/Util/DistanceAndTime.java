package cse403.homesafe.Util;

import android.location.Location;
import android.text.format.Time;

/**
 * Created by Vivek on 5/7/15.
 */
public class DistanceAndTime {
    private double distance;
    private Time time;

    private Location src;
    private Location dst;

    public DistanceAndTime(Location src, Location dst, double distance, Time time) {
        this.src = src;
        this.dst = dst;
        this.distance = distance;
        this.time = time;
    }

    /**
     * Returns the distance between the two locations in this
     * DistanceAndTime object
     * @return distance as a double
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Returns the estimated time beteen the two locations in this
     * DistanceAndTime object
     * @return time between locations
     */
    public Time getTime() {
        return time;
    }

    /**
     * Returns the Source (original location) of this
     * DistanceAndTime object.
     * @return source location
     */
    public Location getSrc() {
        return src;
    }

    /**
     * Returns the destination of this DistanceAndTime object
     * @return destination location
     */
    public Location getDst() {
        return dst;
    }
}
