package kiki__000.walkingstoursapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;


/**
 * Created by kiki__000 on 05-May-15.
 */
public class SlidePageSupportFragment extends Fragment {

    //Share by SlidePageSupportFragment 0~3,
    //and set pageNumber by calling setPageNumber()
    //...NOT always valid

    int pageNumber = 0;  //default
    private String walkName;
    private String twitterText;
    DBController controller;
    Walk walk = new Walk();
    ArrayList<Station> stations = new ArrayList<Station>();
    ArrayList<Photo> photos = new ArrayList<Photo>();

    public void setPageNumber(int num){
        pageNumber = num;
    }

    public int getPageNumber(){return pageNumber;}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        //set the lang from sharedPreferences
        MyApplication.updateLanguage(getActivity().getApplicationContext());

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.slide_page, container, false);

        walkName = Map.walkName;

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
                TextView title = (TextView) rootView.findViewById(R.id.station_title);
                title.setText(stations.get(getPageNumber()).getTitle());

                //station image
                ImageView image = (ImageView) rootView.findViewById(R.id.station_image);
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
                TextView description = (TextView) rootView.findViewById(R.id.station_description);
                description.setText(stations.get(getPageNumber()).getDescription());

                //twitterText
                twitterText = "Thessaloniki%23Walking%23Tours%23" + stations.get(getPageNumber()).getTitle() + "%23";
            }
            else{
                Log.i("stations", "null");
                twitterText = "Thessaloniki%23Walking%23Tours%23";
            }

            //twitter button
            Button twitter = (Button) rootView.findViewById(R.id.twitter);
            twitter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String tweetUrl = "https://twitter.com/intent/tweet?text=" + twitterText;
                    Uri uri = Uri.parse(tweetUrl);
                    startActivity(new Intent(Intent.ACTION_VIEW, uri));
                }
            });
        }

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
