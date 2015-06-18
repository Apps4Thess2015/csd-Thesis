package kiki__000.walkingstoursapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class CSDescriptionOfAWalk extends ActionBarActivity {

    private TextView title;
    private TextView when;
    private TextView time;
    private TextView venue;
    private TextView kind;
    private TextView guides;
    private TextView description;
    private LinearLayout layout;
    private Animation slideDown;
    private String[] walkData = new String[7];
    private Walk walk = new Walk();
    private String walkName;
    private DBController controller = new DBController(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set the lang from sharedPreferences
        MyApplication.updateLanguage(getApplicationContext());

        setContentView(R.layout.activity_csdescription_of_awalk);

        //full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //set the action bar for the right language
        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_csdescription_of_awalk));

        //get the walkName
        Intent intent = getIntent();
        walkName = intent.getStringExtra("walkName");

        //take the walk with name walkName
        walk = controller.getWalkByName(walkName);

        //create the walk
        walkData[0] = walk.getName();
        walkData[1] = walk.getDate();
        walkData[2] = walk.getTime();
        walkData[3] = walk.getVenue();
        walkData[4] = walk.getKind();
        walkData[5] = walk.getGuide();
        walkData[6] = walk.getDescription();

        //create graphics and animation

        // load animations in layout
        slideDown = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_down);
        layout = (LinearLayout)findViewById(R.id.layout);
        layout.startAnimation(slideDown);

        //title
        title = (TextView)findViewById(R.id.walk_title);
        title.setText(walkData[0]);

        //when
        when = (TextView)findViewById(R.id.walk_when);
        when.setText(walkData[1]);

        //time
        time = (TextView)findViewById(R.id.walk_time);
        time.setText(walkData[2]);

        //venue
        venue = (TextView)findViewById(R.id.walk_venue);
        venue.setText(walkData[3]);

        //kind
        kind = (TextView)findViewById(R.id.walk_kind);
        kind.setText(walkData[4]);

        //guides
        guides = (TextView)findViewById(R.id.walk_guides);
        guides.setText(walkData[5]);

        //description
        description = (TextView)findViewById(R.id.walk_description);
        description.setText(walkData[6]);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_csdescription_of_awalk, menu);
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
