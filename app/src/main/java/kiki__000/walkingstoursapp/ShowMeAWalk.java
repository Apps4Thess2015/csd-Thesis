package kiki__000.walkingstoursapp;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import java.util.ArrayList;

public class ShowMeAWalk extends ActionBarActivity {

    public static int NUM_PAGES;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    public static String walkName;
    DBController controller;
    Walk walk = new Walk();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set the lang from sharedPreferences
        MyApplication.updateLanguage(getApplicationContext());

        setContentView(R.layout.activity_show_me_awalk);

        //full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //set the action bar for the right language
        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_show_me_awalk));

        //get the walkName
        Intent intent = getIntent();
        walkName = intent.getStringExtra("walkName");

        //get the station's number of walk and set the NUM_PAGES
        controller = new DBController(this);
        walk = controller.getWalkByName(walkName);
        NUM_PAGES = walk.getStations();

        //view pager for every station of walk
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new MyFragmentStatePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_me_awalk, menu);
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

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    private class MyFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
        public MyFragmentStatePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment tmpFragment;
            if(position==NUM_PAGES){
                tmpFragment = new SlideLastPageSupportFragment();
            }else{
                tmpFragment = new SlidePageSupportFragment();
                ((SlidePageSupportFragment)tmpFragment).setPageNumber(position);
            }
            return tmpFragment;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            String header;
            header = getResources().getString(R.string.station) + " " + Integer.valueOf(position+1);

            return header;
        }
    }
}
