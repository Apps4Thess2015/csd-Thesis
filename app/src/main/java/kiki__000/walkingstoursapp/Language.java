package kiki__000.walkingstoursapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ListView;
import java.util.Locale;


public class Language extends ActionBarActivity {

    private String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set the lang from sharedPreferences
        MyApplication.updateLanguage(getApplicationContext());

        setContentView(R.layout.activity_language);

        //full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //set the action bar for the right language
        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_language));

        //set the languages;
        String[] languages = new String[]{getResources().getString(R.string.gr), getResources().getString(R.string.en)};

        //set the ListView
        ListView listViewLang = (ListView) findViewById(R.id.listView_lang);
        listViewLang.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, languages));
        listViewLang.setItemsCanFocus(true);
        listViewLang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //greek language
                Log.i("lang", "" + position);
                if (position == 0) {
                    Locale locale = new Locale("el");
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getApplicationContext().getResources().updateConfiguration(config, null);
                    language = "el";
                }
                //english language
                else {
                    Locale locale = new Locale("en_US");
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getApplicationContext().getResources().updateConfiguration(config, null);
                    language = "en_US";
                }
                Log.i("LANGUAGE", language);
                //store language in sharedPreferences
                SharedPreferences prefs = getSharedPreferences("language", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("lang", language);
                editor.apply();

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
