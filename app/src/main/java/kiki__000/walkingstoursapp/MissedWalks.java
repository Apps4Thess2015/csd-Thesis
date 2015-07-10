package kiki__000.walkingstoursapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MissedWalks extends ActionBarActivity {

    private GridView grid;
    private ArrayList<String> walksNames = new ArrayList<String>();
    private ArrayList<Bitmap> drawableIds = new ArrayList<Bitmap>();
    private String walkName;
    DBController controller = new DBController(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set the lang from sharedPreferences
        MyApplication.updateLanguage(getApplicationContext());

        setContentView(R.layout.activity_missed_walks);

        //full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //set the action bar for the right language
        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_missed_walks));

        // Get walks from SQLite DB where STATUS = 0
        ArrayList<Walk> walkList = controller.getAllWalks(0);
        // If walks exists in SQLite DB
        if (walkList.size() != 0) {
            //get the names and photos
            for (int i = 0; i < walkList.size(); i++) {
                walksNames.add(walkList.get(i).getName());
                ArrayList<Photo> temp = controller.getPhotosByWalkId(controller.getWalkByName(walkList.get(i).getName()).getId());

                if (temp == null) {
                    Bitmap b = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                    drawableIds.add(b);
                }else{
                    byte[] decodedString = Base64.decode(temp.get(0).getImage(), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                    drawableIds.add(decodedByte);
                }


            }

            //set gridView
            grid = (GridView) findViewById(R.id.grid);
            //grid.setAdapter(adapter);
            grid.setAdapter(new MyAdapter(this, walksNames, drawableIds));

            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                    //find the name of station which the user clicked
                    ViewGroup gridChild = (ViewGroup) grid.getChildAt(position);
                    int childSize = gridChild.getChildCount();
                    for (int k = 0; k < childSize; k++) {
                        if (gridChild.getChildAt(k) instanceof TextView) {
                            walkName = ((TextView) gridChild.getChildAt(k)).getText().toString();
                            Log.i("walkName", walkName);
                        }
                    }

                    //open the new activity
                    Intent intent = new Intent(MissedWalks.this, Map.class);
                    intent.putExtra("walkName", walkName);
                    startActivity(intent);
                }
            });
        } else {
            //stay tuned
            TextView stayTuned = (TextView) findViewById(R.id.stayTuned);
            stayTuned.setText(getString(R.string.stay_tuned));

            //animation
            Animation fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
            stayTuned.startAnimation(fadeIn);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_missed_walks, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class MyAdapter extends BaseAdapter {
        private List<Item> items = new ArrayList<Item>();
        private LayoutInflater inflater;
        private ArrayList<String> names = new ArrayList<String>();
        private ArrayList<Bitmap> icons = new ArrayList<Bitmap>();

        public MyAdapter(Context context, ArrayList<String> names, ArrayList<Bitmap> icons) {
            inflater = LayoutInflater.from(context);
            this.names = names;
            this.icons = icons;

            for (int i = 0; i < icons.size(); i++) {
                items.add(new Item(names.get(i), icons.get(i)));
            }
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i) {
            return items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        /**
         * @Override public Bitmap getItemId(int i)
         * {
         * return items.get(i).drawableId;
         * }
         */

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v = view;
            ImageView picture;
            TextView name;

            if (v == null) {
                v = inflater.inflate(R.layout.grid_view_item, viewGroup, false);
                v.setTag(R.id.picture, v.findViewById(R.id.picture));
                v.setTag(R.id.text, v.findViewById(R.id.text));
            }

            picture = (ImageView) v.getTag(R.id.picture);
            name = (TextView) v.getTag(R.id.text);

            Item item = (Item) getItem(i);

            picture.setImageBitmap(item.drawableId);
            name.setText(item.name);

            return v;
        }

        private class Item {
            final String name;
            final Bitmap drawableId;

            Item(String name, Bitmap drawableId) {
                this.name = name;
                this.drawableId = drawableId;
            }
        }
    }


}
