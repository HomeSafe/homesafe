package cse403.homesafe.Data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.util.Log;

import static cse403.homesafe.Data.HomeSafeContract.*;

/**
 * Created by yellowleaf on 5/10/15.
 */
public class DbFactory {
    private static final String TAG = "DataBase Factory";
    private static Contacts mContactList = Contacts.getInstance();
    private static Destinations mDestinationList = Destinations.getInstance();

    public static boolean addContactToDb(Contact contact, HomeSafeDbHelper mDbHelper){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ContactEntry.COLUMN_NAME, contact.getName());
        values.put(ContactEntry.COLUMN_EMAIL, contact.getEmail());
        values.put(ContactEntry.COLUMN_PHONE, contact.getPhoneNumber());
        values.put(ContactEntry.COLUMN_TIER, contact.getTier().name());

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                ContactEntry.TABLE_NAME,
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

    public static boolean retrieveFromDb(HomeSafeDbHelper mDbHelper) {
        try {
            SQLiteDatabase db = mDbHelper.getReadableDatabase();
            Cursor c = db.query(
                    ContactEntry.TABLE_NAME,  // The table to query
                    null,                       // The columns to return
                    null,                                // The columns for the WHERE clause
                    null,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );

            while (c.moveToNext()) {
                long id = c.getLong(c.getColumnIndexOrThrow(ContactEntry._ID));
                String name = c.getString(c.getColumnIndexOrThrow(ContactEntry.COLUMN_NAME));
                String email = c.getString(c.getColumnIndexOrThrow(ContactEntry.COLUMN_EMAIL));
                String phone = c.getString(c.getColumnIndexOrThrow(ContactEntry.COLUMN_PHONE));
                String TierString = c.getString(c.getColumnIndexOrThrow(ContactEntry.COLUMN_TIER));
                Contacts.Tier tier = Contacts.Tier.valueOf(TierString);
                Contact newContact = new Contact(name, email, phone, tier);
                newContact.setCid(id);
                mContactList.addContact(newContact, newContact.getTier());
            }
            Log.d(TAG, "Retrieve contacts info from Database successfully");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Retrieve contacts info from Database failed");
            e.printStackTrace();
            return false;

        }

    }

    public static void updateContact(Contact contact, HomeSafeDbHelper mDbHelper) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(ContactEntry.COLUMN_NAME, contact.getName());
        values.put(ContactEntry.COLUMN_EMAIL, contact.getEmail());
        values.put(ContactEntry.COLUMN_PHONE, contact.getPhoneNumber());
        values.put(ContactEntry.COLUMN_TIER, contact.getTier().name());

        // Which row to update, based on the ID
        String selection = ContactEntry._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(contact.getCid()) };

        int count = db.update(
                ContactEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }
}
