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
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Locale;
import java.util.Objects;

public class MainActivity extends ActionBarActivity {

    private String[] drawerOptions;
    private DrawerLayout dLayout;
    private ListView dList;
    private TextView menu1;
    private TextView menu2;
    private TextView menu3;
    private GraphicsView graphics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setContentView(new GraphicsView(this, "lalal"));

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


        //button for first menu option - missed walks
        menu1 = (TextView)findViewById(R.id.menu1);
        menu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MissedWalks.class);
                startActivity(intent);
            }
        });

        //button for second menu option - walk of day
        menu2 = (TextView)findViewById(R.id.menu2);
        menu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WalkofDay.class);
                startActivity(intent);
            }
        });

        //button for third menu option - coming soon
        menu3 = (TextView)findViewById(R.id.menu3);
        menu3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ComingSoon.class);
                startActivity(intent);
            }
        });

        drawerOptions = new String[]{getResources().getString(R.string.left_scroll_item1), getResources().getString(R.string.left_scroll_item2), getResources().getString(R.string.left_scroll_item3), getResources().getString(R.string.left_scroll_item4)};
        //dLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

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
                    Intent intent = new Intent(MainActivity.this, Register.class);
                    startActivity(intent);
                }
                if (position == 2) {
                    Intent intent = new Intent(MainActivity.this, ThessalonikiWalkingTours.class);
                    startActivity(intent);
                }
                if (position == 3) {
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
