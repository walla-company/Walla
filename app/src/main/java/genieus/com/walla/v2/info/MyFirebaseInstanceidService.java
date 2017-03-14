package genieus.com.walla.v2.info;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import genieus.com.walla.v2.api.WallaApi;

/**
 * Created by anesu on 1/29/17.
 */

public class MyFirebaseInstanceidService extends FirebaseInstanceIdService {
    private static final String REG_TOKEN = "REG_TOKEN";
    WallaApi api;
    FirebaseAuth auth;
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        api = WallaApi.getInstance(getApplicationContext());
        auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser() == null){
            Log.d("notifdata", "user is null");
            return;
        }

        String token = FirebaseInstanceId.getInstance().getToken();
        api.registerToken(auth.getCurrentUser().getUid(), token);

        Log.d(REG_TOKEN, token);
    }
}
