package cse403.homesafe.Data;

import android.provider.BaseColumns;

/**
 * Created by Ethan on 5/5/15.
 * This class is a container for constants that define
 * names for tables, and columns. Each inner class is definition of a table
 */
public final class HomeSafeContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public HomeSafeContract() {}

    /* Inner class that defines the table 'Contact' */
    public static abstract class ContactEntry implements BaseColumns {
        public static final String TABLE_NAME = "Contact";
        public static final String COLUMN_CID = "cid";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_TIER = "tier";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PHONE = "phone";

    }

    /* Inner class that defines the table 'Location' */
    public static abstract class LocationEntry implements BaseColumns {
        public static final String TABLE_NAME = "Location";
        public static final String COLUMN_LID = "lid";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_LAT = "latitude";
        public static final String COLUMN_LNG = "longitude";
        public static final String COLUMN_ADDRESS = "address";
    }
}
