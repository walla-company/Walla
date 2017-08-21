package genieus.com.walla.v2.datatypes;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by anesu on 1/29/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("push_notif", remoteMessage.toString() + " type: " + remoteMessage.getMessageType() + " data: " + remoteMessage.getData().toString());
    }
}
