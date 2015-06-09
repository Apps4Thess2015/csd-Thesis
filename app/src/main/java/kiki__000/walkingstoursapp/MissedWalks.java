package kiki__000.walkingstoursapp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;


public class MissedWalks extends ActionBarActivity {

    private GridView grid;
    private ArrayList<String> menuOptions = new ArrayList<String>();
    private ArrayList<Integer> imageId = new ArrayList<Integer>();
    private TextView stayTuned;
    private String walkName;
    private Animation fadeIn;
    // DB Class to perform DB related operations
    DBController controller = new DBController(this);
    // Progress Dialog Object
    ProgressDialog prgDialog;
    Walk queryValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missed_walks);

        //handles the language in orientation changes
        MyApplication.updateLanguage(getApplicationContext(), Language.language);

        //full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //set the action bar for the right language
        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_missed_walks));

        // Get walks from SQLite DB where STATUS = 0
        ArrayList<Walk> walkList = controller.getAllWalks(0);
        // If walks exists in SQLite DB
        if (walkList.size() != 0) {
            for (int i=0; i<walkList.size();i++){
                imageId.add(R.mipmap.ic_launcher);
                menuOptions.add(walkList.get(i).getName());
            }
            //convert ArrayLists to arrays
            String[] names = new String[menuOptions.size()];
            names = menuOptions.toArray(names);
            int[] icons = new int[imageId.size()];
            for (int i=0; i<imageId.size(); i++){
                icons[i] = Integer.valueOf(imageId.get(i));
            }
            //set gridView
            CustomGrid adapter = new CustomGrid(MissedWalks.this, names, icons);
            grid = (GridView)findViewById(R.id.grid);
            grid.setAdapter(adapter);


            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                    //find the name of station which the user clicked
                    ViewGroup gridChild = (ViewGroup) grid.getChildAt(position);
                    int childSize = gridChild.getChildCount();
                    for(int k = 0; k < childSize; k++) {
                        if( gridChild.getChildAt(k) instanceof TextView ){
                            walkName = ((TextView) gridChild.getChildAt(k)).getText().toString();
                            Log.i("walkName", walkName);
                        }
                    }

                    //open the new activity
                    Intent intent = new Intent(MissedWalks.this, ShowMeAWalk.class);
                    intent.putExtra("walkName",walkName);
                    startActivity(intent);
                }
            });
        }
        else{
            //stay tuned
            stayTuned = (TextView)findViewById(R.id.stayTuned);
            stayTuned.setText(getString(R.string.stay_tuned));

            //animation
            fadeIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
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
