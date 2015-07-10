package kiki__000.walkingstoursapp;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class MainActivity extends ActionBarActivity {

    private String[] drawerOptions;
    private DrawerLayout dLayout;
    private ListView dList;
    private TextView menu1;
    private TextView menu2;
    private TextView menu3;
    private DBController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set the lang from sharedPreferences
        MyApplication.updateLanguage(getApplicationContext());

        setContentView(R.layout.activity_main);

        //full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        controller = new DBController(this);
        try {
            checkWalksStatus();
        } catch (ParseException e) {
            e.printStackTrace();
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

        drawerOptions = new String[]{getResources().getString(R.string.left_scroll_item1), getResources().getString(R.string.left_scroll_item2), getResources().getString(R.string.left_scroll_item3), getResources().getString(R.string.left_scroll_item4), getResources().getString(R.string.left_scroll_item5)};
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
                    Intent intent = new Intent(MainActivity.this, Register.class);
                    startActivity(intent);
                }
                if (position == 2) {
                    Intent intent = new Intent(MainActivity.this, RefreshDatabaseActivity.class);
                    startActivity(intent);
                }
                if (position == 3) {
                    Intent intent = new Intent(MainActivity.this, ThessalonikiWalkingTours.class);
                    startActivity(intent);
                }
                if (position == 4) {
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
        if (id == R.id.menu_button) {
            if (dLayout.isDrawerOpen(Gravity.LEFT)){
                dLayout.closeDrawers();
            }
            else{
                dLayout.openDrawer(Gravity.LEFT);
            }
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * check the status of walks
     * for current date in background
     * */
    public void checkWalksStatus() throws ParseException {

        ArrayList<Walk> walksCS = new ArrayList<>();
        ArrayList<Walk> walksD = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
        String currentDateString;
        String walkDayString;
        Date walkDate;
        Date currentDate;

        currentDateString = sdf.format(new Date());
        currentDate = sdf.parse(currentDateString);

        //check the walks with status 2
        walksCS = controller.getAllWalks(2);

        for (int i=0; i<walksCS.size(); i++){
            walkDayString = walksCS.get(i).getDate();
            walkDate = sdf.parse(walkDayString);
            // if walkDate == currentDay then status = 1
            if (walkDate.compareTo(currentDate) == 0){
                controller.updateStatus(walksCS.get(i).getId(), 1);
                Log.i("here1", "change");
            }
            // if walkDate < currentDay then status = 0
            if (walkDate.compareTo(currentDate) < 0){
                controller.updateStatus(walksCS.get(i).getId(), 0);
                Log.i("here1", "change");
            }
        }

        //check the walks with status 1
        walksD = controller.getAllWalks(1);

        for (int i=0; i<walksD.size(); i++){
            walkDayString = walksD.get(i).getDate();
            walkDate = sdf.parse(walkDayString);
            // if walkDate < currentDay then status = 0
            if (walkDate.compareTo(currentDate) < 0){
                controller.updateStatus(walksD.get(i).getId(), 0);
                Log.i("here2", "change");
            }
        }
    }


}
