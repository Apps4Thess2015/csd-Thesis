package kiki__000.walkingstoursapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


public class LogIn extends ActionBarActivity {

    private EditText emailET;
    private EditText passwordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set the lang from sharedPreferences
        MyApplication.updateLanguage(getApplicationContext());

        setContentView(R.layout.activity_log_in);

        //full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //set the action bar for the right language
        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_log_in));

        //check the internet status
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        //if there isn't internet connection show a message
        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()){

            final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

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
        }

        //email editText
        emailET = (EditText)findViewById(R.id.edit_text);

        //password editText
        passwordET = (EditText)findViewById(R.id.password_field);

        //logInButton
        Button logInButton = (Button) findViewById(R.id.log_in_button);
        //set onClick Listener for logInButton
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailET.getText().toString();
                String password = passwordET.getText().toString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.empty_login), Toast.LENGTH_SHORT).show();
                } else {
                    //check if this user is already register
                    checkUserLogIn(email, password);
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_log_in, menu);
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

    /**
     * check if user is already register in remote database
     */
    public void checkUserLogIn(final String mail, final String password) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("email", mail);
        params.put("password", password);
        Log.i("LOGIN check", mail);

        //lock screen orientation
        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        // Checks if user exists
        client.post(ApplicationConstants.CHECK_LOG_IN, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                try {
                    //convert response to string
                    String responseString = new String(response, "UTF-8");
                    System.out.println(responseString);

                    // Create JSON object out of the response sent by php file
                    JSONObject obj = new JSONObject(responseString);
                    System.out.println(obj.get("exist"));

                    //unlock screen orientation
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

                    // If the exist value is 0 then it means failed log in
                    if (obj.getInt("exist") == 0) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.login_fail), Toast.LENGTH_SHORT).show();
                    } else { //success logIn

                        //store emailId in SharedPref
                        SharedPreferences prefs = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("eMailId", mail);
                        editor.putString("regId", "exists");
                        editor.commit();

                        //go to the Greeting activity
                        Intent intent = new Intent(getApplicationContext(), GreetingActivity.class);
                        intent.putExtra("eMailId", mail);
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            //when error occurred
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {

                //unlock screen orientation
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

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
