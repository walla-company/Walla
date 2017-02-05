package genieus.com.walla.v2.info;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import genieus.com.walla.R;
import genieus.com.walla.v1.Activities;

/**
 * Created by anesu on 1/29/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("push_notif", remoteMessage.toString() + " type: " + remoteMessage.getMessageType() + " data: " + remoteMessage.getData().toString());

        //displayNotification(remoteMessage);

    }

    private void displayNotification(RemoteMessage remoteMessage) {
        switch(remoteMessage.getMessageType()){

        }
        NotificationManager nm = (NotificationManager) getApplicationContext().getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
        Intent rintent = new Intent(getApplicationContext(), Activities.class);
        rintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 100, rintent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_notif_logo)
                .setContentTitle("Walla")
                .setContentText("")
                .setAutoCancel(true);

        nm.notify(100, builder.build());
    }
}
