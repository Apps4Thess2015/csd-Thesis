package kiki__000.walkingstoursapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import java.util.Locale;
import java.util.Objects;

public class MainActivity extends ActionBarActivity {

    private GridView grid;
    private String[] menuOptions;
    private int[] imageId = {R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};
    private String[] drawerOptions;
    private DrawerLayout dLayout;
    private ListView dList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //handles the language in orientation changes
        MyApplication.updateLanguage(getApplicationContext(), Language.language);

        //full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

       // Locale current = getResources().getConfiguration().locale;
        //String current = Locale.getDefault().getDisplayLanguage();
        //Log.i("locale", current);


        //set the language of tables in local database
        String lang = Language.language;

        if(lang == null){
            String current = Locale.getDefault().getDisplayLanguage();
            Log.i("locale", current);
            if (current.equals("????????")){
                DBController.walks = "walksG";
                DBController.stations = "stationsG";
            }
            else{
                DBController.walks = "walksE";
                DBController.stations = "stationsE";
            }
        }
        else {
            if (lang.equals("en_US")) {
                DBController.walks = "walksE";
                DBController.stations = "stationsE";
            } else if (lang.equals("el")) {
                DBController.walks = "walksG";
                DBController.stations = "stationsG";
            }
        }



        menuOptions = new String[]{getResources().getString(R.string.menu1), getResources().getString(R.string.menu2), getResources().getString(R.string.menu3)};

        CustomGrid adapter = new CustomGrid(MainActivity.this, menuOptions, imageId);
        grid = (GridView)findViewById(R.id.grid);
        grid.setAdapter(adapter);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                if (position == 0){
                    Intent intent = new Intent(MainActivity.this, MissedWalks.class);
                    startActivity(intent);
                }
                else if (position == 1){
                    Intent intent = new Intent(MainActivity.this, WalkofDay.class);
                    startActivity(intent);
                }
                else if (position == 2){
                    Intent intent = new Intent(MainActivity.this, ComingSoon.class);
                    startActivity(intent);
                }
            }
        });

        drawerOptions = new String[]{getResources().getString(R.string.left_scroll_item1), getResources().getString(R.string.left_scroll_item2), getResources().getString(R.string.left_scroll_item3)};
        dLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ListViewAdapter adapterDrawer = new ListViewAdapter(MainActivity.this, drawerOptions);
        dList = (ListView) findViewById(R.id.left_drawer);
        dList.setAdapter(adapterDrawer);
        dList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                //dLayout.closeDrawers();
                if (position == 0) {
                    Intent intent = new Intent(MainActivity.this, Language.class);
                    startActivity(intent);
                }
                if (position == 1) {
                    Intent intent = new Intent(MainActivity.this, ThessalonikiWalkingTours.class);
                    startActivity(intent);
                }
                if (position == 2) {
                    Intent intent = new Intent(MainActivity.this, About.class);
                    startActivity(intent);
                }
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
