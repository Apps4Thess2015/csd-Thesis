package kiki__000.walkingstoursapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Objects;


/**
 * Created by kiki__000 on 21-May-15.
 */
public class UpdateSqlLite {

    String lang[] = new String[]{"gr", "en"};
    DBController controller;
    ProgressDialog prgDialog;
    Walk queryValues;
    Station qValuesStation;
    Photo qValuesPhoto;
    Rating qValuesRating;
    ArrayList<String> deletedWalks = new ArrayList<>();
    Context context;

    public UpdateSqlLite(Context context) {

        this.context = context;
        controller = new DBController(context);
    }

    public void update() {

        // Initialize Progress Dialog properties
        prgDialog = new ProgressDialog(context);
        prgDialog.setMessage(context.getResources().getString(R.string.transfer));
        prgDialog.setCancelable(false);

        //first check for deleted walks
        syncSQLiteMySQLDB(ApplicationConstants.GET_DELETED_WALKS, lang[0]);
        //update table walksG
        syncSQLiteMySQLDB(ApplicationConstants.GET_WALKS_G, lang[0]);
        //update table walksE
        syncSQLiteMySQLDB(ApplicationConstants.GET_WALKS_E, lang[1]);
        //update table stationsG
        syncSQLiteMySQLDB(ApplicationConstants.GET_STATIONS_G, lang[0]);
        //update table stationsE
        syncSQLiteMySQLDB(ApplicationConstants.GET_STATIONS_E, lang[1]);
        //update table rating
        syncSQLiteMySQLDB(ApplicationConstants.GET_RATING, lang[0]);
        //update table Photos
        syncSQLiteMySQLDB(ApplicationConstants.GET_PHOTOS, lang[0]);
    }

    /**
     * Method to Sync MySQL to SQLite DB
     *
     * @param url
     * @param lang
     */
    public void syncSQLiteMySQLDB(final String url, final String lang) {
        // Create AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        // Http Request Params Object
        RequestParams params = new RequestParams();
        //get the email of user
        SharedPreferences prefs = context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        String email = prefs.getString("eMailId", "");
        params.put("email", email);
        Log.i("EMAIL", email);
        // Show ProgressBar
        prgDialog.show();
        // Make Http call to remote php file
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                try {
                    //convert response to string
                    String responseString = new String(response, "UTF-8");

                    // Update SQLite DB with response sent by php file
                    if (url.contains("Walks")) {
                        updateWalks(responseString, lang);
                        Log.i("responce", responseString);
                    } else if (url.contains("Stations")) {
                        updateStations(responseString, lang);
                        Log.i("responce", responseString);
                    } else if (url.contains("Deleted")) {
                        updateDeleted(responseString);
                        Log.i("responce", responseString);
                    } else if (url.contains("Rating")) {
                        updateRating(responseString);
                        Log.i("responce", responseString);
                    } else {
                        updatePhotos(responseString);
                        Log.i("responce", responseString);
                    }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


            }

            // When error occurred
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // Hide ProgressBar
                prgDialog.hide();
                if (statusCode == 404) {
                    Toast.makeText(context, "Requested resource not found", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(context, "Something went wrong at server end", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.gone_internet),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Update the deleted walks
     *
     * @param response
     */
    public void updateDeleted(String response) {

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
                    // Add fields extracted from Object
                    deletedWalks.add(obj.get("walkId").toString());
                    controller.deleteWalk(deletedWalks.get(i));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * Update the table walks
     *
     * @param response
     * @param lang
     */
    public void updateWalks(String response, String lang) {

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
                    //check first if this walk already exists, if yes then deleted
                    String walkId = obj.get("id").toString();
                    if (controller.recordExists(walkId) && lang.equals("gr")) {
                        Log.i("EXISTS", "yes");
                        controller.deleteWalk(walkId);
                    }
                    // DB QueryValues Object to insert into SQLite
                    queryValues = new Walk();
                    // Add fields extracted from Object
                    queryValues.setId(walkId);
                    queryValues.setName(obj.get("name").toString());
                    queryValues.setDate(obj.get("date").toString());
                    queryValues.setTime(obj.get("time").toString());
                    queryValues.setVenue(obj.get("venue").toString());
                    queryValues.setKind(obj.get("kind").toString());
                    queryValues.setGuide(obj.get("guide").toString());
                    queryValues.setDescription(obj.get("description").toString());
                    queryValues.setStations(Integer.parseInt(obj.get("stations").toString()));
                    queryValues.setStatus(Integer.parseInt(obj.get("status").toString()));
                    queryValues.setJoinIn("0");

                    // Insert Walk into SQLite DB
                    controller.insertWalk(queryValues, lang);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * Update the table stations
     *
     * @param response
     * @param lang
     */
    public void updateStations(String response, String lang) {

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
                    qValuesStation.setTurn(obj.get("turn").toString());

                    // Insert Station into SQLite DB
                    controller.insertStation(qValuesStation, lang);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * Update the table Rating
     *
     * @param response
     */
    public void updateRating(String response) {

        try {
            // Extract JSON array from the response
            JSONArray arr = new JSONArray(response);
            System.out.println(arr.length());
            // If no of array elements is not zero
            if (arr.length() != 0) {
                // Loop through each array element, get JSON object
                for (int i = 0; i < arr.length(); i++) {
                    // Get JSON object
                    JSONObject obj = (JSONObject) arr.get(i);
                    //first check if this record already exists in table rating
                    String stationId = obj.get("stationId").toString();
                    if (!controller.recordRating(stationId)) {
                        // DB QueryValues Object to insert into SQLite
                        qValuesRating = new Rating();
                        // Add fields extracted from Object
                        qValuesRating.setId(obj.get("id").toString());
                        qValuesRating.setStationId(obj.get("stationId").toString());
                        qValuesRating.setWalkId(obj.get("walkId").toString());
                        qValuesRating.setPoints(Integer.parseInt(obj.get("points").toString()));
                        qValuesRating.setRated("0");
                        qValuesRating.setSent("0");
                        Log.i("points", obj.get("points").toString());
                        // Insert Photo into SQLite DB
                        controller.insertRating(qValuesRating);
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    /**
     * Update the table Photos
     *
     * @param response
     */
    public void updatePhotos(String response) {

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
                    qValuesPhoto = new Photo();
                    // Add fields extracted from Object
                    qValuesPhoto.setId(obj.get("id").toString());
                    qValuesPhoto.setStationId(obj.get("stationId").toString());
                    qValuesPhoto.setImage(obj.get("image").toString());
                    Log.i("image", obj.get("image").toString());
                    qValuesPhoto.setWalkId(obj.get("walkId").toString());
                    // Insert Photo into SQLite DB
                    controller.insertPhoto(qValuesPhoto);
                }
            }

            finish();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void finish() {

        // Hide ProgressBar
        prgDialog.hide();

        // Reload MainActivity
        Intent objIntent = new Intent(context.getApplicationContext(), MainActivity.class);
        context.startActivity(objIntent);
    }
}
