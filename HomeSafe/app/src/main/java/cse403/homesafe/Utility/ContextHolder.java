package cse403.homesafe.Utility;

import android.content.Context;

/**
 * Holder object that holds Context so it can be accessed by other classes.
 * <br><br>
 * Created by dliuxy94 on 5/5/15.
 */
public class ContextHolder {
    private static final ContextHolder _instance = new ContextHolder(); // Instance of ContextHolder
    private Context _appContext = null; // Stored Context

    /**
     * Constructor for ContextHolder
     */
    private ContextHolder() {

    }

    /**
     * Get the singleton instance of ContextHolder
     * @return ContextHolder instance
     */
    public static ContextHolder getInstance() {
        return _instance;
    }

    /**
     * Set Context in ContextHolder
     * @param appContext    Context to store
     */
    public void setContext(Context appContext) {
        _appContext = appContext;
    }

    /**
     * Get Context stored
     * @return  Context object stored
     */
    public Context getContext() {
        return _appContext;
    }
}
