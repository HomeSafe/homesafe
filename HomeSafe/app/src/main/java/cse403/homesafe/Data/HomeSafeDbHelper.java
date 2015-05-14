package cse403.homesafe.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;

/**
 * Created by Ethan on 5/5/15.
 */
public class HomeSafeDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "HomeSafe.db";


    // SQL query
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    // CREATE TABLE
    private static final String SQL_CREATE_TABLE_CONTACT = "CREATE TABLE " + HomeSafeContract.ContactEntry.TABLE_NAME + " ("
                                                        + HomeSafeContract.ContactEntry._ID + " INTEGER PRIMARY KEY,"
                                                        + HomeSafeContract.ContactEntry.COLUMN_CID + INT_TYPE + COMMA_SEP
                                                        + HomeSafeContract.ContactEntry.COLUMN_TIER + INT_TYPE + COMMA_SEP
                                                        + HomeSafeContract.ContactEntry.COLUMN_NAME + TEXT_TYPE + COMMA_SEP
                                                        + HomeSafeContract.ContactEntry.COLUMN_EMAIL + TEXT_TYPE + COMMA_SEP
                                                        + HomeSafeContract.ContactEntry.COLUMN_PHONE + TEXT_TYPE + " )";

    private static final String SQL_CREATE_TABLE_LOCATION = "CREATE TABLE " + HomeSafeContract.LocationEntry.TABLE_NAME + " ("
                                                        + HomeSafeContract.LocationEntry._ID + " INTEGER PRIMARY KEY,"
                                                        + HomeSafeContract.LocationEntry.COLUMN_LID + INT_TYPE + COMMA_SEP
                                                        + HomeSafeContract.LocationEntry.COLUMN_NAME + TEXT_TYPE + COMMA_SEP
                                                        + HomeSafeContract.LocationEntry.COLUMN_ADDRESS + TEXT_TYPE + COMMA_SEP
                                                        + HomeSafeContract.LocationEntry.COLUMN_LAT + TEXT_TYPE + COMMA_SEP
                                                        + HomeSafeContract.LocationEntry.COLUMN_LNG + TEXT_TYPE + " )";


    public HomeSafeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_CONTACT);
        db.execSQL(SQL_CREATE_TABLE_LOCATION);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
