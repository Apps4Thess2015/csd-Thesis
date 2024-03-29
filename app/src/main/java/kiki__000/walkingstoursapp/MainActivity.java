package kiki__000.walkingstoursapp;

import android.content.Intent;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends ActionBarActivity {

    private SlidingPaneLayout slidingPanel;
    private DBController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set the lang from sharedPreferences
        MyApplication.updateLanguage(getApplicationContext());

        setContentView(R.layout.activity_main);

        //full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        controller = new DBController(this);

        //check for walks status
        try {
            checkWalksStatus();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //sent points to server
        sentPointsToServer();
        //get points from server
        getPointsFromServer();

        //button for first menu option - missed walks
        Button menu1 = (Button) findViewById(R.id.menu1);
        // load animation in layout
        Animation leftToRight = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_to_right);
        menu1.startAnimation(leftToRight);
        //set listener
        menu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MissedWalks.class);
                startActivity(intent);
            }
        });

        //button for second menu option - walk of day
        Button menu2 = (Button) findViewById(R.id.menu2);
        // load animation in layout
        menu2.startAnimation(leftToRight);
        //set listener
        menu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WalkofDay.class);
                startActivity(intent);
            }
        });

        //button for third menu option - coming soon
        Button menu3 = (Button) findViewById(R.id.menu3);
        // load animation in layout
        menu3.startAnimation(leftToRight);
        //set listener
        menu3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ComingSoon.class);
                startActivity(intent);
            }
        });

        ImageView ballarina = (ImageView) findViewById(R.id.ballarina);
        // load animation in imageView
        Animation bottomToUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_to_up);
        ballarina.startAnimation(bottomToUp);

        slidingPanel = (SlidingPaneLayout) findViewById(R.id.slidingPanel);
        slidingPanel.setParallaxDistance(200);

        //set the listView
        ListView list = (ListView) findViewById(R.id.list);
        //get the listView strings
        String[] listOptions = new String[]{getResources().getString(R.string.left_scroll_item1), getResources().getString(R.string.left_scroll_item2), getResources().getString(R.string.left_scroll_item3), getResources().getString(R.string.left_scroll_item4), getResources().getString(R.string.left_scroll_item5)};
        ListViewAdapter adapterDrawer = new ListViewAdapter(MainActivity.this, listOptions);
        list.setAdapter(adapterDrawer);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent intent = new Intent(MainActivity.this, Language.class);
                    startActivity(intent);
                } else if (position == 1) {
                    Intent intent = new Intent(MainActivity.this, Account.class);
                    startActivity(intent);
                } else if (position == 2) {
                    Intent intent = new Intent(MainActivity.this, RefreshDatabaseActivity.class);
                    startActivity(intent);
                } else if (position == 3) {
                    Intent intent = new Intent(MainActivity.this, ThessalonikiWalkingTours.class);
                    startActivity(intent);
                } else if (position == 4) {
                    Intent intent = new Intent(MainActivity.this, About.class);
                    startActivity(intent);
                }

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.menu_button) {
            if (slidingPanel.isOpen()) {
                slidingPanel.closePane();
            } else {
                slidingPanel.openPane();
            }
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * check the status of walks
     * for current date in background
     */
    public void checkWalksStatus() throws ParseException {

        ArrayList<Walk> walksCS = new ArrayList<>();
        ArrayList<Walk> walksD = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
        String currentDateString;
        String walkDayString;
        Date walkDate;
        Date currentDate;

        currentDateString = sdf.format(new Date());
        currentDate = sdf.parse(currentDateString);

        //check the walks with status 2
        walksCS = controller.getAllWalks(2);

        for (int i = 0; i < walksCS.size(); i++) {
            walkDayString = walksCS.get(i).getDate();
            walkDate = sdf.parse(walkDayString);
            // if walkDate == currentDay then status = 1
            if (walkDate.compareTo(currentDate) == 0) {
                controller.updateStatus(walksCS.get(i).getId(), 1);
                Log.i("here1", "change");
            }
            // if walkDate < currentDay then status = 0
            if (walkDate.compareTo(currentDate) < 0) {
                controller.updateStatus(walksCS.get(i).getId(), 0);
                Log.i("here1", "change");
            }
        }

        //check the walks with status 1
        walksD = controller.getAllWalks(1);

        for (int i = 0; i < walksD.size(); i++) {
            walkDayString = walksD.get(i).getDate();
            walkDate = sdf.parse(walkDayString);
            // if walkDate < currentDay then status = 0
            if (walkDate.compareTo(currentDate) < 0) {
                controller.updateStatus(walksD.get(i).getId(), 0);
                Log.i("here2", "change");
            }
        }
    }

    /**
     * sent the points of stations to Server
     */
    public void sentPointsToServer() {

        ArrayList<String> stationIds = new ArrayList<String>();
        RequestParams params = new RequestParams();

        stationIds = controller.getAllRatedStations();

        if (stationIds == null) {
            Log.i("setPoints", "noResult");
        } else {
            params.put("stationIds", stationIds);
            // Make RESTFul webservice call using AsyncHttpClient object
            AsyncHttpClient client = new AsyncHttpClient();
            client.post(ApplicationConstants.SENT_POINTS, params,
                    new AsyncHttpResponseHandler() {
                        // When the response returned by REST has Http
                        // response code '200'
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                            Toast.makeText(getApplicationContext(), "Succesful sent points", Toast.LENGTH_LONG).show();
                        }

                        // When error occurred
                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                            if (statusCode == 404) {
                                Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                            } else if (statusCode == 500) {
                                Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.gone_internet),
                                        Toast.LENGTH_LONG).show();
                            }
                        }

                    });
        }
    }

    /**
     * get the points of stations from Server
     * in order to update the local database
     */
    public void getPointsFromServer() {

        // Create AsycHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        // Http Request Params Object
        RequestParams params = new RequestParams();

        // Make Http call to remote php file
        client.post(ApplicationConstants.GET_POINTS, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                try {
                    //convert response to string
                    String responseString = new String(response, "UTF-8");

                    // Extract JSON array from the response
                    JSONArray arr = new JSONArray(responseString);
                    System.out.println(arr.length());
                    // If no of array elements is not zero
                    if (arr.length() != 0) {
                        // Loop through each array element, get JSON object
                        for (int i = 0; i < arr.length(); i++) {
                            // Get JSON object
                            JSONObject obj = (JSONObject) arr.get(i);
                            // Add fields extracted from Object
                            String stationId = obj.get("stationId").toString();
                            int points = Integer.parseInt(obj.get("points").toString());

                            //update the field points for the station with this stationId
                            controller.updatePointsByStationId(stationId, points);
                        }
                    }

                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }

            // When error occurred
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.gone_internet),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}