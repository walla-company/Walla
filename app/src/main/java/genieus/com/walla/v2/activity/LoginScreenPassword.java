package genieus.com.walla.v2.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import java.util.regex.Matcher;

import genieus.com.walla.R;
import genieus.com.walla.v2.api.WallaApi;
import genieus.com.walla.v2.info.Fonts;

public class LoginScreenPassword extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth auth;
    private ImageButton confirm;
    private TextView explanation;
    private Fonts fonts;
    private EditText password;
    private ProgressDialog loadingDialog;

    private static final int LOGIN_SUCCESS = 0;
    private static final int LOGIN_FAIL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initUi();

    }

    private void initUi() {
        fonts = new Fonts(this);
        auth = FirebaseAuth.getInstance();
        ;
        password = (EditText) findViewById(R.id.password);
        password.setTypeface(fonts.AzoSansRegular);

        confirm = (ImageButton) findViewById(R.id.confirm);
        confirm.setOnClickListener(this);

        explanation = (TextView) findViewById(R.id.explation_msg);
        explanation.setTypeface(fonts.AzoSansRegular);
        explanation.setOnClickListener(this);

        loadingDialog = ProgressDialog.show(this, "", "Authenticating...", true);
        loadingDialog.cancel();

    }

    private boolean isValidPassword(String pass) {
        if (pass == null || pass.isEmpty()) return false;
        return true;
    }

    private void handleConfirmClick() {
        String emailStr = getEmail();
        String passStr = password.getText().toString();
        attemptLogin(emailStr, passStr);
    }

    private String getEmail(){
        Bundle extras = getIntent().getExtras();

        String emailStr = "";
        if (extras != null && extras.containsKey("email")) {
            emailStr = extras.getString("email");
        } else {
            Log.d("email bug", "email not passed in intent");
        }

        return emailStr;
    }

    private void handleLoginResult(int state) {
        switch (state){
            case LOGIN_SUCCESS:
                loginSuccess();
                break;
            case LOGIN_FAIL:
                loginFail();
                break;
        }
    }

    private void loginSuccess(){
        loadingDialog.cancel();

        String emailStr = getEmail();
        WallaApi api = new WallaApi(LoginScreenPassword.this);
        api.resetDomain(emailStr);
        startActivity(new Intent(LoginScreenPassword.this, MainContainer.class));
    }
    private void loginFail(){
        loadingDialog.cancel();
        Toast.makeText(LoginScreenPassword.this, "Authentication failed", Toast.LENGTH_SHORT).show();
    }

    //return true is user exists, false otherwise
    private void attemptLogin(final String emailStr, final String passStr) {
        if (!isValidPassword(passStr)) {
            handleLoginResult(LOGIN_FAIL);
            return;
        }

        //email was already verified

        loadingDialog.show();
        auth.signInWithEmailAndPassword(emailStr, passStr)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            handleLoginResult(LOGIN_SUCCESS);
                        } else {
                            handleLoginResult(LOGIN_FAIL);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        handleLoginResult(LOGIN_FAIL);
                    }
                });

    }

    private void forgotPassword() {
        startActivity(new Intent(this, ForgotPassword.class));
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.confirm:
                handleConfirmClick();
                break;
            case R.id.explation_msg:
                forgotPassword();
                break;
            default:
                break;
        }
    }
}
