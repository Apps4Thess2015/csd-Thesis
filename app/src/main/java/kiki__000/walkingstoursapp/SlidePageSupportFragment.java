package kiki__000.walkingstoursapp;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by kiki__000 on 05-May-15.
 */
public class SlidePageSupportFragment extends Fragment {

    //Share by SlidePageSupportFragment 0~3,
    //and set pageNumber by calling setPageNumber()
    //...NOT always valid

    int pageNumber = 0;  //default
    private TextView title;
    private ImageView image;
    private TextView description;
    private String walkName;
    private Button plusOne;
    private Button lessOne;
    private SupportMapFragment fragment;
    private GoogleMap miniMap;
    DBController controller;
    Walk walk = new Walk();
    ArrayList<Station> stations = new ArrayList<Station>();
    ArrayList<Photo> photos = new ArrayList<Photo>();
    private Marker marker;
    private LatLng latLng;

    public void setPageNumber(int num){
        pageNumber = num;
    }

    public int getPageNumber(){return pageNumber;}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.slide_page, container, false);

        walkName = ShowMeAWalk.walkName;

        controller = new DBController(getActivity().getApplicationContext());

        // Get walk from SQLite DB
        walk = controller.getWalkByName(walkName);
        if (walk == null){
            Log.i("station_name", "null");
        }
        else{
            stations = controller.getStationsByWalkId(walk.getId());
            Log.i("walkId",walk.getId());
            if (stations != null) {
                Log.i("STATIONS", "" + stations.size());
                //station title
                title = (TextView) rootView.findViewById(R.id.station_title);
                title.setText(stations.get(getPageNumber()).getTitle());

                //station image
                image = (ImageView) rootView.findViewById(R.id.station_image);
                photos = controller.getPhotosByWalkId(walk.getId());

                if (photos == null) {
                    image.setImageResource(R.mipmap.ic_launcher);
                }else{
                    //convert bytes into array
                    byte[] decodedString = Base64.decode(photos.get(getPageNumber()).getImage(), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                    image.setImageBitmap(decodedByte);
                }

                //station description
                description = (TextView) rootView.findViewById(R.id.station_description);
                description.setText(stations.get(getPageNumber()).getDescription());

                //lat & lng
                latLng = new LatLng(stations.get(getPageNumber()).getLat(), stations.get(getPageNumber()).getLng());
            }

            //plus one & less one buttons for rating
            plusOne = (Button)rootView.findViewById(R.id.plus_one);
            plusOne.setClickable(true);
            lessOne = (Button)rootView.findViewById(R.id.less_one);
            lessOne.setClickable(true);

            plusOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    plusOne.setTextColor(getResources().getColor(R.color.gold));
                    plusOne.setClickable(false);
                    lessOne.setClickable(false);
                }
            });

            lessOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lessOne.setTextColor(getResources().getColor(R.color.gold));
                    lessOne.setClickable(false);
                    plusOne.setClickable(false);
                }
            });

        }

        return rootView;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentManager fm = getChildFragmentManager();
        fragment = (SupportMapFragment) fm.findFragmentById(R.id.miniMap);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.miniMap, fragment).commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (miniMap == null) {
            miniMap = fragment.getMap();
            if (miniMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {

        //enable zoom controls buttons
        miniMap.getUiSettings().setZoomControlsEnabled(true);

        //enable location button
        miniMap.setMyLocationEnabled(true);
        miniMap.getUiSettings().setMyLocationButtonEnabled(true);

        //camera position and center
        CameraUpdate center= CameraUpdateFactory.newLatLng(new LatLng(40.6312779, 22.9526476));
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);
        miniMap.moveCamera(center);
        miniMap.animateCamera(zoom);

        //add marker
        if (latLng != null) {
            marker = miniMap.addMarker(new MarkerOptions().position(latLng).title(stations.get(getPageNumber()).getTitle()));
            marker.showInfoWindow();
            Timer timer = new Timer();
            TimerTask updateProfile = new CustomTimerTask(getActivity().getApplicationContext());
            timer.scheduleAtFixedRate(updateProfile, 10, 5000);
            miniMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18.0f));
        }

    }

    /**
     * CustomTimerTask class for animate-bounce marker in map
     * */
    private class CustomTimerTask extends TimerTask {
        private Context context;
        private Handler mHandler = new Handler();

        public CustomTimerTask(Context con) {
            this.context = con;
        }

        @Override
        public void run() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            final Handler handler = new Handler();
                            final long start = SystemClock.uptimeMillis();
                            final long duration = 1500;
                            final Interpolator interpolator = new BounceInterpolator();

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    long elapsed = SystemClock.uptimeMillis() - start;
                                    float t = Math.max(1 - interpolator.getInterpolation((float)elapsed/duration), 0);
                                    marker.setAnchor(0.5f, 1.0f + 2 * t);

                                    if (t > 0.0) {
                                        // Post again 12ms later.
                                        handler.postDelayed(this, 12);
                                    }
                                }
                            });
                        }
                    });
                }
            }).start();
        }
    }





}
