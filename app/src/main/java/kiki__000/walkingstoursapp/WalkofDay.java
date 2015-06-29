package kiki__000.walkingstoursapp;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


public class WalkofDay extends ActionBarActivity {

    private String walkName;
    private TextView walkTimeMessage;
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

        } else { //else show the walk of day

            String walkTimeString = walkList.get(0).getTime();
            walkName = walkList.get(0).getName();

            //get the current time
            SimpleDateFormat sdf = new SimpleDateFormat("kk:mm", Locale.ENGLISH);
            String currentTimeString = sdf.format(new Date());

            try {
                Date currentTime = sdf.parse(currentTimeString);
                Date walkTime = sdf.parse(walkTimeString);

                Log.i("TIME", currentTimeString);

                Date fiveMinAgo=sdf.parse(sdf.format(walkTime.getTime() - (5*60000)));
                String fiveMinAgoString = sdf.format(fiveMinAgo);

                Log.i("after", fiveMinAgoString);
                long countDownInterval = walkTime.getTime() - currentTime.getTime();
                Log.i("long", String.valueOf(countDownInterval));

                walkTimeMessage = (TextView) findViewById(R.id.walkTimeMessage);

                if (currentTime.compareTo(walkTime) < 0 && currentTime.compareTo(fiveMinAgo) < 0) { //currentTime < walkTime therefore, show a TextView with walkTime

                    walkTimeMessage.setText(getResources().getString(R.string.walk_time_message) + walkTimeString);

                } else if(currentTime.compareTo(walkTime) < 0 && fiveMinAgo.compareTo(currentTime) <= 0){ //5 minutes before the walk show the countdown timer
                    //count down for walk
                    //long countDownInterval = walkTime.getTime() - currentTime.getTime();
                    new CountDownTimer(countDownInterval, 1000) {

                        public void onTick(long millisUntilFinished) {
                            long minutes = (millisUntilFinished/1000) / 60;
                            long seconds = (millisUntilFinished/1000) % 60;
                            walkTimeMessage.setText(getResources().getString(R.string.time_remaining) + " " + minutes + ":" + seconds);
                        }

                        public void onFinish() { //show start message and show the walk
                            //hide textView, show toast message and show the walk
                            walkTimeMessage.setText("");
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.walk_starts), Toast.LENGTH_SHORT).show();
                            showTheWalk(walkName);
                        }
                    }.start();

                } else if (currentTime.compareTo(walkTime) >= 0) { //currentTime >= walkTime therefore, show the walk
                    showTheWalk(walkName);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
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

    /**
     * show the walk with this wN name
     * when it's time
     *
     * @param wN
     */
    public void showTheWalk(final String wN) {

        GridView grid;
        String[] menuOptions;

        menuOptions = new String[]{getResources().getString(R.string.map), getResources().getString(R.string.stations)};

        CustomGrid adapter = new CustomGrid(WalkofDay.this, menuOptions);
        grid = (GridView) findViewById(R.id.grid);
        grid.setAdapter(adapter);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                if (position == 0) {
                    Intent intent = new Intent(WalkofDay.this, Map.class);
                    intent.putExtra("walkName", wN);
                    startActivity(intent);
                } else if (position == 1) {
                    Intent intent = new Intent(WalkofDay.this, ShowMeAWalk.class);
                    intent.putExtra("walkName", wN);
                    startActivity(intent);
                }
            }
        });
    }

}
