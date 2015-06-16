package kiki__000.walkingstoursapp;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.renderscript.Allocation;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class ThessalonikiWalkingTours extends ActionBarActivity {

    private String[] team;
    private String[] info;
    private int[] imageId;
    private TableLayout table;
    private ArrayList<TableRow> hiddenRows = new ArrayList<>();
    private ArrayList<Button> buttons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set the lang from sharedPreferences
        MyApplication.updateLanguage(getApplicationContext());

        setContentView(R.layout.activity_thessaloniki_walking_tours);

        //full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //set the action bar for the right language
        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_thessaloniki_walking_tours));

        //create array with team's photos
        imageId = new int[]{R.mipmap.evi, R.mipmap.kostis, R.mipmap.giorgos, R.mipmap.tasos, R.mipmap.alkistis, R.mipmap.mada};
        //create array with team's names
        team = new String[]{getResources().getString(R.string.member1), getResources().getString(R.string.member2), getResources().getString(R.string.member3), getResources().getString(R.string.member4), getResources().getString(R.string.member5), getResources().getString(R.string.member6)};
        //create array with team's info
        info = new String[]{getResources().getString(R.string.info_member1), getResources().getString(R.string.info_member2), getResources().getString(R.string.info_member3), getResources().getString(R.string.info_member4), getResources().getString(R.string.info_member5), getResources().getString(R.string.info_member6)};

        table = (TableLayout) findViewById(R.id.table);

        int rows = 2*team.length;
        for (int i = 0; i<rows; i=i+2) {

            //add a row with image, name and a button
            TableRow row= new TableRow(this);

            TableLayout.LayoutParams layoutRow = new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT);
            layoutRow.setMargins(5, 5, 5, 5);
            row.setLayoutParams(layoutRow);

            int index = i/2;
            //member image
            ImageView image = new ImageView(this);
            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(80, 80);
            image.setLayoutParams(layoutParams);
            image.setBackgroundResource(imageId[index]);

            //member name
            TextView name = new TextView(this);
            name.setText(team[index]);
            name.setTextSize(getResources().getDimension(R.dimen.text_size));

            //button in order to show the info of member
            Button more = new Button(this);
            TableRow.LayoutParams buttonParams = new TableRow.LayoutParams(20, 20);
            more.setLayoutParams(buttonParams);
            more.setBackgroundResource(R.mipmap.down_arrow);
            more.setTag("more");
            more.setId(i);
            more.setOnClickListener(infoButtonClickListener(more));
            buttons.add(more);

            //center the elements
            row.setGravity(Gravity.CENTER);

            row.addView(image);
            row.addView(name);
            row.addView(more);

            table.addView(row, i);

            //add a row with description for above member
            TableRow row1= new TableRow(this);

            TableRow.LayoutParams layoutRow1 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT);
            row1.setLayoutParams(layoutRow1);

            TableRow.LayoutParams params1 = (TableRow.LayoutParams) row1.getLayoutParams();
            params1.span = 3; //amount of columns for spanning

            TextView details = new TextView(this);
            details.setText(info[index]);

            details.setLayoutParams(params1);
            details.setGravity(Gravity.CENTER);

            row1.setGravity(Gravity.CENTER);
            row1.addView(details);
            row1.setVisibility(View.GONE);
            row1.setId(i);

            hiddenRows.add(row1);

            table.addView(row1, i + 1);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_thessaloniki_walking_tours, menu);
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


    // When info button is clicked show a window with member's info
    View.OnClickListener infoButtonClickListener(final Button button)  {
        return new View.OnClickListener() {
            public void onClick(View v) {

                int position = button.getId();

                if (button.getTag() == "more") {
                    for (int i = 0; i < hiddenRows.size(); i++) {
                        if (hiddenRows.get(i).getId() == position) {
                            hiddenRows.get(i).setVisibility(View.VISIBLE);
                            TableRow.LayoutParams buttonParams = new TableRow.LayoutParams(20, 20);
                            buttons.get(i).setLayoutParams(buttonParams);
                            buttons.get(i).setBackgroundResource(R.mipmap.up_arrow);
                            buttons.get(i).setTag("less");
                        } else {
                            hiddenRows.get(i).setVisibility(View.GONE);
                            TableRow.LayoutParams buttonParams = new TableRow.LayoutParams(20, 20);
                            buttons.get(i).setLayoutParams(buttonParams);
                            buttons.get(i).setBackgroundResource(R.mipmap.down_arrow);
                            buttons.get(i).setTag("more");
                        }
                    }
                }else{
                    for (int i = 0; i < hiddenRows.size(); i++) {
                        if (hiddenRows.get(i).getId() == position) {
                            hiddenRows.get(i).setVisibility(View.GONE);
                            TableRow.LayoutParams buttonParams = new TableRow.LayoutParams(20, 20);
                            buttons.get(i).setLayoutParams(buttonParams);
                            buttons.get(i).setBackgroundResource(R.mipmap.down_arrow);
                            buttons.get(i).setTag("more");
                        }
                    }
                }
            }
        };
    }

}
