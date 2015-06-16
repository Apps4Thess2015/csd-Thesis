package kiki__000.walkingstoursapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class CSDescriptionOfAWalk extends ActionBarActivity {

    //private int[] imagesId = {R.mipmap.walk, R.mipmap.date, R.mipmap.time, R.mipmap.venue, R.mipmap.kind, R.mipmap.guide, R.mipmap.description};
    private int[] imagesId = {R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};
    private String[] walkData = new String[7];
    private ListView dList;
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

        //walk
        walkData[0] = walk.getName();
        walkData[1] = walk.getDate();
        walkData[2] = walk.getTime();
        walkData[3] = walk.getVenue();
        walkData[4] = walk.getKind();
        walkData[5] = walk.getGuide();
        walkData[6] = walk.getDescription();

        //set the listView
        ListViewAdapterCS adapter = new ListViewAdapterCS(CSDescriptionOfAWalk.this, walkData, imagesId);
        dList = (ListView) findViewById(R.id.listView);
        dList.setAdapter(adapter);

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
