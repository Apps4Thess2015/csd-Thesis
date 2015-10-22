package kiki__000.walkingstoursapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class RefreshDatabaseActivity extends ActionBarActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set the lang from sharedPreferences
        MyApplication.updateLanguage(getApplicationContext());

        setContentView(R.layout.activity_refresh_database);

        context = this;

        //full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //set the action bar for the right language
        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_refresh_database));

        //refresh button
        Button refreshButton = (Button) findViewById(R.id.refresh_button);
        //animation
        Animation animationSlideUp = AnimationUtils.loadAnimation(this, R.anim.panel_slide_up);
        //start animation
        refreshButton.startAnimation(animationSlideUp);
        //set click listener
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //first check the internet connection
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

                //if there isn't internet connection show a message
                if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {

                    final AlertDialog.Builder dialog = new AlertDialog.Builder(context);

                    dialog.setTitle(getResources().getString(R.string.message));
                    dialog.setMessage(getResources().getString(R.string.no_internet));
                    dialog.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialog.setCancelable(true);
                        }
                    });
                    AlertDialog alert = dialog.create();
                    alert.show();
                } else {

                    //check if user is registered
                    SharedPreferences prefs = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
                    String mail = prefs.getString("eMailId", "");
                    if (TextUtils.isEmpty(mail)) { //if not show a message
                        //go to register screen in order to register
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.go_to_register), Toast.LENGTH_SHORT).show();
                    } else { //start

                        //check if update needs
                        AsyncHttpClient client = new AsyncHttpClient();
                        RequestParams params = new RequestParams();
                        params.put("email", mail);
                        Log.i("EMAIL check", mail);
                        // Checks if new records are inserted in Remote MySQL DB to proceed with Sync operation
                        client.post(ApplicationConstants.CHECH_FOR_UPDATE, params, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                                try {
                                    //convert the response to string
                                    String responseString = new String(response, "UTF-8");
                                    System.out.println(responseString);
                                    // Create JSON object out of the response sent by php fle
                                    JSONObject obj = new JSONObject(responseString);
                                    System.out.println(obj.get("count"));
                                    // If the count value is not zero, update the local database
                                    if (obj.getInt("count") != 0) {
                                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.start_update), Toast.LENGTH_SHORT).show();
                                        // Transfer data from remote MySQL DB to SQLite on Android and perform Sync
                                        UpdateSqlLite updateSQL = new UpdateSqlLite(RefreshDatabaseActivity.this, RefreshDatabaseActivity.this);
                                        updateSQL.update();
                                    } else {
                                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.up_date), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException | UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                                if (statusCode == 404) {
                                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                                } else if (statusCode == 500) {
                                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.gone_internet),
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });


                    }
                }


            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_refresh_database, menu);
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
