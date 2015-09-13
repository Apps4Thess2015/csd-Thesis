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

    //columns' names of table walks
    private static final String[] COLUMNSWalks = {"id", "name", "date", "time", "venue", "kind", "guide", "description", "stations", "status", "joinIn"};
    //columns' names of table stations
    private static final String[] COLUMNSStations = {"id", "title", "description", "lat", "lng", "walkId", "turn"};
    //columns' names of table photos
    private static final String[] COLUMNSPhotos = {"id", "stationId", "walkId", "image"};
    //columns' names of table rating
    private static final String[] COLUMNSRating = {"id", "stationId", "walkId", "points", "rated", "sent"};
    //set the string walks as walksG for greek or walksE for english
    public static String walks;
    //set the string stations as stationsG for greek or stationsE for english
    public static String stations;

    public DBController(Context applicationcontext) {
        super(applicationcontext, "walks.db", null, 47);
    }

    //Create Table
    @Override
    public void onCreate(SQLiteDatabase database) {
        String query;

        //create table walksG (table walks with greek content)
        query = "CREATE TABLE walksG ( id TEXT, name TEXT, date TEXT, time TEXT, venue TEXT, kind TEXT, guide TEXT, description TEXT, stations INTEGER, status INTEGER, joinIn TEXT)";
        database.execSQL(query);

        //create table stationsG (table stations with greek content)
        query = "CREATE TABLE stationsG ( id TEXT, title TEXT, description TEXT, lat DOUBLE, lng DOUBLE, walkId TEXT, turn TEXT)";
        database.execSQL(query);

        //create table walksE (table walks with english content)
        query = "CREATE TABLE walksE ( id TEXT, name TEXT, date TEXT, time TEXT, venue TEXT, kind TEXT, guide TEXT, description TEXT, stations INTEGER, status INTEGER, joinIn TEXT)";
        database.execSQL(query);

        //create table stationsE (table stations with english content)
        query = "CREATE TABLE stationsE ( id TEXT, title TEXT, description TEXT, lat DOUBLE, lng DOUBLE, walkId TEXT, turn TEXT)";
        database.execSQL(query);

        //create table photos
        query = "CREATE TABLE photos ( id TEXT, stationId TEXT, walkId TEXT,  image TEXT)";
        database.execSQL(query);

        //create table rating
        query = "CREATE TABLE rating (id TEXT, stationId TEXT, walkId TEXT, points INTEGER, rated TEXT, sent TEXT)";
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

        query = "DROP TABLE IF EXISTS rating";
        database.execSQL(query);

        onCreate(database);
    }

    /** Delete walk from SQLite with this id
     *
     * @param walkId
     */
    public void deleteWalk(String walkId){

        SQLiteDatabase database = this.getWritableDatabase();
        String[] whereArgs = new String[]{walkId};

        //delete walk from table walksG
        database.delete("walksG", "id=?", whereArgs);

        //delete walk from table walksE
        database.delete("walksE", "id=?", whereArgs);

        //delete all the stations of walk from table stationsG
        database.delete("stationsG", "walkId=?", whereArgs);

        //delete all the stations of walk from table stationsE
        database.delete("stationsE", "walkId=?", whereArgs);

        //delete all the photos of walk from table photos
        database.delete("photos", "walkId=?", whereArgs);

        //delete all the records of walk from table rating
        database.delete("rating", "walkId=?", whereArgs);

        database.close();
    }

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
        values.put("joinIn", queryValues.getJoinIn());
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
        values.put("turn", queryValues.getTurn());
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
     * Inserts Rating into SQLite DB
     * @param queryValues
     */
    public void insertRating(Rating queryValues) {

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("id", queryValues.getId());
        values.put("stationId", queryValues.getStationId());
        values.put("walkId", queryValues.getWalkId());
        values.put("points", queryValues.getPoints());
        values.put("rated", queryValues.getRated());
        values.put("sent", queryValues.getSent());

        database.insert("rating", null, values);

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
                walk.setJoinIn(cursor.getString(cursor.getColumnIndex("joinIn")));
                walksList.add(walk);

            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return  walksList;
    }

    /**
     * get the walks which have been past
     * and the field joinIn = 1 (true)
     *
     * @return joinedWalksList
     */
    public ArrayList<Walk> getJoinedWalks(){
        ArrayList<Walk> joinedWalksList;
        joinedWalksList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + walks + " WHERE status = 0 AND joinIn = 1 ORDER BY id";
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
                walk.setJoinIn(cursor.getString(cursor.getColumnIndex("joinIn")));
                joinedWalksList.add(walk);

            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return  joinedWalksList;
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
            walk.setJoinIn(cursor.getString(cursor.getColumnIndex("joinIn")));

            cursor.close();
            db.close();
            return walk;
        }

        cursor.close();
        db.close();
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
        String selectQuery = "SELECT  * FROM " + stations + " WHERE walkId = " + walkId + " ORDER BY turn";
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
                station.setTurn(cursor.getString(cursor.getColumnIndex("turn")));
                stationsList.add(station);
            } while (cursor.moveToNext());

            cursor.close();
            database.close();
            return  stationsList;
        }
        cursor.close();
        database.close();
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
            database.close();
            return  photosList;
        }
        cursor.close();
        database.close();
        return  null;
    }

    /**
     * update the status of the
     * walk with this walkId and set this status
     *
     * @param walkId
     * @param status
     */
    public void updateStatus(String walkId, int status){

        SQLiteDatabase database = this.getReadableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put("status", status);

        // Which row to update, based on the ID
        String selection = "id" + " LIKE ?";
        String[] selectionArgs = { walkId };

        //update table walksG
        int countG = database.update(
                        "walksG",
                        values,
                        selection,
                        selectionArgs);

        //update table walksE
        int countE = database.update(
                "walksE",
                values,
                selection,
                selectionArgs);

        database.close();
    }

    /**
     * check if record with this id
     * already exists in local database
     *
     * @param id
     * @return
     */
    public boolean recordExists(String id){

        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT * FROM walksG  WHERE id = " + id;
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.getCount() <= 0) {
            Log.i("EXISTS1","no");
            cursor.close();
            database.close();
            return false;
        }
        Log.i("EXISTS1","yes");
        cursor.close();
        database.close();
        return true;
    }

    /**
     * join the walk with this id
     * set the field joinIn = 1 in tables walkG and walkE
     *
     * @param walkId
     */
    public void joinInWalk(String walkId){

        SQLiteDatabase database = this.getReadableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put("joinIn", "1");

        // Which row to update, based on the ID
        String selection = "id" + " LIKE ?";
        String[] selectionArgs = { walkId };

        //update table walksG
        int countG = database.update(
                "walksG",
                values,
                selection,
                selectionArgs);

        //update table walksE
        int countE = database.update(
                "walksE",
                values,
                selection,
                selectionArgs);

        database.close();

    }


    /**
     * check if the user has rated the
     * station with this stationId
     *
     * @param stationId
     * @return true or false
     */
    public boolean getRatedByStationId(String stationId){

        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT rated FROM rating  WHERE stationId = " + stationId;
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.getCount() <= 0) {
            Log.i("RATED","noResult");
            cursor.close();
            database.close();
            return false;
        }else {
            cursor.moveToFirst();
            String rated = cursor.getString(cursor.getColumnIndex("rated"));
            cursor.close();
            database.close();
            if (rated.equals("1")){
                Log.i("RATED","yes");
                return true;
            }else {
                Log.i("RATED","no");
                return false;
            }
        }
    }

    /**
     * get the points of station with this stationId
     *
     * @param  stationId
     * @return points
     */
    public int getPointsByStationId(String stationId) {

        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT points FROM rating  WHERE stationId = " + stationId;
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.getCount() <= 0) {
            Log.i("POINTS","noResult");
            cursor.close();
            database.close();

            return -1;
        }else {
            cursor.moveToFirst();
            int points = Integer.parseInt(cursor.getString(cursor.getColumnIndex("points")));
            cursor.close();
            database.close();

            return points;

        }
    }

    /**
     * set field rated for station with this stationId
     * set the field rated = 1 in table rating
     *
     * @param stationId
     */
    public void setRatedByStationId(String stationId){

        SQLiteDatabase database = this.getReadableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put("rated", "1");

        // Which row to update, based on the ID
        String selection = "stationId" + " LIKE ?";
        String[] selectionArgs = { stationId };

        //update table rating
        int countG = database.update(
                "rating",
                values,
                selection,
                selectionArgs);

        database.close();

    }

    /**
     * increase the points of station with this StationId by one
     *
     * @param stationId
     */
    public void setPointsByStationId(String stationId, int po){

        SQLiteDatabase database = this.getReadableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        String points = String.valueOf(po + 1);
        values.put("points", points);

        // Which row to update, based on the ID
        String selection = "stationId" + " LIKE ?";
        String[] selectionArgs = { stationId };

        //update table rating
        int countG = database.update(
                "rating",
                values,
                selection,
                selectionArgs);

        database.close();
    }


    /**
     * get all the stations where rated == 1 AND sent == 0
     * in order to sent the points to server
     * also set sent == 1
     *
     * @return stationIds
     */
    public ArrayList<String> getAllRatedStations(){

        ArrayList<String> stationIds = new ArrayList<String>();

        //build query
        String selectQuery = "SELECT stationId FROM rating WHERE rated = 1 AND sent = 0";
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.rawQuery(selectQuery, null);

        // if we got results get them
        if (cursor.moveToFirst()) {
            do {
                // add to stationIds arrayList
                String stationId = cursor.getString(cursor.getColumnIndex("stationId"));
                stationIds.add(stationId);
                Log.i("RatedStationID", stationId);

                //set sent = 1 for this station
                // New value for one column
                ContentValues values = new ContentValues();
                values.put("sent", "1");

                // Which row to update, based on the ID
                String selection = "stationId" + " LIKE ?";
                String[] selectionArgs = { stationId };

                //update table rating
                int countG = database.update(
                        "rating",
                        values,
                        selection,
                        selectionArgs);

            } while (cursor.moveToNext());

            cursor.close();
            database.close();
            return  stationIds;
        }
        cursor.close();
        database.close();
        return  null;
    }


    /**
     * update table rating
     * set points to station with this stationId
     *
     * @param stationId
     */
    public void updatePointsByStationId(String stationId, int points){

        SQLiteDatabase database = this.getReadableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put("points", points);

        // Which row to update, based on the ID
        String selection = "stationId" + " LIKE ?";
        String[] selectionArgs = { stationId };

        //update table rating
        int countG = database.update(
                "rating",
                values,
                selection,
                selectionArgs);

        database.close();
    }

    /**
     * check if record with this stationId
     * already exists in local database in table rating
     *
     * @param stationId
     * @return
     */
    public boolean recordRating(String stationId){

        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT * FROM rating  WHERE stationId = " + stationId;
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.getCount() <= 0) {
            Log.i("EXISTSRATING","no");
            cursor.close();
            database.close();
            return false;
        }
        Log.i("EXISTSRATING","yes");
        cursor.close();
        database.close();
        return true;
    }



}
