package genieus.com.walla.v1;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import genieus.com.walla.R;

/**
 * Created by Anesu on 11/13/2016.
 */
public class EventReceiver extends BroadcastReceiver {
    String description, time, location, category, color, key, poster, uid;
    double rawTime;
    long people;
    boolean expired;

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager nm = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        Intent rintent = new Intent(context, ActivityDetails.class);

        Bundle extras = intent.getExtras();
        if (extras != null) {
            description = extras.getString("description");
            time = extras.getString("time");
            rawTime = extras.getDouble("rawTime");
            location = extras.getString("location");
            category = extras.getString("category");
            people = extras.getLong("people");
            color = extras.getString("color");
            key = extras.getString("key");
            poster = extras.getString("poster");
            uid = extras.getString("uid");
            expired = extras.getBoolean("expired");
        }

        rintent.putExtra("description", description);
        rintent.putExtra("time", time);
        rintent.putExtra("location", location);
        rintent.putExtra("people", people);
        rintent.putExtra("category", category);
        rintent.putExtra("color", color);
        rintent.putExtra("key", key);
        rintent.putExtra("poster", poster);
        rintent.putExtra("uid",uid);
        rintent.putExtra("expired", expired);
        rintent.putExtra("rawTime", rawTime);

        rintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, rintent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_notif_logo)
                .setContentTitle("Walla")
                .setContentText(String.format("EventInfo starting in 15 minutes: %s", description))
                .setAutoCancel(true);

        nm.notify(100, builder.build());
    }
}
