package kiki__000.walkingstoursapp;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/**
 * Created by kiki__000 on 07-Jun-15.
 */
public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {


        ComponentName comp = new ComponentName(context.getPackageName(),
                GCMNotificationIntentService.class.getName());

        Log.i("here1", "Started bar service");
        startWakefulService(context, (intent.setComponent(comp)));


        Log.i("here2", "Started bar service");


        setResultCode(Activity.RESULT_OK);
    }

}
