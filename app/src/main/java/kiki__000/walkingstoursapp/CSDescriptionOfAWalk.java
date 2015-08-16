package kiki__000.walkingstoursapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class CSDescriptionOfAWalk extends ActionBarActivity {

    private String[] walkData = new String[7];
    private DBController controller = new DBController(this);
    private Button joinIn;

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
        String walkName = intent.getStringExtra("walkName");

        //take the walk with name walkName
        Walk walk = new Walk();
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
        Animation slideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
        LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
        layout.startAnimation(slideDown);

        //joinIn button
        joinIn = (Button)findViewById(R.id.joinIn);

        final String id = walk.getId();
        String join = walk.getJoinIn();

        if (join.equals("0")){
            joinIn.setText(getResources().getString(R.string.question_join));
            joinIn.setEnabled(true);
        }else{
            joinIn.setText(getResources().getString(R.string.join_in));
            joinIn.setEnabled(false);
        }

        joinIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                controller.joinInWalk(id);
                joinIn.setText(getResources().getString(R.string.join_in));
                joinIn.setEnabled(false);
            }
        });

        //title
        TextView title = (TextView) findViewById(R.id.walk_title);
        title.setText(walkData[0]);

        //when
        TextView when = (TextView) findViewById(R.id.walk_when);
        when.setText(walkData[1]);

        //time
        TextView time = (TextView) findViewById(R.id.walk_time);
        time.setText(walkData[2]);

        //venue
        TextView venue = (TextView) findViewById(R.id.walk_venue);
        venue.setText(walkData[3]);

        //kind
        TextView kind = (TextView) findViewById(R.id.walk_kind);
        kind.setText(walkData[4]);

        //guides
        TextView guides = (TextView) findViewById(R.id.walk_guides);
        guides.setText(walkData[5]);

        //description
        TextView description = (TextView) findViewById(R.id.walk_description);
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
