package kiki__000.walkingstoursapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by kiki__000 on 08-May-15.
 */
public class SlideLastPageSupportFragment extends Fragment {

    private TextView title;
    private ImageView image;
    private TextView description;
    private String walkName;
    private SupportMapFragment fragment;
    private Button plusOne;
    private Button lessOne;
    private GoogleMap miniMap;
    DBController controller;
    Walk walk = new Walk();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.slide_page, container, false);

        walkName = ShowMeAWalk.walkName;

        controller = new DBController(getActivity().getApplicationContext());

        // Get walk from SQLite DB
        walk = controller.getWalkByName(walkName);
        if (walk == null) {
            Log.i("station_name", "null");
        } else {
            //station title
            title = (TextView) rootView.findViewById(R.id.station_title);
            title.setText(walk.getName());

            //station image
            image = (ImageView) rootView.findViewById(R.id.station_image);
            image.setImageResource(R.mipmap.ic_launcher);

            //station description
            description = (TextView) rootView.findViewById(R.id.station_description);
            description.setText(walk.getDescription());

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
            miniMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)));
        }
    }

    private void setUpMap() {
        miniMap.addMarker(new MarkerOptions().position(new LatLng(40.6312779, 22.9526476)).title("Marker"));

        //enable zoom controls buttons
        miniMap.getUiSettings().setZoomControlsEnabled(true);

        //enable location button
        miniMap.setMyLocationEnabled(true);
        miniMap.getUiSettings().setMyLocationButtonEnabled(true);

        //camera position and center
        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(40.6312779, 22.9526476));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        miniMap.moveCamera(center);
        miniMap.animateCamera(zoom);
    }

}
