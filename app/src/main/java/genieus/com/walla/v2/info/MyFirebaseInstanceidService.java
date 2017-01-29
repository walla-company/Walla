package genieus.com.walla.v2.info;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by anesu on 1/29/17.
 */

public class MyFirebaseInstanceidService extends FirebaseInstanceIdService {
    private static final String REG_TOKEN = "REG_TOKEN";
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(REG_TOKEN, token);
    }
}
