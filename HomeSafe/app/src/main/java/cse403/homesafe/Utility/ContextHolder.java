package cse403.homesafe.Utility;

import android.content.Context;

/**
 * Holds Context so it can be accessed by other classes.
 * <br><br>
 * Created by dliuxy94 on 5/5/15.
 */
public class ContextHolder {
    private static Context _appContext = null; // Stored Context

    /**
     * Set Context in ContextHolder
     * @param appContext    Context to store
     */
    public static void setContext(Context appContext) {
        _appContext = appContext;
    }

    /**
     * Get Context stored
     * @return  Context object stored
     */
    public static Context getContext() {
        return _appContext;
    }
}
