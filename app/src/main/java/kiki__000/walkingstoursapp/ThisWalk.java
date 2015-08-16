package kiki__000.walkingstoursapp;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class ThisWalk extends ActionBarActivity {

    private TextView walkTimeMessage;
    private String walkName;
    DBController controller = new DBController(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set the lang from sharedPreferences
        MyApplication.updateLanguage(getApplicationContext());

        setContentView(R.layout.activity_this_walk);

        //full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //set the action bar for the right language
        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_this_walk));

        //get the walkName
        Intent intent = getIntent();
        walkName = intent.getStringExtra("walkName");

        //take the walk with name walkName
        Walk walk = controller.getWalkByName(walkName);

        String walkTimeString = walk.getTime();

        //get the current time
        SimpleDateFormat sdf = new SimpleDateFormat("kk:mm", Locale.ENGLISH);
        String currentTimeString = sdf.format(new Date());

        try {
            Date currentTime = sdf.parse(currentTimeString);
            Date walkTime = sdf.parse(walkTimeString);

            Log.i("TIME", currentTimeString);

            Date fiveMinAgo = sdf.parse(sdf.format(walkTime.getTime() - (5 * 60000)));
            String fiveMinAgoString = sdf.format(fiveMinAgo);

            Log.i("after", fiveMinAgoString);
            long countDownInterval = walkTime.getTime() - currentTime.getTime();
            Log.i("long", String.valueOf(countDownInterval));

            walkTimeMessage = (TextView) findViewById(R.id.walkTimeMessage);

            if (currentTime.compareTo(walkTime) < 0 && currentTime.compareTo(fiveMinAgo) < 0) { //currentTime < walkTime therefore, show a TextView with walkTime

                walkTimeMessage.setText(getResources().getString(R.string.walk_time_message) + walkTimeString);

            } else if (currentTime.compareTo(walkTime) < 0 && fiveMinAgo.compareTo(currentTime) <= 0) { //5 minutes before the walk show the countdown timer
                //count down for walk
                //long countDownInterval = walkTime.getTime() - currentTime.getTime();
                new CountDownTimer(countDownInterval, 1000) {

                    public void onTick(long millisUntilFinished) {
                        long minutes = (millisUntilFinished / 1000) / 60;
                        long seconds = (millisUntilFinished / 1000) % 60;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_this_walk, menu);
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

        Intent intent = new Intent(ThisWalk.this, Map.class);
        intent.putExtra("walkName", wN);
        startActivity(intent);

    }
}
