package genieus.com.walla.v1;

import android.support.annotation.NonNull;
import android.util.Log;

import com.firebase.client.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Anesu on 9/16/2016.
 */
public class FirebaseInit implements FirebaseAuth.AuthStateListener{
    private final String TAG = "Walla";
    public static void init(){

    }

    @Override 
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            // User is signed in
            Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
        } else {
            // User is signed out
            Log.d(TAG, "onAuthStateChanged:signed_out");
        }
    }
}
