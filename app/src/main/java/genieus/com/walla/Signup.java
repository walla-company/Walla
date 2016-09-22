package genieus.com.walla;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Signup extends AppCompatActivity implements View.OnClickListener {
    Button signup;
    Button login;
    EditText name, email, pass, pass2;
    final String TAG = "msg";
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
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
                // ...
            }
        };



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

    private void initUi(){
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();

        signup = (Button) findViewById(R.id.signup);
        login = (Button) findViewById(R.id.login);

        email = (EditText) findViewById(R.id.enter_email);
        name = (EditText) findViewById(R.id.enter_name);
        pass = (EditText) findViewById(R.id.enter_pass);
        pass2 = (EditText) findViewById(R.id.confirm_pass);

        pass.setTypeface(email.getTypeface());
        pass2.setTypeface(email.getTypeface());

        signup.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    private void loginUser(){
        Intent intent = new Intent(this, Login.class);
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
                            Toast.makeText(Signup.this, "authentication failed",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            String id = task.getResult().getUser().getUid();
                            mDatabase = FirebaseDatabase.getInstance().getReference();

                            Map<String, Object> info = new HashMap<>();
                            info.put("name", name.getText().toString());
                            info.put("profile_image", "");
                            info.put("emailVerificationSent", false);
                            info.put("timeCreated", System.currentTimeMillis() / 1000);
                            mDatabase.child("users").child(id).setValue(info);

                            String[] interests = new String[]{"Art","School","Sports","Rides","Games","Food","Other"};
                            for(String i : interests) {
                                mDatabase.child("notification_settings").child(id).child(i.toLowerCase()).setValue(true);
                            }


                            Intent intent = new Intent(Signup.this, Activities.class);
                            startActivity(intent);


                        }

                        // ...
                    }
                });
    }

    private boolean verified(){
        return email != null && email.getText().toString().endsWith("@duke.edu")
            && pass != null && pass2 != null && pass.getText().toString().equals(pass2.getText().toString());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id){
            case R.id.signup:
                if(verified())
                    createUser(email.getText().toString(), pass.getText().toString());
                break;
            case R.id.login:
                loginUser();
        }
    }
}
