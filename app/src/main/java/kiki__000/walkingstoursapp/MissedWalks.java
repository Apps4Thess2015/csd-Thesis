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
import android.widget.Toast;
import java.util.ArrayList;


public class MissedWalks extends ActionBarActivity {


    private ArrayList<String> walksNames = new ArrayList<String>();
    private ArrayList<String> icons = new ArrayList<>();
    DBController controller = new DBController(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set the lang from sharedPreferences
        MyApplication.updateLanguage(getApplicationContext());

        setContentView(R.layout.activity_missed_walks);

        //full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //set the action bar for the right language
        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_missed_walks));

        // Get walks from SQLite DB where STATUS = 0 and joinIn = 1
        ArrayList<Walk> walkList = controller.getJoinedWalks();

        //ArrayList<Walk> walkList = controller.getAllWalks(0);

        //message
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.joined_walks), Toast.LENGTH_LONG).show();

        // If walks exists in SQLite DB
        if (walkList.size() != 0) {
            //get the names and photos
            for (int i = 0; i < walkList.size(); i++) {
                walksNames.add(walkList.get(i).getName());
                ArrayList<Photo> temp = controller.getPhotosByWalkId(controller.getWalkByName(walkList.get(i).getName()).getId());
                icons.add(temp.get(0).getImage());
            }

            //set listView
            ListView list = (ListView) findViewById(R.id.list);
            final MyAdapter adapter = new MyAdapter(this, walksNames, icons);
            list.setAdapter(adapter);

            //set on item click
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    //get the card's name which user has clicked
                    Object nameObject = adapter.getItem(position);
                    String walkName = String.valueOf(nameObject);

                    //open the new Activity
                    Intent intent = new Intent(MissedWalks.this, Map.class);
                    intent.putExtra("walkName", walkName);
                    startActivity(intent);

                }
            });

        } else {
            //stay tuned
            TextView stayTuned = (TextView) findViewById(R.id.stayTuned);
            stayTuned.setText(getString(R.string.no_participation));

            //animation
            Animation fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
            stayTuned.startAnimation(fadeIn);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_missed_walks, menu);
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
