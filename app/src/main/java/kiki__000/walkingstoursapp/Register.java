package kiki__000.walkingstoursapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;

public class Register extends ActionBarActivity {

    public static final String REG_ID = "regId";
    public static final String EMAIL_ID = "eMailId";
    private EditText emailET;
    private EditText password;
    private EditText passwordConfirm;
    private String email;
    private String password1;
    private String password2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set the lang from sharedPreferences
        MyApplication.updateLanguage(getApplicationContext());

        setContentView(R.layout.activity_register);

        //full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //set the action bar for the right language
        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_register));

        emailET = (EditText) findViewById(R.id.edit_text);

        password = (EditText) findViewById(R.id.password_field);
        passwordConfirm = (EditText) findViewById(R.id.confirm_password_field);

        SharedPreferences prefs = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        String registrationId = prefs.getString(REG_ID, "");
        String emailId = prefs.getString(EMAIL_ID, "");

        if (!TextUtils.isEmpty(registrationId)) {
            Intent i = new Intent(getApplicationContext(), GreetingActivity.class);
            i.putExtra("regId", registrationId);
            i.putExtra("emailId", emailId);
            startActivity(i);
            finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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

    // When Register Me button is clicked
    public void RegisterEmail(View view) {
        email = emailET.getText().toString();
        password1 = password.getText().toString();
        password2 = passwordConfirm.getText().toString();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password1) && !TextUtils.isEmpty(password2) && MyApplication.validate(email) && password1.equals(password2)) {

            //Check if user exists
            checkUser(email);
        } else { // When Email is invalid
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.register_error),
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * check if user already exists
     */
    public void checkUser(final String mail) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("email", mail);
        Log.i("EMAIL check", mail);

        // Checks if user exists
        client.post(ApplicationConstants.CHECK_FOR_USER, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                System.out.println(response);
                try {
                    // Create JSON object out of the response sent by getdbrowcount.php
                    JSONObject obj = new JSONObject(response);
                    System.out.println(obj.get("exist"));
                    // If the exist value is 1 then it means that user already exists
                    if (obj.getInt("exist") == 1) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.user_exists), Toast.LENGTH_SHORT).show();
                    } else {

                        //sent the validation email
                        new SendEmailAsyncTask().execute();

                        //go to the RegisterGCM activity
                        Intent intent = new Intent(getApplicationContext(), RegisterGCM.class);
                        intent.putExtra("emailId", email);
                        intent.putExtra("password", password1);
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                // TODO Auto-generated method stub
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "404", Toast.LENGTH_SHORT).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "500", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Error occured!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    /**
     * sent email with link in order the user validate the e-mail address
     */
    class SendEmailAsyncTask extends AsyncTask<Void, Void, Boolean> {

        public SendEmailAsyncTask() {
            if (BuildConfig.DEBUG)
                Log.v(SendEmailAsyncTask.class.getName(), "SendEmailAsyncTask()");
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            if (BuildConfig.DEBUG) Log.v(SendEmailAsyncTask.class.getName(), "doInBackground()");

            try {
                String link = ApplicationConstants.LINK_TO_VALIDATE + "?email=" + email + "&password=" + password1;
                GmailSender sender = new GmailSender(ApplicationConstants.EMAIL_ADDRESS, ApplicationConstants.EMAIL_PASSWORD);
                sender.sendMail("TWT - Confirm the email address",
                        link,
                        email,
                        email);
                Log.i("EMAIL", "OK");
                return true;
            } catch (AuthenticationFailedException e) {
                Log.e(SendEmailAsyncTask.class.getName(), "Bad account details");
                e.printStackTrace();
                return false;
            } catch (MessagingException e) {
                Log.e(SendEmailAsyncTask.class.getName(), e.getMessage() + "failed");
                e.printStackTrace();
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }


}
