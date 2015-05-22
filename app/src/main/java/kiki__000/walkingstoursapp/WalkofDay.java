package kiki__000.walkingstoursapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class WalkofDay extends ActionBarActivity {

    private GridView grid;
    private String[] menuOptions;
    private int[] imageId = {R.mipmap.map, R.mipmap.ic_launcher};
    private TextView stayTuned;
    private String walkName;
    // DB Class to perform DB related operations
    DBController controller = new DBController(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkof_day);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Check if there is walk of day where STATUS = 1
        ArrayList<Walk> walkList = controller.getAllWalks(1);

        if (walkList.size() == 0){ //if there isn't walk of day show a message
            stayTuned = (TextView)findViewById(R.id.noWalk);
            stayTuned.setText(getResources().getString(R.string.stay_tuned));
        }
        else{ //else show the walk of day
            walkName = walkList.get(0).getName();

            menuOptions = new String[]{getResources().getString(R.string.map), getResources().getString(R.string.stations)};

            CustomGrid adapter = new CustomGrid(WalkofDay.this, menuOptions, imageId);
            grid = (GridView) findViewById(R.id.grid);
            grid.setAdapter(adapter);

            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                    if (position == 0) {
                        Intent intent = new Intent(WalkofDay.this, Map.class);
                        intent.putExtra("walkName",walkName);
                        startActivity(intent);
                    } else if (position == 1) {
                        Intent intent = new Intent(WalkofDay.this, ShowMeAWalk.class);
                        intent.putExtra("walkName",walkName);
                        startActivity(intent);
                    }
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
