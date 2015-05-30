package kiki__000.walkingstoursapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class ComingSoon extends ActionBarActivity {

    private int[] imagesId = {R.mipmap.walk, R.mipmap.date, R.mipmap.time,R.mipmap.venue, R.mipmap.kind, R.mipmap.guide, R.mipmap.description};
    private String[] walkData = new String[7];
    private ListView dList;
    private TextView stayTuned;
    private DBController controller = new DBController(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coming_soon);

        //handles the language in orientation changes
        MyApplication.updateLanguage(getApplicationContext(), Language.language);

        //full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //set the action bar for the right language
        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_coming_soon));

        //animation
        overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);

        // Get walks from SQLite DB where status = 2
        ArrayList<Walk> walkList = controller.getAllWalks(2);

        if (walkList.size() != 0) { // If walks exists in SQLite DB
            for (int i=0; i<walkList.size();i++){
                //walk
                walkData[0] = walkList.get(i).getName();
                walkData[1] = walkList.get(i).getDate();
                walkData[2] = walkList.get(i).getTime();
                walkData[3] = walkList.get(i).getVenue();
                walkData[4] = walkList.get(i).getKind();
                walkData[5] = walkList.get(i).getGuide();
                walkData[6] = walkList.get(i).getDescription();
            }

            //set the listView
            ListViewAdapterCS adapter = new ListViewAdapterCS(ComingSoon.this, walkData, imagesId);
            dList = (ListView) findViewById(R.id.listView);
            dList.setAdapter(adapter);


        }
        else{
            //stay tuned
            stayTuned = (TextView)findViewById(R.id.stayTuned);
            stayTuned.setText(getResources().getString(R.string.stay_tuned));
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
            // Transfer data from remote MySQL DB to SQLite on Android and perform Sync
            UpdateSqlLite updateSQL = new UpdateSqlLite(this);
            updateSQL.update();
            reloadActivity();

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
