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


public class MainActivity extends ActionBarActivity {

    private GridView grid;
    //private String[] menuOptions = {getString(R.string.menu1), getString(R.string.menu2), getString(R.string.menu3)};
    String[] menuOptions = {
            "Google",
            "Github",
            "Instagram"};
    private int[] imageId = {R.drawable.abc_ic_go_search_api_mtrl_alpha, R.drawable.abc_ic_go_search_api_mtrl_alpha, R.drawable.abc_ic_go_search_api_mtrl_alpha};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        CustomGrid adapter = new CustomGrid(MainActivity.this, menuOptions, imageId);
        grid = (GridView)findViewById(R.id.grid);
        grid.setAdapter(adapter);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                if (position == 0){
                    Intent intent = new Intent(MainActivity.this, MIssedWalks.class);
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
