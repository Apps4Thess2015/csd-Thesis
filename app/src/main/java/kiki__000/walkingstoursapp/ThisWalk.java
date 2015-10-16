package kiki__000.walkingstoursapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;


public class ThisWalk extends ActionBarActivity {

    private TextView walkTimeMessage;
    private String walkName;
    private Button joinIn;
    private String join;
    private Walk walk = new Walk();
    DBController controller = new DBController(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set the lang from sharedPreferences
        MyApplication.updateLanguage(getApplicationContext());

        setContentView(R.layout.activity_this_walk);

        //full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //set the action bar for the right language
        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_this_walk));

        //get the walkName
        Intent intent = getIntent();
        walkName = intent.getStringExtra("walkName");

        //take the walk with name walkName
        walk = controller.getWalkByName(walkName);

        //joinIn button
        joinIn = (Button)findViewById(R.id.joinIn);

        final String id = walk.getId();
        join = walk.getJoinIn();

        if (join.equals("0")){
            joinIn.setText(getResources().getString(R.string.question_join));
            joinIn.setEnabled(true);
        }else{
            joinIn.setText(getResources().getString(R.string.join_in));
            joinIn.setEnabled(false);
        }

        joinIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //check if user is registered
                SharedPreferences prefs = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
                String email = prefs.getString("eMailId", "");
                if (TextUtils.isEmpty(email)) {
                    //go to register screen in order to register
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.go_to_register), Toast.LENGTH_SHORT).show();
                }else {
                    //check the internet status
                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

                    //if there isn't internet connection show a message
                    if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {

                        final AlertDialog.Builder dialog = new AlertDialog.Builder(ThisWalk.this);

                        dialog.setTitle(getResources().getString(R.string.message));
                        dialog.setMessage(getResources().getString(R.string.internet_connection_participation));
                        dialog.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialog.setCancelable(true);
                            }
                        });
                        AlertDialog alert = dialog.create();
                        alert.show();
                    } else {
                        controller.joinInWalk(id);
                        joinIn.setText(getResources().getString(R.string.join_in));
                        joinIn.setEnabled(false);
                        sentParticipantToServer(walk.getId());
                        new SendEmailAsyncTask().execute();
                    }
                }
            }
        });


        String walkTimeString = walk.getTime();

        //get the current time
        SimpleDateFormat sdf = new SimpleDateFormat("kk:mm", Locale.ENGLISH);
        String currentTimeString = sdf.format(new Date());

        try {
            Date currentTime = sdf.parse(currentTimeString);
            Date walkTime = sdf.parse(walkTimeString);

            Log.i("TIME", currentTimeString);

            Date fiveMinAgo = sdf.parse(sdf.format(walkTime.getTime() - (5 * 60000)));
            String fiveMinAgoString = sdf.format(fiveMinAgo);

            Log.i("after", fiveMinAgoString);
            long countDownInterval = walkTime.getTime() - currentTime.getTime();
            Log.i("long", String.valueOf(countDownInterval));

            walkTimeMessage = (TextView) findViewById(R.id.walkTimeMessage);

            if (currentTime.compareTo(walkTime) < 0 && currentTime.compareTo(fiveMinAgo) < 0) { //currentTime < walkTime therefore, show a TextView with walkTime

                walkTimeMessage.setText(getResources().getString(R.string.walk_time_message) + " " + walkTimeString);

            } else if (currentTime.compareTo(walkTime) < 0 && fiveMinAgo.compareTo(currentTime) <= 0) { //5 minutes before the walk show the countdown timer

                //hide the joinIn button
                joinIn.setVisibility(View.INVISIBLE);

                //count down for walk
                //long countDownInterval = walkTime.getTime() - currentTime.getTime();
                new CountDownTimer(countDownInterval, 1000) {

                    public void onTick(long millisUntilFinished) {
                        long minutes = (millisUntilFinished / 1000) / 60;
                        long seconds = (millisUntilFinished / 1000) % 60;
                        walkTimeMessage.setText(getResources().getString(R.string.time_remaining) + " " + minutes + ":" + seconds);
                    }

                    public void onFinish() { //show start message and show the walk
                        //hide textView, show toast message and show the walk
                        walkTimeMessage.setText("");
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.walk_starts), Toast.LENGTH_SHORT).show();

                        if (join.equals("1")) {
                            showTheWalk(walkName);
                        }else{
                            walkTimeMessage.setText(getResources().getString(R.string.no_join_in));
                        }
                    }
                }.start();

            } else if (currentTime.compareTo(walkTime) >= 0) { //currentTime >= walkTime therefore, show the walk

                //hide the joinIn button
                joinIn.setVisibility(View.INVISIBLE);

                if (join.equals("1")) {
                    showTheWalk(walkName);
                }else{
                    walkTimeMessage.setText(getResources().getString(R.string.no_join_in));
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_this_walk, menu);
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
     * show the walk with this wN name
     * when it's time
     *
     * @param wN
     */
    public void showTheWalk(final String wN) {

        Intent intent = new Intent(ThisWalk.this, Map.class);
        intent.putExtra("walkName", wN);
        startActivity(intent);
        finish();

    }

    /**
     * send participation for this walk to Server
     *
     * @param walkId
     */
    public void sentParticipantToServer(String walkId){

        // Create AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        // Http Request Params Object
        RequestParams params = new RequestParams();
        //get the email of user
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        String email = prefs.getString("eMailId", "");

        final ProgressDialog prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage(getResources().getString(R.string.please_wait));
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        prgDialog.show();
        params.put("email", email);
        params.put("walkId", walkId);
        System.out.println("Email id = " + email + " walkId = " + walkId);
        client.post(ApplicationConstants.INSERT_PARTICIPANT, params,
                new AsyncHttpResponseHandler() {
                    // When the response returned by REST has Http
                    // response code '200'
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                        // Hide Progress Dialog
                        prgDialog.hide();
                        if (prgDialog != null) {
                            prgDialog.dismiss();
                        }
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.participation_ok), Toast.LENGTH_LONG).show();
                    }

                    // When the response returned by REST has Http
                    // response code other than '200' such as '404',
                    // '500' or '403' etc
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {

                        // Hide Progress Dialog
                        prgDialog.hide();
                        if (prgDialog != null) {
                            prgDialog.dismiss();
                        }
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

    /**
     * sent email with the participation of user for the walk
     * in background
     */
    class SendEmailAsyncTask extends AsyncTask<Void, Void, Boolean> {

        public SendEmailAsyncTask() {
            if (BuildConfig.DEBUG)
                Log.v(SendEmailAsyncTask.class.getName(), "SendEmailAsyncTask()");
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            if (BuildConfig.DEBUG) Log.v(SendEmailAsyncTask.class.getName(), "doInBackground()");

            //get the mail of user
            SharedPreferences prefs = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
            String mailUser = prefs.getString("eMailId", "");

            try {
                GmailSender sender = new GmailSender(ApplicationConstants.EMAIL_ADDRESS, ApplicationConstants.EMAIL_PASSWORD);
                sender.sendMail("TWT - New participation",
                        "The user " + mailUser + " has enshrined participation for walk " + walk.getName() + "." ,
                        "kiki_paniskaki@hotmail.com",
                        "kiki_paniskaki@hotmail.com");
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
