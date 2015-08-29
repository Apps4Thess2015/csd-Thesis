package kiki__000.walkingstoursapp;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by kiki__000 on 05-May-15.
 */
public class SlidePageSupportFragment extends Fragment {

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
            Log.i("walkId", walk.getId());
            if (stations != null) {
                Log.i("STATIONS", "" + stations.size());

                //station title
                TextView title = (TextView) rootView.findViewById(R.id.station_title);
                title.setText(stations.get(getPageNumber()).getTitle());

                //points of station
                final TextView points = (TextView)rootView.findViewById(R.id.points);
                final int p = controller.getPointsByStationId(stations.get(getPageNumber()).getId());
                points.setText(String.valueOf(p) + " " + getResources().getString(R.string.stars));

                //star button
                final Button star = (Button)rootView.findViewById(R.id.star_button);
                Boolean rated = controller.getRatedByStationId(stations.get(getPageNumber()).getId());
                if (rated){
                    star.setBackgroundResource(R.mipmap.pressed_star);
                    star.setEnabled(false);
                }else{
                    star.setBackgroundResource(R.mipmap.unpressed_star);
                    star.setEnabled(true);
                }

                star.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        star.setBackgroundResource(R.mipmap.pressed_star);
                        star.setEnabled(false);
                        controller.setRatedByStationId(stations.get(getPageNumber()).getId());
                        controller.setPointsByStationId(stations.get(getPageNumber()).getId(), p);
                        points.setText(String.valueOf(controller.getPointsByStationId(stations.get(getPageNumber()).getId())) + " " + getResources().getString(R.string.stars));
                    }
                });

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

                //facebook button
                Button facebook = (Button) rootView.findViewById(R.id.facebook);
                facebook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                       /** boolean found = false;
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/html");
                        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml("<p>This is the text that will be shared.</p>"));

                        PackageManager pm = v.getContext().getPackageManager();
                        List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);
                        for (final ResolveInfo app : activityList) {
                            if ((app.activityInfo.name).contains("com.facebook.katana")) {
                                final ActivityInfo activity = app.activityInfo;
                                final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
                                shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                                shareIntent.setComponent(name);
                                v.getContext().startActivity(shareIntent);
                                found = true;
                                break;
                            }
                        }
                        if (found == false) {
                            Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.no_app), Toast.LENGTH_SHORT).show();
                        }*/

                    }
                });

            }
            else{
                Log.i("stations", "null");
                twitterText = "Thessaloniki%23Walking%23Tours%23";
            }
        }

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


}
