package genieus.com.walla.v1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import genieus.com.walla.R;

public class Login extends AppCompatActivity implements View.OnClickListener{
    ProgressDialog loading;
    final String TAG = "msg";
    Button login;
    EditText email, pass;
    TextView signup, forgot;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        checkVersion();

        authenticate();
        initUi();

    }

    private void checkVersion(){
        mDatabase.child("minimumVersion").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> versions = (Map<String, Object>) dataSnapshot.getValue();
                String version = (String) versions.get("Android");
                String appVersion = "";
                try {
                    PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    appVersion = pInfo.versionName;
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                Log.wtf("version", appVersion + " and " + version);

                if(appVersion.compareTo(version) < 0){
                    Intent intent  = new Intent(Login.this, OutdatedVersion.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void showFeed(){
        Intent intent = new Intent(this, Activities.class);
        intent.putExtra("login", true);
        startActivity(intent);
    }

    private void  authenticate(){
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    showFeed();
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                }else{

                }
            }
        };
    }


    private void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        loading.cancel();
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(Login.this, "Authenication failed",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            showFeed();
                        }
                    }
                });
    }

    private void initUi(){
        loading = ProgressDialog.show(Login.this, "",
                "Authenticating...", true);
        loading.cancel();

        forgot = (TextView) findViewById(R.id.forgot);
        signup = (TextView) findViewById(R.id.signup);
        forgot.setOnClickListener(this);
        signup.setOnClickListener(this);

        email = (EditText) findViewById(R.id.enter_email);
        pass = (EditText) findViewById(R.id.enter_pass);

        pass.setTypeface(email.getTypeface());

        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);
    }

    private boolean legit(String em, String pa){
        return em != null && pa != null && !em.isEmpty() && !pa.isEmpty();
    }

    private void switchToSignup(){
        Intent intent = new Intent(this, Signup.class);
        startActivity(intent);
    }

    private void resetPassword(){
        Intent intent = new Intent(this, ResetPassword.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id){
            case R.id.signup:
                switchToSignup();
                break;
            case R.id.login:
                String em = email.getText().toString().trim();
                String pa = pass.getText().toString();

                if(legit(em, pa)) {
                    loading.show();
                    signIn(em, pa);
                }else{
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_LONG).show();

                }
                break;
            case R.id.forgot:
                resetPassword();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onBackPressed() {
        //nothing happens if back pressed on login screen
    }
}