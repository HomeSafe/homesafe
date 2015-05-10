package cse403.homesafe.Data;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

/**
 * Created by yellowleaf on 5/10/15.
 */
public class DbFactory {
    HomeSafeDbHelper mDbHelper;

    public static boolean addContactToDb(Contact contact, HomeSafeDbHelper mDbHelper){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(HomeSafeContract.ContactEntry.COLUMN_NAME, contact.getName());
        values.put(HomeSafeContract.ContactEntry.COLUMN_EMAIL, contact.getEmail());
        values.put(HomeSafeContract.ContactEntry.COLUMN_PHONE, contact.getPhoneNumber());
        values.put(HomeSafeContract.ContactEntry.COLUMN_TIER, contact.getTier().name());

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                HomeSafeContract.ContactEntry.TABLE_NAME,
                null,
                values);
        contact.setCid(newRowId);
        return true;
    }

    public static boolean addLocationToDb(Location location, HomeSafeDbHelper mDbHelper) {
        //TODO Need to modify location to have a name
        /*
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(HomeSafeContract.LocationEntry.COLUMN_NAME, location.getName());
        values.put(HomeSafeContract.ContactEntry.COLUMN_EMAIL, contact.getEmail());
        values.put(HomeSafeContract.ContactEntry.COLUMN_PHONE, contact.getPhoneNumber());
        values.put(HomeSafeContract.ContactEntry.COLUMN_TIER, contact.getTier().name());

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                HomeSafeContract.ContactEntry.TABLE_NAME,
                null,
                values);
        contact.setCid(newRowId);
        return true;
        */
        return true;
    }
}
