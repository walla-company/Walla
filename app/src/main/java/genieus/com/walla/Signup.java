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

public class Signup extends AppCompatActivity implements View.OnClickListener {
    Button signup;
    Button login;
    EditText name, email, pass, pass2;
    final String TAG = "msg";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initUi();
    }

    private void initUi(){
        mAuth = FirebaseAuth.getInstance();

        signup = (Button) findViewById(R.id.signup);
        login = (Button) findViewById(R.id.login);

        email = (EditText) findViewById(R.id.enter_email);
        name = (EditText) findViewById(R.id.enter_name);
        pass = (EditText) findViewById(R.id.enter_pass);
        pass2 = (EditText) findViewById(R.id.confirm_pass);

        signup.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    private void loginUser(){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    private void createUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(Signup.this, "authentication failed",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id){
            case R.id.signup:
                //createUser();
                break;
            case R.id.login:
                loginUser();
        }
    }
}
