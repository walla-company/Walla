package genieus.com.walla.v1;

import android.content.Intent;
import android.net.Uri;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import genieus.com.walla.R;

public class Signup extends AppCompatActivity implements View.OnClickListener {
    Button signup;
    TextView login;

    TextView terms;
    EditText name, email, pass;
    final String TAG = "msg";
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        authenticate(false, true);
        initUi();
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

    private void  authenticate(final boolean login, final boolean first){
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    showFeed(login, first);
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                }
            }
        };
    }

    private void showFeed(boolean first, boolean login){
        Intent intent = new Intent(this, Activities.class);
        intent.putExtra("Login", login);
        intent.putExtra("first", first);



        startActivity(intent);
    }

    private void initUi(){

        signup = (Button) findViewById(R.id.signup);
        login = (TextView) findViewById(R.id.login);

        email = (EditText) findViewById(R.id.enter_email);
        name = (EditText) findViewById(R.id.enter_name);
        pass = (EditText) findViewById(R.id.enter_pass);
        terms = (TextView) findViewById(R.id.terms);

        pass.setTypeface(email.getTypeface());

        signup.setOnClickListener(this);
        login.setOnClickListener(this);
        terms.setOnClickListener(this);
    }

    private void showTerms(){
        String url = "https://www.wallasquad.com/terms-and-conditions/";
        Intent in = new Intent(Intent.ACTION_VIEW);
        in.setData(Uri.parse(url));
        startActivity(in);
    }

    private void loginUser(){
        Intent intent = new Intent(this, Login.class);
        intent.putExtra("launch", false);
        startActivity(intent);
    }

    private void createUser(final String e, final String p){
        mAuth.signOut();
        mAuth.createUserWithEmailAndPassword(e, p)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        Log.d("user", e + " " + p );

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            //Toast.makeText(Signup.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                        }else{
                            String id = task.getResult().getUser().getUid();
                            mDatabase = FirebaseDatabase.getInstance().getReference();

                            if(e.trim().endsWith("@sandiego.edu")){
                                mDatabase = mDatabase.child("sandiego-*-edu");
                            }

                            Map<String, Object> info = new HashMap<>();
                            info.put("name", name.getText().toString().trim());
                            info.put("profile_image", "");
                            info.put("emailVerificationSent", false);
                            info.put("timeCreated", System.currentTimeMillis() / 1000);
                            mDatabase.child("users").child(id).setValue(info);

                            String[] interests = new String[]{"Art","School","Sports","Rides","Games","Food","Other"};
                            for(String i : interests) {
                                mDatabase.child("notification_settings").child(id).child(i.toLowerCase()).setValue(true);
                            }

                            /*
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            user.sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "Email sent.");
                                            }
                                        }
                                    });
                            */

                            authenticate(true, false);


                        }

                        // ...
                    }
                });
    }

    private boolean verified(){

        if(pass != null && pass.getText().toString().length() < 6){
            String msg = "it's 2016 bruh. Your password must be more than 5 characters";
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
            return false;
        }

        //// TODO: 11/1/2016 fix ugly fix for USD
        return email != null && (email.getText().toString().endsWith("@duke.edu") || email.getText().toString().endsWith("@sandiego.edu"))
            && pass != null;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id){
            case R.id.signup:
                if(verified()) {
                    try {
                        createUser(email.getText().toString().trim(), pass.getText().toString());
                    }catch(Exception e){
                        authenticate(true, false);
                    }
                }else{
                    Toast.makeText(this, "email or password could not be verified", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.login:
                loginUser();
                break;
            case R.id.terms:
                showTerms();
                break;
        }
    }
}
