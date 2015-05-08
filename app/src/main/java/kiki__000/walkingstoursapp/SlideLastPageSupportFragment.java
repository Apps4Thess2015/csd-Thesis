package kiki__000.walkingstoursapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by kiki__000 on 08-May-15.
 */
public class SlideLastPageSupportFragment extends Fragment {

    int pageNumber = 4;
    private TextView title;
    private ImageView image;
    private TextView description;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.slide_page, container, false);

        title = (TextView)rootView.findViewById(R.id.station_title);
        title.setText("station title" + pageNumber );

        image = (ImageView)rootView.findViewById(R.id.station_image);
        image.setImageResource(R.mipmap.ic_launcher);

        description = (TextView)rootView.findViewById(R.id.station_description);
        description.setText("station description");

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
