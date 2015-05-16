package kiki__000.walkingstoursapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by kiki__000 on 14-May-15.
 */
public class ListViewAdapterCS extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] options;
    private final int[] images;

    public ListViewAdapterCS(Activity context, String[] options, int[] images) {
        super(context, R.layout.coming_soon_item, options);
        this.context = context;
        this.options = options;
        this.images = images;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.coming_soon_item, null, true);
        rowView.setFocusable(false);
        //set image
        ImageView imageView = (ImageView) rowView.findViewById(R.id.comingSoonImage);
        imageView.setBackgroundResource(images[position]);
        //set text
        TextView txtTitle = (TextView) rowView.findViewById(R.id.comingSoonText);
        txtTitle.setText(options[position]);

        return rowView;
    }
}
