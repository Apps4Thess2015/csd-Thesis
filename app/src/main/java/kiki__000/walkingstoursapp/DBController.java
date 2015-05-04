package kiki__000.walkingstoursapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by kiki__000 on 21-Apr-15.
 */
public class DBController extends SQLiteOpenHelper {

    public DBController(Context applicationcontext) {
        super(applicationcontext, "walks.db", null, 2);
    }
    //Creates Table
    @Override
    public void onCreate(SQLiteDatabase database) {
        String query;
        query = "CREATE TABLE walks ( id TEXT, name TEXT, date TEXT, time TEXT, venue TEXT, kind TEXT, guide TEXT, description TEXT, stations INTEGER, status INTEGER)";
        database.execSQL(query);
    }
    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
        String query;
        query = "DROP TABLE IF EXISTS walks";
        database.execSQL(query);
        onCreate(database);
    }

    /**
     * Inserts Walk into SQLite DB
     * @param queryValues
     */
    public void insertWalk(Walk queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", queryValues.getId());
        values.put("name", queryValues.getName());
        values.put("date", queryValues.getDate());
        values.put("time", queryValues.getTime());
        values.put("venue", queryValues.getVenue());
        values.put("kind", queryValues.getKind());
        values.put("guide", queryValues.getGuide());
        values.put("description", queryValues.getDescription());
        values.put("stations" ,queryValues.getStations());
        values.put("status", queryValues.getStatus());
        database.insert("walks", null, values);
        database.close();
    }

    /**
     * Get list of Walks from SQLite DB as Array List
     * @return
     */
    public ArrayList<Walk> getAllWalks() {
        ArrayList<Walk> walksList;
        walksList = new ArrayList<Walk>();
        String selectQuery = "SELECT  * FROM walks";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            for (String s : cursor.getColumnNames()) {
                Log.i("name ", s);
            }
            ;
            do {
                Walk walk = new Walk();
                walk.setId(cursor.getString(cursor.getColumnIndex("id")));
                walk.setName(cursor.getString(cursor.getColumnIndex("name")));
                walk.setDate(cursor.getString(cursor.getColumnIndex("date")));
                walk.setTime(cursor.getString(cursor.getColumnIndex("time")));
                walk.setVenue(cursor.getString(cursor.getColumnIndex("venue")));
                walk.setKind(cursor.getString(cursor.getColumnIndex("kind")));
                walk.setGuide(cursor.getString(cursor.getColumnIndex("guide")));
                walk.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                walk.setStations(cursor.getInt(cursor.getColumnIndex("stations")));
                walk.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
                walksList.add(walk);

            } while (cursor.moveToNext());
        }
        database.close();
        return  walksList;
    }
}
