package kiki__000.walkingstoursapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by kiki__000 on 23-Mar-15.
 */
public class CustomGrid extends BaseAdapter {

    private Context mContext;
    private final String[] menuOptions;
    private final int[] Imageid;

    public CustomGrid(Context c, String[] menuOptions, int[] Imageid ) {
        mContext = c;
        this.Imageid = Imageid;
        this.menuOptions = menuOptions;
    }

    @Override
    public int getCount() {
        return menuOptions.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_single, null);
            TextView textView = (TextView) grid.findViewById(R.id.grid_text);
            ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);
            textView.setText(menuOptions[position]);
            imageView.setImageResource(Imageid[position]);
        } else {
            grid = (View) convertView;
        }
        return grid;
    }

}
