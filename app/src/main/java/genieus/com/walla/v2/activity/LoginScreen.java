package genieus.com.walla.v2.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import genieus.com.walla.R;
import genieus.com.walla.v1.Login;
import genieus.com.walla.v2.api.WallaApi;
import genieus.com.walla.v2.info.Fonts;

public class LoginScreen extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth auth;
    private Fonts fonts;
    private ImageButton login, signup, forgot;
    private EditText email, password;
    private ProgressDialog loadingDialog;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        initUi();
    }

    private void initUi() {
        fonts = new Fonts(this);
        auth = FirebaseAuth.getInstance();;
        email = (EditText) findViewById(R.id.email);
        email.setTypeface(fonts.AzoSansRegular);
        password = (EditText) findViewById(R.id.password);
        password.setTypeface(fonts.AzoSansRegular);
        login = (ImageButton) findViewById(R.id.login);
        login.setOnClickListener(this);
        signup = (ImageButton) findViewById(R.id.signup);
        signup.setOnClickListener(this);
        forgot = (ImageButton) findViewById(R.id.forgot_password);
        forgot.setOnClickListener(this);

        loadingDialog = ProgressDialog.show(this, "", "Authenticating...", true);
        loadingDialog.cancel();

    }

    private void attemptLogin(){
        final String emailStr = email.getText().toString().trim().toLowerCase();
        String passwordStr = password.getText().toString().trim().toString();

        if(!isValidEmail(emailStr)){
            email.setError("enter a valid email");
            return;
        }

        if(!isValidPassword(passwordStr)){
            password.setError("enter a valid password");
            return;
        }

        loadingDialog.show();

        auth.signInWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    loadingDialog.cancel();
                    WallaApi api = new WallaApi(LoginScreen.this);
                    api.resetDomain(emailStr);
                    startActivity(new Intent(LoginScreen.this, MainContainer.class));
                }else{
                    loadingDialog.cancel();
                    Toast.makeText(LoginScreen.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingDialog.cancel();
                Toast.makeText(LoginScreen.this, "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean isValidEmail(String email){
        if(email == null || email.isEmpty()) return false;
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
        return matcher.find();
    }

    private boolean isValidPassword(String password){
        if(password == null || password.isEmpty()) return false;
        return true;
    }

    @Override
    public void onBackPressed() {
        //nothing happens to prevent users from going back to main screen after logging out
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.login:
                auth.signOut();
                attemptLogin();
                break;
            case R.id.signup:
                startActivity(new Intent(this, SignUp.class));
                break;
            case R.id.forgot_password:
                startActivity(new Intent(this, ForgotPassword.class));
                break;
            default:
                break;
        }
    }
}
