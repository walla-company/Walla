package genieus.com.walla.v2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import genieus.com.walla.R;
import genieus.com.walla.v2.info.Fonts;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {

    private EditText email;
    private ImageButton login, send;
    private Fonts fonts;
    private FirebaseAuth auth;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        initUi();
    }

    private void initUi() {
        fonts = new Fonts(this);
        auth = FirebaseAuth.getInstance();

        email = (EditText) findViewById(R.id.email);
        email.setTypeface(fonts.AzoSansRegular);
        login = (ImageButton) findViewById(R.id.login);
        login.setOnClickListener(this);
        send = (ImageButton) findViewById(R.id.send);
        send.setOnClickListener(this);
    }

    private void sendRecoveryEmail(String email){
        auth.sendPasswordResetEmail(email);
        Toast.makeText(this, "Recovery email sent to " + email, Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, LoginScreen.class));
    }

    private boolean isValidEmail(String email){
        if(email == null || email.isEmpty()) return false;
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
        return matcher.find();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.login:
                startActivity(new Intent(this, LoginScreen.class));
                break;
            case R.id.send:
                String emailStr = email.getText().toString().trim().toLowerCase();
                if(isValidEmail(emailStr))
                    sendRecoveryEmail(emailStr);
                else
                    email.setError("enter a valid email");
                break;
            default:
                break;
        }
    }
}
