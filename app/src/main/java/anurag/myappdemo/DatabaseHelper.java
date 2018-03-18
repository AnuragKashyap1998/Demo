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

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Tourism2";
    private static final String TABLE_CONTACTS = "Guide";
    private static final String KEY_ID="regno";
    private static final String KEY_NAME= "name";
    private static final String KEY_AREA = "area";
    private static final String KEY_MOB_NO = "mobile_no";
    private static final String KEY_PRICE= "package_price";
    private static final String KEY_RATING="rating";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        /*String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " TEXT," + KEY_NAME + " TEXT,"
                + KEY_MOB_NO + " TEXT," +KEY_AREA + " TEXT,"+ KEY_PRICE + " TEXT,"+ KEY_RATING+"float"+")";
        db.execSQL(CREATE_CONTACTS_TABLE);
        */
        db.execSQL("create table guide(regno text, name text, mobile_no text, area text,package_price text, rating float)");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        // Create tables again
        onCreate(db);
    }
    public Map<String, Object> toMap(Guide guide) {
        HashMap<String, Object> values = new HashMap<>();
        values.put(KEY_ID, guide.getRegno());
        values.put(KEY_NAME, guide.getName());
        values.put(KEY_MOB_NO, guide.getMobile_no());
        values.put(KEY_PRICE, guide.getPackage_price());
        values.put(KEY_AREA, guide.getArea());
        values.put(KEY_RATING,guide.getRating());

        return values;
    }
    // code to add the new guide
    boolean addGuide(Guide guide) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ID, guide.getRegno());
        values.put(KEY_NAME, guide.getName());
        values.put(KEY_MOB_NO, guide.getMobile_no());
        values.put(KEY_PRICE, guide.getPackage_price());
        values.put(KEY_AREA, guide.getArea());
        values.put(KEY_RATING,guide.getRating());

        ///////////////////


        // Inserting Row
        long res=db.insert(TABLE_CONTACTS, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
        if (res!=-1)
            return  true;
        else return false;
    }

    // code to get the single guide
    Guide getGuide(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
                        KEY_NAME, KEY_MOB_NO ,KEY_AREA,KEY_PRICE,KEY_RATING}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Guide guide = new Guide(cursor.getString(0),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),cursor.getFloat(5));
        // return guide
        return guide;
    }

    // code to get all guides in a list view
    public ArrayList<Guide> getAllGuide() {
        ArrayList<Guide> guideList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Guide guide = new Guide();
                guide.setRegno(cursor.getString(0));
                guide.setName(cursor.getString(1));
                guide.setMobile_no(cursor.getString(2));
                guide.setArea(cursor.getString(3));
                guide.setPackage_price(cursor.getString(4));
                guide.setRating(cursor.getFloat(5));
                // Adding guide to list
                guideList.add(guide);
            } while (cursor.moveToNext());
        }

        // return guide list
        return guideList;
    }

    // code to update the single guide
    public int updateGuide(Guide guide) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, guide.getRegno());
        values.put(KEY_NAME, guide.getName());
        values.put(KEY_MOB_NO, guide.getMobile_no());
        values.put(KEY_PRICE, guide.getPackage_price());
        values.put(KEY_AREA, guide.getArea());
        values.put(KEY_RATING,guide.getRating());

        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(guide.getRegno()) });
    }

    // Deleting single guide
    public void deleteGuide(Guide guide) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[] { String.valueOf(guide.getRegno()) });
        db.close();
    }

    // Getting guide Count
    public int getGuideCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}

