package kiki__000.walkingstoursapp;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import java.util.ArrayList;

import static com.google.android.gms.maps.GoogleMap.*;

public class Map extends ActionBarActivity implements OnMarkerClickListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    public static String walkName;
    private Button showRoute;
    DBController controller;
    Walk walk = new Walk();
    ArrayList<String> titles = new ArrayList<>();
    ArrayList<Station> stations = new ArrayList<>();
    ArrayList<LatLng> listPoint = new ArrayList<>();
    int currentPt;
    public static int NUM_PAGES;
    private ViewPager mPager;
    Button up;
    Button down;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set the lang from sharedPreferences
        MyApplication.updateLanguage(getApplicationContext());

        setContentView(R.layout.activity_map);

        //full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //set the action bar for the right language
        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_map));

        //get the walkName
        Intent intent = getIntent();
        walkName = intent.getStringExtra("walkName");

        controller = new DBController(getApplicationContext());
        // Get walk from SQLite DB
        walk = controller.getWalkByName(walkName);
        if (walk == null) {
            Log.i("station_name", "null");
        } else {
            //get the station's number of walk and set the NUM_PAGES
            NUM_PAGES = walk.getStations();

            //view pager for every station of walk
            mPager = (ViewPager) findViewById(R.id.stationsPanel);
            PagerAdapter mPagerAdapter = new MyFragmentStatePagerAdapter(getSupportFragmentManager());
            mPager.setAdapter(mPagerAdapter);
            mPager.setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View v, DragEvent event) {
                    return false;
                }
            });


            //animation for station's panel in order to slide up-down
            Animation animationSlideDownIn;
            animationSlideDownIn = AnimationUtils.loadAnimation(this, R.anim.panel_slide_up);
            mPager.startAnimation(animationSlideDownIn);

            stations = controller.getStationsByWalkId(walk.getId());
            Log.i("walkId", walk.getId());
            if (stations != null) {
                Log.i("stations", "" + stations.size());
                for (int i = 0; i < stations.size(); i++) {

                    //lat & lng
                    listPoint.add(new LatLng(stations.get(i).getLat(), stations.get(i).getLng()));

                    //markers' titles
                    titles.add(stations.get(i).getTitle());
                }
            }
        }

        //button in order to start the animation of route
        showRoute = (Button) findViewById(R.id.show_route);

        up = (Button)findViewById(R.id.up);
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //animation for station's panel in order to slide up-down
                Animation animationSlideUp;
                animationSlideUp = AnimationUtils.loadAnimation(Map.this, R.anim.panel_slide_up);
                mPager.startAnimation(animationSlideUp);
                mPager.setVisibility(View.VISIBLE);

            }
        });

        down = (Button)findViewById(R.id.down);
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //animation for station's panel in order to slide up-down
                Animation animationSlideDown;
                animationSlideDown = AnimationUtils.loadAnimation(Map.this, R.anim.panel_slide_down);
                mPager.startAnimation(animationSlideDown);
                mPager.setVisibility(View.INVISIBLE);


            }
        });

        setUpMapIfNeeded();

    }

   /** @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

        //enable zoom controls buttons
        mMap.getUiSettings().setZoomControlsEnabled(true);

        //enable location button
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        //camera position and center
        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(40.6312779, 22.9526476));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        mMap.moveCamera(center);
        mMap.animateCamera(zoom);

        //onMarkerClick go to station's view
        mMap.setOnMarkerClickListener(this);

        //clickListener for showRoute button
        showRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stations == null) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_route), Toast.LENGTH_LONG).show();
                } else {
                    int zoomValue = 15;
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(zoomValue), 4000, MyCancelableCallback);
                    currentPt = 0;
                }
            }
        });

    }

    /**
     * add markers to map with animation
     **/
    CancelableCallback MyCancelableCallback = new CancelableCallback() {

        @Override
        public void onCancel() {
        }

        @Override
        public void onFinish() {
            if (currentPt < listPoint.size()) {
                Log.i("current", "" + currentPt);
                Log.i("listpointSize", "" + listPoint.size());

                //set number of station inside of marker
                IconGenerator tc = new IconGenerator(Map.this);
                Bitmap bmp = tc.makeIcon("" + (currentPt + 1));

                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(listPoint.get(currentPt))
                        .title(titles.get(currentPt))
                        .icon(BitmapDescriptorFactory.fromBitmap(bmp)));
                marker.showInfoWindow();

                mMap.animateCamera(CameraUpdateFactory.newLatLng(listPoint.get(currentPt)), 4000, MyCancelableCallback);
                currentPt++;

            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.end_route), Toast.LENGTH_LONG).show();
            }

        }

    };

    /**
     * clicking the marker go to
     * the corresponding station-page
     *
     * @param marker
     *
     * @return
     */
    @Override
    public boolean onMarkerClick(Marker marker) {

        int viewNumber = 0;

        for (int i = 0; i < titles.size(); i++) {
            if (marker.getTitle().equals(titles.get(i))) {
                viewNumber = i;
            }
        }

        //change the color of pressed marker
        IconGenerator tc = new IconGenerator(Map.this);
        tc.setTextAppearance(R.style.pressedMarker);
        Bitmap bmp = tc.makeIcon("" + (viewNumber + 1));
        marker.setIcon(BitmapDescriptorFactory.fromBitmap(bmp));

        //set all others to unpressed markers


        //set the viewPager to the right station
        mPager.setCurrentItem(viewNumber, true);

        return false;
    }

    /**
     * class for the fragment of viewpager
     **/
    private class MyFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
        public MyFragmentStatePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment tmpFragment;
            if (position == NUM_PAGES) {
                tmpFragment = new SlideLastPageSupportFragment();
            } else {
                tmpFragment = new SlidePageSupportFragment();
                ((SlidePageSupportFragment) tmpFragment).setPageNumber(position);
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
            header = getResources().getString(R.string.station) + " " + Integer.valueOf(position + 1);

            return header;
        }
    }




}
