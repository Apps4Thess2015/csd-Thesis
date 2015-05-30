package kiki__000.walkingstoursapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;

import static com.google.android.gms.maps.GoogleMap.*;

public class Map extends ActionBarActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    public static String walkName;
    private Button showRoute;
    private Marker marker;
    DBController controller;
    Walk walk;
    ArrayList<String> titles = new ArrayList<>();
    ArrayList<Station> stations = new ArrayList<>();
    ArrayList<LatLng> listPoint = new ArrayList<>();
    int currentPt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //handles the language in orientation changes
        MyApplication.updateLanguage(getApplicationContext(), Language.language);

        //full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //set the action bar for the right language
        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_map));

        setContentView(R.layout.activity_map);

        //get the walkName
        Intent intent = getIntent();
        walkName = intent.getStringExtra("walkName");


        controller = new DBController(getApplicationContext());
        // Get walk from SQLite DB
        walk = controller.getWalkByName(walkName);
        if (walk == null){
            Log.i("station_name", "null");
        }
        else {
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
        showRoute = (Button)findViewById(R.id.show_route);

        setUpMapIfNeeded();
    }

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

       showRoute.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(stations == null){
                   Toast.makeText(getApplicationContext(),getResources().getString(R.string.no_route),Toast.LENGTH_LONG).show();
               }else{
                   int zoomValue = 15;
                   mMap.animateCamera(CameraUpdateFactory.zoomTo(zoomValue),4000,MyCancelableCallback);
                   currentPt = 0;
               }
           }
       });

    }


    CancelableCallback MyCancelableCallback = new CancelableCallback(){

        @Override
        public void onCancel() {}

        @Override
        public void onFinish() {
            if(currentPt < listPoint.size()){
                Log.i("current", "" + currentPt);
                Log.i("listpointSize", "" + listPoint.size());
                marker = mMap.addMarker(new MarkerOptions().position(listPoint.get(currentPt)).title(titles.get(currentPt)));
                marker.showInfoWindow();
                mMap.animateCamera(CameraUpdateFactory.newLatLng(listPoint.get(currentPt)),4000, MyCancelableCallback);
                currentPt++;

            }else{
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.end_route),Toast.LENGTH_LONG).show();
            }

        }

    };

}
