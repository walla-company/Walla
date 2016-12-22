package genieus.com.walla.v1;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

import genieus.com.walla.R;

/**
 * Created by Anesu on 11/12/2016.
 */
public class NotificationReciever extends BroadcastReceiver implements ChildEventListener {

    private DatabaseReference mDatabase;
    private Context context;
    private SharedPreferences prefs;
    private final String LOGIN_TIME_KEY = "last_login";

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        mDatabase = FirebaseDatabase.getInstance().getReference();
        prefs = context.getSharedPreferences(
                "genieus.com.walla", Context.MODE_PRIVATE);

        searchForPostsSince(prefs.getLong(LOGIN_TIME_KEY, System.currentTimeMillis() / 1000));

    }

    private void searchForPostsSince(long time){
        mDatabase.child("activities").orderByChild("activityTime")
                .startAt(time)
                .addChildEventListener(this);
    }

    private void pushNotifications(String msg){
        NotificationManager nm = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        Intent rintent = new Intent(context, Activities.class);
        rintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, rintent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_notif_logo)
                .setContentTitle("Walla")
                .setContentText(msg)
                .setAutoCancel(true);

        nm.notify(100, builder.build());

        prefs.edit().putLong(LOGIN_TIME_KEY, System.currentTimeMillis() / 1000);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Map<Object, Object> act = (Map<Object, Object>) dataSnapshot.getValue();
            if(act.size() > 0){
                pushNotifications(act.size() + " new activities posted since you last checked!");
            }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
