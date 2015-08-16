package kiki__000.walkingstoursapp;

import android.content.Intent;
import android.os.CountDownTimer;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class WalkofDay extends ActionBarActivity {

    private String walkName;
    private ArrayList<String> namesOfWalksDay = new ArrayList<String>();
    DBController controller = new DBController(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set the lang from sharedPreferences
        MyApplication.updateLanguage(getApplicationContext());

        setContentView(R.layout.activity_walkof_day);

        //full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //set the action bar for the right language
        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_walkof_day));

        //Check if there is walk of day where STATUS = 1
        ArrayList<Walk> walkList = controller.getAllWalks(1);

        if (walkList.size() == 0) { //if there isn't walk of day show a message
            TextView stayTuned = (TextView) findViewById(R.id.noWalk);
            stayTuned.setText(getResources().getString(R.string.stay_tuned));

            //animation
            Animation fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
            stayTuned.startAnimation(fadeIn);

        } else if (walkList.size() == 1) { //show the walk of day

            walkName = walkList.get(0).getName();

            Intent intent = new Intent(WalkofDay.this, ThisWalk.class);
            intent.putExtra("walkName", walkName);
            startActivity(intent);

        } else if (walkList.size() > 1) {  // show the listView in order to select the walk

            //get the names of walks of day
            for (int i = 0; i < walkList.size(); i++) {
                namesOfWalksDay.add(walkList.get(i).getName());
            }

            //convert the arrayList to array of strings
            String[] names = new String[namesOfWalksDay.size()];
            names = namesOfWalksDay.toArray(names);

            //set the listView
            ListViewAdapter adapter = new ListViewAdapter(WalkofDay.this, names);
            ListView dList = (ListView) findViewById(R.id.listView);
            dList.setAdapter(adapter);
            dList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    walkName = namesOfWalksDay.get(position);

                    Intent intent = new Intent(WalkofDay.this, ThisWalk.class);
                    intent.putExtra("walkName", walkName);
                    startActivity(intent);
                }
            });

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_walkof_day, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
