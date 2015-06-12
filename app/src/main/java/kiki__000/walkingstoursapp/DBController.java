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

    //database Version number
    private int dbVersion;
    //columns' names of table walks
    private static final String[] COLUMNSWalks = {"id", "name", "date", "time", "venue", "kind", "guide", "description", "stations", "status"};
    //columns' names of table stations
    private static final String[] COLUMNSStations = {"id", "title", "description", "lat", "lng", "walkId"};
    //columns' names of table photos
    private static final String[] COLUMNSPhotos = {"id", "stationId", "walkId", "image"};
    //set the string walks as walksG for greek or walksE for english
    public static String walks;
    //set the string stations as stationsG for greek or stationsE for english
    public static String stations;

    public DBController(Context applicationcontext) {
        super(applicationcontext, "walks.db", null, 35);
    }

    //Create Table
    @Override
    public void onCreate(SQLiteDatabase database) {
        String query;

        //create table walksG (table walks with greek content)
        query = "CREATE TABLE walksG ( id TEXT, name TEXT, date TEXT, time TEXT, venue TEXT, kind TEXT, guide TEXT, description TEXT, stations INTEGER, status INTEGER)";
        database.execSQL(query);

        //create table stationsG (table stations with greek content)
        query = "CREATE TABLE stationsG ( id TEXT, title TEXT, description TEXT, lat DOUBLE, lng DOUBLE, walkId TEXT)";
        database.execSQL(query);

        //create table walksE (table walks with english content)
        query = "CREATE TABLE walksE ( id TEXT, name TEXT, date TEXT, time TEXT, venue TEXT, kind TEXT, guide TEXT, description TEXT, stations INTEGER, status INTEGER)";
        database.execSQL(query);

        //create table stationsE (table stations with english content)
        query = "CREATE TABLE stationsE ( id TEXT, title TEXT, description TEXT, lat DOUBLE, lng DOUBLE, walkId TEXT)";
        database.execSQL(query);

        //create table photos
        query = "CREATE TABLE photos ( id TEXT, stationId TEXT, walkId TEXT,  image TEXT)";
        database.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
        String query;

        query = "DROP TABLE IF EXISTS walksG";
        database.execSQL(query);

        query = "DROP TABLE IF EXISTS stationsG";
        database.execSQL(query);

        query = "DROP TABLE IF EXISTS walksE";
        database.execSQL(query);

        query = "DROP TABLE IF EXISTS stationsE";
        database.execSQL(query);

        query = "DROP TABLE IF EXISTS photos";
        database.execSQL(query);

        onCreate(database);
    }

    /**
     * increase dbVersion by one
     * when the user update the local database
     * in order not to be there duplicates
     * */
    public void increaseDbVersion(){ dbVersion++; }

    /**
     * get the database version number
     *
     * @return dbVersion
     * */
    public int getDbVersion(){return dbVersion;}

    /**
     * Inserts Walk into SQLite DB with this lang
     * @param queryValues
     */
    public void insertWalk(Walk queryValues, String lang) {

        String table;

        if (lang == "gr"){
            table = "walksG";
        }
        else{
            table = "walksE";
        }

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
        values.put("stations", queryValues.getStations());
        values.put("status", queryValues.getStatus());
        database.insert(table, null, values);
        database.close();
    }

    /**
     * Inserts Station into SQLite DB with this lang
     * @param queryValues
     */
    public void insertStation(Station queryValues, String lang) {

        String table;

        if (lang == "gr"){
            table = "stationsG";
        }
        else{
            table = "stationsE";
        }

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", queryValues.getId());
        values.put("title", queryValues.getTitle());
        values.put("description", queryValues.getDescription());
        values.put("lat", queryValues.getLat());
        values.put("lng", queryValues.getLng());
        values.put("walkId", queryValues.getWalkId());
        database.insert(table, null, values);
        Log.i("station_id", queryValues.getId());
        Log.i("station_walkId", queryValues.getWalkId());
        database.close();
    }

    /**
     * Inserts Photo into SQLite DB
     * @param queryValues
     */
    public void insertPhoto(Photo queryValues) {

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("id", queryValues.getId());
        values.put("stationId", queryValues.getStationId());
        values.put("walkId", queryValues.getWalkId());
        values.put("image", queryValues.getImage());

        database.insert("photos", null, values);

        database.close();
    }

    /**
     * Get list of Walks from SQLite DB as Array List
     * with this status (0 for missedWalks, 1 for walkOfDay, 2 for ComingSoon)
     *
     * @return <ArrayList<Walk>
     */
    public ArrayList<Walk> getAllWalks(int status) {
        ArrayList<Walk> walksList;
        walksList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + walks + " WHERE status = " + status + " ORDER BY id";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            for (String s : cursor.getColumnNames()) {
                Log.i("name ", s);
            }
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

    /**
     * Get Walk of Walks with this name
     * @return Walk
     */
    public Walk getWalkByName(String name){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(walks, // a. table
                        COLUMNSWalks, // b. column names
                        " name = ?", // c. selections
                        new String[]{String.valueOf(name)}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor.moveToFirst()) {
            // 4. build walk object
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

            cursor.close();
            return walk;
        }

        cursor.close();
        // 5. return walk
        return null;
    }

    /**
     * Get stations of the walk with this walkId
     *
     * @return ArrayList<Station>
     */
    public ArrayList<Station> getStationsByWalkId(String walkId){

        ArrayList<Station> stationsList = new ArrayList<Station>();
        String selectQuery = "SELECT  * FROM " + stations + " WHERE walkId = " + walkId + " ORDER BY id";
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.rawQuery(selectQuery, null);

        // if we got results get them
        if (cursor.moveToFirst()) {
            do {
                // build stations object
                Station station = new Station();
                station.setId(cursor.getString(cursor.getColumnIndex("id")));
                station.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                station.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                station.setLat(cursor.getDouble(cursor.getColumnIndex("lat")));
                station.setLng(cursor.getDouble(cursor.getColumnIndex("lng")));
                station.setWalkId(cursor.getString(cursor.getColumnIndex("walkId")));
                stationsList.add(station);
            } while (cursor.moveToNext());

            cursor.close();
            return  stationsList;
        }
        cursor.close();
        return  null;
    }

    /**
     * Get photos of the walk with this walkId
     *
     * @return ArrayList<Photo>
     */
    public ArrayList<Photo> getPhotosByWalkId(String walkId){

        ArrayList<Photo> photosList = new ArrayList<Photo>();
        String selectQuery = "SELECT  * FROM photos WHERE walkId = " + walkId + " ORDER BY stationId";
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.rawQuery(selectQuery, null);

        // if we got results get them
        if (cursor.moveToFirst()) {
            do {
                // build photos object
                Photo photo = new Photo();
                photo.setId(cursor.getString(cursor.getColumnIndex("id")));
                photo.setStationId(cursor.getString(cursor.getColumnIndex("stationId")));
                photo.setImage(cursor.getString(cursor.getColumnIndex("image")));
                photo.setWalkId(cursor.getString(cursor.getColumnIndex("walkId")));
                photosList.add(photo);
            } while (cursor.moveToNext());

            cursor.close();
            return  photosList;
        }
        cursor.close();
        return  null;
    }







}
