package kiki__000.walkingstoursapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by kiki__000 on 27-Apr-15.
 */
public class ListViewAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] options;

    public ListViewAdapter(Activity context, String[] options) {
        super(context, R.layout.row_listview, options);
        this.context = context;
        this.options = options;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.row_listview, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.listName);
        txtTitle.setText(options[position]);

        return rowView;
    }

}
