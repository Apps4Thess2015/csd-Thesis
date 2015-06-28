package kiki__000.walkingstoursapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ComingSoon extends ActionBarActivity {

    private ListView dList;
    private TextView stayTuned;
    private Animation fadeIn;
    private ArrayList<String> namesOfWalksCS = new ArrayList<String>();
    private DBController controller = new DBController(this);
    private String mail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set the lang from sharedPreferences
        MyApplication.updateLanguage(getApplicationContext());

        setContentView(R.layout.activity_coming_soon);

        //full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //set the action bar for the right language
        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_coming_soon));

        //animation
        overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);

        // Get walks from SQLite DB where status = 2
        ArrayList<Walk> walkList = controller.getAllWalks(2);

        if (walkList.size() != 0) { // If walks exists in SQLite DB

            //get the names of walks with status 2
            for (int i = 0; i < walkList.size(); i++) {
                namesOfWalksCS.add(walkList.get(i).getName());
            }

            //convert the arrayList to array of strings
            String[] names = new String[namesOfWalksCS.size()];
            names = namesOfWalksCS.toArray(names);

            //set the listView
            ListViewAdapter adapter = new ListViewAdapter(ComingSoon.this, names);
            dList = (ListView) findViewById(R.id.listView);
            dList.setAdapter(adapter);
            dList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    String walkName = namesOfWalksCS.get(position);

                    Intent intent = new Intent(ComingSoon.this, CSDescriptionOfAWalk.class);
                    intent.putExtra("walkName", walkName);
                    startActivity(intent);
                }
            });
        } else {
            //stay tuned
            stayTuned = (TextView) findViewById(R.id.stayTuned);
            stayTuned.setText(getResources().getString(R.string.stay_tuned));

            //animation
            fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
            stayTuned.startAnimation(fadeIn);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_coming_soon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        // Handle action bar item clicks here.
        int id = item.getItemId();
        // When Sync action button is clicked
        if (id == R.id.refresh) {

            //check if user is registered
            SharedPreferences prefs = getSharedPreferences("UserDetails",
                    Context.MODE_PRIVATE);
            mail = prefs.getString("eMailId", "");
            if (TextUtils.isEmpty(mail)) {
                //go to register screen in order to register
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.go_to_register), Toast.LENGTH_SHORT).show();
            } else {
                //check if update needs
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("email", mail);
                Log.i("EMAIL check", mail);
                // Checks if new records are inserted in Remote MySQL DB to proceed with Sync operation
                client.post(ApplicationConstants.CHECH_FOR_UPDATE, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(String response) {
                        System.out.println(response);
                        try {
                            // Create JSON object out of the response sent by getdbrowcount.php
                            JSONObject obj = new JSONObject(response);
                            System.out.println(obj.get("count"));
                            // If the count value is not zero, update the local database
                            if (obj.getInt("count") != 0) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.start_update), Toast.LENGTH_SHORT).show();
                                // Transfer data from remote MySQL DB to SQLite on Android and perform Sync
                                UpdateSqlLite updateSQL = new UpdateSqlLite(ComingSoon.this);
                                updateSQL.update();
                                reloadActivity();
                            } else {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.up_date), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Throwable error,
                                          String content) {
                        // TODO Auto-generated method stub
                        if (statusCode == 404) {
                            Toast.makeText(getApplicationContext(), "404", Toast.LENGTH_SHORT).show();
                        } else if (statusCode == 500) {
                            Toast.makeText(getApplicationContext(), "500", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error occured!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    // Reload MainActivity
    public void reloadActivity() {
        Intent objIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(objIntent);
    }

}
