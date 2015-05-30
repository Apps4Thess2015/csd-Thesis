package kiki__000.walkingstoursapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.List;
import java.util.Locale;


public class Language extends ActionBarActivity {

    private String[] languages;
    private ListView listViewLang;
    public static String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        //handles the language in orientation changes
        MyApplication.updateLanguage(getApplicationContext(), Language.language);

        //full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //set the action bar for the right language
        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_language));

        //set the languages;
        languages = new String[]{getResources().getString(R.string.gr), getResources().getString(R.string.en)};

        //set the ListView
        listViewLang = (ListView)findViewById(R.id.listView_lang);
        listViewLang.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, languages));
        listViewLang.setItemsCanFocus(true);
        listViewLang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               //greek language
                Log.i("lang", "" + position);
                if(position == 0){
                    Locale locale = new Locale("el");
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getApplicationContext().getResources().updateConfiguration(config, null);
                    language = "el";
                }
                //english language
                else{
                    Locale locale = new Locale("en_US");
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getApplicationContext().getResources().updateConfiguration(config, null);
                    language = "en_US";

                }

                //reload activity
                Intent intent = new Intent(Language.this, MainActivity.class);
                Language.this.startActivity(intent);
                finish();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_language, menu);
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
}
