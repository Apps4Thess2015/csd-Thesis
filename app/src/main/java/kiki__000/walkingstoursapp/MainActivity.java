package kiki__000.walkingstoursapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;

public class MainActivity extends ActionBarActivity {

    private GridView grid;
    private String[] menuOptions;
    private int[] imageId = {R.mipmap.missed, R.mipmap.today, R.mipmap.coming};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        menuOptions = new String[]{getResources().getString(R.string.menu1), getResources().getString(R.string.menu2), getResources().getString(R.string.menu3)};

        CustomGrid adapter = new CustomGrid(MainActivity.this, menuOptions, imageId);
        grid = (GridView)findViewById(R.id.grid);
        grid.setAdapter(adapter);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                Log.i("menu1", getResources().getString(R.string.menu1));
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
