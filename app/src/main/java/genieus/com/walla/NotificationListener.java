package genieus.com.walla;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class NotificationListener extends Service implements ValueEventListener {
    private DatabaseReference mDatabase;
    FirebaseUser user;

    public NotificationListener() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("notifications");
        user = FirebaseAuth.getInstance().getCurrentUser();

        mDatabase.addValueEventListener(this);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Map<Object, Object> post = (Map<Object, Object>) dataSnapshot.getValue();
        Log.d("notificatios", post.toString() + "\n");
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
