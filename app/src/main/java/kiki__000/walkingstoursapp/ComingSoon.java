package kiki__000.walkingstoursapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.HashMap;


public class ComingSoon extends ActionBarActivity {

    private TextView walk;
    private TextView when;
    private TextView time;
    private TextView venue;
    private TextView kind;
    private TextView guides;
    private TextView descr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coming_soon);

        //full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //walk
        walk = (TextView)findViewById(R.id.walk);
        walk.setText(R.string.walk);

        //when
        when = (TextView)findViewById(R.id.when);
        when.setText(R.string.when);

        //time
        time = (TextView)findViewById(R.id.time);
        time.setText(R.string.time);

        //venue
        venue = (TextView)findViewById(R.id.venue);
        venue.setText(R.string.venue);

        //kind
        kind = (TextView)findViewById(R.id.kind);
        kind.setText(R.string.kind);

        //guides
        guides = (TextView)findViewById(R.id.guides);
        guides.setText(R.string.guides);

        //description
        descr = (TextView)findViewById(R.id.description);
        descr.setText(R.string.description);






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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
