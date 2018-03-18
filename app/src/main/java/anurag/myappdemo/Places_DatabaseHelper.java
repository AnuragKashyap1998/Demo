package anurag.myappdemo;

/**
 * Created by anurag on 18-03-2018.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Er.Deepak_kumar on 16-03-2018.
 */

public class Places_DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Tourism";
    private static final String TABLE_CONTACTS = "PLACES";
    private static final String KEY_NAME= "name";
    private static final String KEY_URL1= "url1";
    private static final String KEY_URL2= "url2";
    private static final String KEY_URL3= "url3";

    private static final String KEY_PLACE = "place";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_TYPE="type";
    private static final String KEY_DESCRIPTION="description";

    public Places_DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_NAME + " TEXT,"
                +KEY_PLACE + " TEXT,"
                + KEY_TYPE + " TEXT,description TEXT,latitude text,longitude text,url1 text,url2 text,url3 text)";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        // Create tables again
        onCreate(db);
    }
    public Map<String, Object> toMap(Places places) {
        HashMap<String, Object> values = new HashMap<>();
        values.put(KEY_NAME, places.getName());
        values.put(KEY_LATITUDE, places.getLatitude());
        values.put(KEY_LONGITUDE, places.getLongitude());
        values.put(KEY_TYPE, places.getType());
        values.put(KEY_PLACE, places.getPlace());
        values.put(KEY_DESCRIPTION,places.getDescription());
        values.put(KEY_URL1,places.getUrl1());
        values.put(KEY_URL2,places.getUrl2());
        values.put(KEY_URL3,places.getUrl3());

        return values;
    }
    // code to add the new places
    boolean addPlaces(Places places) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_NAME, places.getName());
        values.put(KEY_LATITUDE, places.getLatitude());
        values.put(KEY_LONGITUDE, places.getLongitude());
        values.put(KEY_TYPE, places.getType());
        values.put(KEY_PLACE, places.getPlace());
        values.put(KEY_DESCRIPTION,places.getDescription());
        values.put(KEY_URL1,places.getUrl1());
        values.put(KEY_URL2,places.getUrl2());
        values.put(KEY_URL3,places.getUrl3());

        ///////////////////


        // Inserting Row
        long res=db.insert(TABLE_CONTACTS, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
        if (res!=-1)
            return  true;
        else return false;
    }

    // code to get the single places
    Places getPlaces(String name) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[] {
                        KEY_NAME,KEY_PLACE, KEY_TYPE,KEY_DESCRIPTION,KEY_LATITUDE,KEY_LONGITUDE,KEY_URL1,KEY_URL2,KEY_URL3 }, KEY_NAME + "=?",
                new String[] { String.valueOf(name) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Places places = new Places(cursor.getString(0),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7),cursor.getString(8));
        // return places
        return places;
    }

    // code to get all placess in a list view
    public ArrayList<Places> getAllPlaces() {
        ArrayList<Places> placesList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Places places = new Places();
                places.setName(cursor.getString(0));
                places.setPlace(cursor.getString(1));
                places.setType(cursor.getString(2));
                places.setDescription(cursor.getString(3));
                places.setLatitude(cursor.getString(4));
                places.setLongitude(cursor.getString(5));
                places.setUrl1(cursor.getString(6));
                places.setUrl2(cursor.getString(7));
                places.setUrl3(cursor.getString(8));
                // Adding places to list
                placesList.add(places);
            } while (cursor.moveToNext());
        }

        // return places list
        return placesList;
    }

    // code to update the single places
    public int updatePlaces(Places places) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, places.getName());
        values.put(KEY_LATITUDE, places.getLatitude());
        values.put(KEY_LONGITUDE, places.getLongitude());
        values.put(KEY_TYPE, places.getType());
        values.put(KEY_PLACE, places.getPlace());
        values.put(KEY_DESCRIPTION,places.getDescription());
        values.put(KEY_URL1,places.getUrl1());
        values.put(KEY_URL2,places.getUrl2());
        values.put(KEY_URL3,places.getUrl3());

        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_NAME + " = ?",
                new String[] { String.valueOf(places.getName()) });
    }

    // Deleting single places
    public void deletePlaces(Places places) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_NAME + " = ?",
                new String[] { String.valueOf(places.getName()) });
        db.close();
    }

    // Getting places Count
    public int getPlacesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}

