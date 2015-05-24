package kiki__000.walkingstoursapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by kiki__000 on 21-May-15.
 */
public class UpdateSqlLite {

    String urls[] = new String[]{"http://paniskak.webpages.auth.gr/walkingTours/getWalksG.php", "http://paniskak.webpages.auth.gr/walkingTours/getWalksE.php", "http://paniskak.webpages.auth.gr/walkingTours/getStationsG.php", "http://paniskak.webpages.auth.gr/walkingTours/getStationsE.php"};
    String lang[] = new String[]{"gr", "en"};
    DBController controller;
    ProgressDialog prgDialog;
    Walk queryValues;
    Station qValuesStation;
    Context context;

    public UpdateSqlLite(Context context) {

        this.context = context;
        controller = new DBController(context);
    }

    public void update() {

        // Initialize Progress Dialog properties
        prgDialog = new ProgressDialog(context);
        prgDialog.setMessage("Transferring Data from Remote MySQL DB and Syncing SQLite. Please wait...");
        prgDialog.setCancelable(false);
        // BroadCase Receiver Intent Object
        Intent alarmIntent = new Intent(context, SampleBC.class);
        // Pending Intent Object
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Alarm Manager Object
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        // Alarm Manager calls BroadCast for every Ten seconds (10 * 1000), BroadCase further calls service to check if new records are inserted in
        // Remote MySQL DB
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() + 5000, 10 * 1000, pendingIntent);

        //increase dbVersion
       // controller.increaseDbVersion();

        //update walksG
        syncSQLiteMySQLDB(urls[0], lang[0]);
        //update walksE
        syncSQLiteMySQLDB(urls[1], lang[1]);
        //update stationsG
        syncSQLiteMySQLDB(urls[2], lang[0]);
        //update stationsE
        syncSQLiteMySQLDB(urls[3], lang[1]);
    }

    // Method to Sync MySQL to SQLite DB
    public void syncSQLiteMySQLDB(final String url, final String lang) {
        // Create AsycHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        // Http Request Params Object
        RequestParams params = new RequestParams();
        // Show ProgressBar
        prgDialog.show();
        // Make Http call to remote php file
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                // Hide ProgressBar
                prgDialog.hide();
                // Update SQLite DB with response sent by php file
                if (url.contains("Walks")) {
                    updateWalks(response, lang);
                } else {
                    updateStations(response, lang);
                }
            }

            // When error occured
            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                // TODO Auto-generated method stub
                // Hide ProgressBar
                prgDialog.hide();
                if (statusCode == 404) {
                    Toast.makeText(context, "Requested resource not found", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(context, "Something went wrong at server end", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    /**
     * Update the walk table
     */

    public void updateWalks(String response, String lang) {

        // Create GSON object
        Gson gson = new GsonBuilder().create();
        try {
            // Extract JSON array from the response
            JSONArray arr = new JSONArray(response);
            System.out.println(arr.length());
            // If no of array elements is not zero
            if (arr.length() != 0) {
                // Loop through each array element, get JSON object which has userid and username
                for (int i = 0; i < arr.length(); i++) {
                    // Get JSON object
                    JSONObject obj = (JSONObject) arr.get(i);
                    // DB QueryValues Object to insert into SQLite
                    queryValues = new Walk();
                    // Add fields extracted from Object
                    queryValues.setId(obj.get("id").toString());
                    queryValues.setName(obj.get("name").toString());
                    queryValues.setDate(obj.get("date").toString());
                    queryValues.setTime(obj.get("time").toString());
                    queryValues.setVenue(obj.get("venue").toString());
                    queryValues.setKind(obj.get("kind").toString());
                    queryValues.setGuide(obj.get("guide").toString());
                    queryValues.setDescription(obj.get("description").toString());
                    queryValues.setStations(Integer.parseInt(obj.get("stations").toString()));
                    queryValues.setStatus(Integer.parseInt(obj.get("status").toString()));

                    // Insert Walk into SQLite DB
                    controller.insertWalk(queryValues, lang);
                }
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * update the table stations
     */

    public void updateStations(String response, String lang) {

        // Create GSON object
        Gson gson = new GsonBuilder().create();
        try {
            // Extract JSON array from the response
            JSONArray arr = new JSONArray(response);
            System.out.println(arr.length());
            // If no of array elements is not zero
            if (arr.length() != 0) {

                // Loop through each array element, get JSON object which has userid and username
                for (int i = 0; i < arr.length(); i++) {
                    // Get JSON object
                    JSONObject obj = (JSONObject) arr.get(i);
                    // DB QueryValues Object to insert into SQLite
                    qValuesStation = new Station();
                    // Add fields extracted from Object
                    qValuesStation.setId(obj.get("id").toString());
                    qValuesStation.setTitle(obj.get("title").toString());
                    qValuesStation.setDescription(obj.get("description").toString());
                    qValuesStation.setLat(Double.parseDouble(obj.get("lat").toString()));
                    qValuesStation.setLng(Double.valueOf(obj.get("lng").toString()));
                    qValuesStation.setWalkId(obj.get("walkId").toString());

                    // Insert Station into SQLite DB
                    controller.insertStation(qValuesStation, lang);
                }
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}