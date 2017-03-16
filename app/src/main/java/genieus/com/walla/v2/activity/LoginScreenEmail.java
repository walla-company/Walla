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
import com.google.firebase.auth.ProviderQueryResult;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import genieus.com.walla.R;
import genieus.com.walla.v1.Login;
import genieus.com.walla.v2.api.WallaApi;
import genieus.com.walla.v2.info.Fonts;

public class LoginScreenEmail extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth auth;
    private Fonts fonts;
    private ImageButton confirm;
    private EditText email;
    private TextView explanation;
    private ProgressDialog loadingDialog;

    private final static int EMAIL_EXISTS = 0;
    private final static int EMAIL_NOT_EXISTS = 1;
    private final static int EMAIL_INVALID = 2;

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

        confirm = (ImageButton) findViewById(R.id.confirm);
        confirm.setOnClickListener(this);

        explanation = (TextView) findViewById(R.id.explation_msg);
        explanation.setTypeface(fonts.AzoSansRegular);

        loadingDialog = ProgressDialog.show(this, "", "Authenticating...", true);
        loadingDialog.cancel();

    }

    private boolean isValidEmail(String email){
        if(email == null || email.isEmpty()) return false;
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
        return matcher.find();
    }

    private void handleConfirmClick() {
        String emailStr = email.getText().toString();
        attemptLoginWithEmail(emailStr);
    }

    private void handleLoginResult(int state){
        String emailStr = email.getText().toString();

        switch (state){
            case EMAIL_EXISTS:
                nextLoginScreen(emailStr);
                break;
            case EMAIL_INVALID:
                email.setError("invalid");
                break;
            case EMAIL_NOT_EXISTS:
                signUpScreen(emailStr);
                break;
            default:
                break;
        }
    }

    private void attemptLoginWithEmail(String emailStr){
        if(!isValidEmail(emailStr)){
            //email is invalid
            handleLoginResult(EMAIL_INVALID);
        }

        loadingDialog.show();
        auth.fetchProvidersForEmail(emailStr).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                loadingDialog.cancel();
                if(!task.isSuccessful()){
                    handleLoginResult(EMAIL_INVALID);
                }else{
                    List<String> providers = task.getResult().getProviders();
                    if(providers.isEmpty()){
                        //means that the email is not registered
                        handleLoginResult(EMAIL_NOT_EXISTS);
                    }else{
                        handleLoginResult(EMAIL_EXISTS);
                    }
                }
            }
        });
    }

    private void nextLoginScreen(String emailStr){
        Intent intent = new Intent(this, LoginScreenPassword.class);
        intent.putExtra("email", emailStr);
        startActivity(intent);
    }

    private void signUpScreen(String emailStr){
        Intent intent = new Intent(this, SignUp.class);
        intent.putExtra("email", emailStr);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        //nothing happens to prevent users from going back to main screen after logging out
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.confirm:
                handleConfirmClick();
                break;
            default:
                break;
        }
    }
}
