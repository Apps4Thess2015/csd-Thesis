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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;


public class CSDescriptionOfAWalk extends ActionBarActivity {

    private String[] walkData = new String[7];
    private DBController controller = new DBController(this);
    private Button joinIn;
    private Walk walk = new Walk();
    private AlertDialog.Builder dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set the lang from sharedPreferences
        MyApplication.updateLanguage(getApplicationContext());

        setContentView(R.layout.activity_csdescription_of_awalk);

        //full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //set the action bar for the right language
        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_csdescription_of_awalk));

        //get the walkName
        Intent intent = getIntent();
        String walkName = intent.getStringExtra("walkName");

        //take the walk with name walkName
        walk = controller.getWalkByName(walkName);

        //create the walk
        walkData[0] = walk.getName();
        walkData[1] = walk.getDate();
        walkData[2] = walk.getTime();
        walkData[3] = walk.getVenue();
        walkData[4] = walk.getKind();
        walkData[5] = walk.getGuide();
        walkData[6] = walk.getDescription();

        //create graphics and animation

        // load animations in layout
        Animation slideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
        LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
        layout.startAnimation(slideDown);

        //title
        TextView title = (TextView) findViewById(R.id.walk_title);
        title.setText(walkData[0]);

        //when
        TextView when = (TextView) findViewById(R.id.walk_when);
        when.setText(walkData[1]);

        //time
        TextView time = (TextView) findViewById(R.id.walk_time);
        time.setText(walkData[2]);

        //venue
        TextView venue = (TextView) findViewById(R.id.walk_venue);
        venue.setText(walkData[3]);

        //kind
        TextView kind = (TextView) findViewById(R.id.walk_kind);
        kind.setText(walkData[4]);

        //guides
        TextView guides = (TextView) findViewById(R.id.walk_guides);
        guides.setText(walkData[5]);

        //description
        TextView description = (TextView) findViewById(R.id.walk_description);
        description.setText(walkData[6]);

        //joinIn button
        joinIn = (Button)findViewById(R.id.joinIn);

        final String id = walk.getId();
        String join = walk.getJoinIn();

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

                        final AlertDialog.Builder dialog = new AlertDialog.Builder(CSDescriptionOfAWalk.this);

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

        //dialog window with joinIn message
        if(join.equals("0")) {
            dialog = new AlertDialog.Builder(this);

            dialog.setTitle(getResources().getString(R.string.message));
            dialog.setMessage(getResources().getString(R.string.joinIn_message));
            dialog.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialog.setCancelable(true);
                }
            });
            AlertDialog alert = dialog.create();
            alert.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_csdescription_of_awalk, menu);
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
     * sent participation for this walk to Server
     *
     * @param walkId
     */
    public void sentParticipantToServer(String walkId){

        // Create AsycHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        // Http Request Params Object
        RequestParams params = new RequestParams();
        //get the email of user
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        String email = prefs.getString("eMailId", "");

        final ProgressDialog prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
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
                    public void onSuccess(String response) {
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
                    public void onFailure(int statusCode, Throwable error,
                                          String content) {
                        // Hide Progress Dialog
                        prgDialog.hide();
                        if (prgDialog != null) {
                            prgDialog.dismiss();
                        }
                        // When Http response code is '404'
                        if (statusCode == 404) {
                            Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                        }
                        // When Http response code is '500'
                        else if (statusCode == 500) {
                            Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                        }
                        // When Http response code other than 404, 500
                        else {
                            Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might "
                                            + "not be connected to Internet or remote server is not up and running], check for other errors as well",
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
            SharedPreferences prefs = getSharedPreferences("UserDetails",
                    Context.MODE_PRIVATE);
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
