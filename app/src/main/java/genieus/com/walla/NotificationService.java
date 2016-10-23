package genieus.com.walla;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Anesu on 10/22/2016.
 */
public class NotificationService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Intent intent = new Intent(this, Activities.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notifsBuilder = new NotificationCompat.Builder(this);
        notifsBuilder.setContentTitle("Walla");
        notifsBuilder.setContentText(remoteMessage.getNotification().getBody());
        notifsBuilder.setAutoCancel(true);
        notifsBuilder.setSmallIcon(R.mipmap.icon);
        notifsBuilder.setContentIntent(pIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notifsBuilder.build());
    }
}
