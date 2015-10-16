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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ComingSoon extends ActionBarActivity {

    private ArrayList<String> namesOfWalksCS = new ArrayList<String>();
    private DBController controller = new DBController(this);

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

        if (walkList != null && walkList.size() != 0) { // If walks exists in SQLite DB

            //get the names of walks with status 2
            for (int i = 0; i < walkList.size(); i++) {
                namesOfWalksCS.add(walkList.get(i).getName());
            }

            //convert the arrayList to array of strings
            String[] names = new String[namesOfWalksCS.size()];
            names = namesOfWalksCS.toArray(names);

            //set the listView
            ListViewAdapter adapter = new ListViewAdapter(ComingSoon.this, names);
            ListView dList = (ListView) findViewById(R.id.listView);
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
            TextView stayTuned = (TextView) findViewById(R.id.stayTuned);
            stayTuned.setText(getResources().getString(R.string.stay_tuned));

            //animation
            Animation fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
