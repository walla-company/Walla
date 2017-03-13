package genieus.com.walla.v2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import genieus.com.walla.R;
import genieus.com.walla.v2.info.Fonts;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {

    private EditText email;
    private ImageButton confirm, back;
    private TextView explanation, confirmation;
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

        confirm = (ImageButton) findViewById(R.id.confirm);
        confirm.setOnClickListener(this);

        back = (ImageButton) findViewById(R.id.back_btn);
        back.setOnClickListener(this);

        explanation = (TextView) findViewById(R.id.explation_msg);
        explanation.setTypeface(fonts.AzoSansRegular);

        confirmation = (TextView) findViewById(R.id.confirmation_msg);
        confirmation.setTypeface(fonts.AzoSansRegular);

        initPreConfirmState();
    }

    private void initPreConfirmState(){
        back.setVisibility(View.GONE);
        confirmation.setVisibility(View.GONE);
        email.setVisibility(View.VISIBLE);
        confirm.setVisibility(View.VISIBLE);
    }

    private void initPostConfirmState(){
        back.setVisibility(View.VISIBLE);
        confirmation.setVisibility(View.VISIBLE);
        email.setVisibility(View.GONE);
        confirm.setVisibility(View.GONE);
    }

    private void sendRecoveryEmail(String email){
        auth.sendPasswordResetEmail(email);
    }

    private boolean isValidEmail(String email){
        if(email == null || email.isEmpty()) return false;
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
        return matcher.find();
    }

    private void handleConfimClick(){
        String emailStr = email.getText().toString().trim().toLowerCase();

        if(isValidEmail(emailStr)) {
            sendRecoveryEmail(emailStr);
            initPostConfirmState();
        }
        else {
            email.setError("invalid email");
        }
    }

    private void handleBackClick(){
        startActivity(new Intent(this, LoginScreenEmail.class));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.confirm:
                handleConfimClick();
                break;
            case R.id.back_btn:
                handleBackClick();
            default:
                break;
        }
    }
}
