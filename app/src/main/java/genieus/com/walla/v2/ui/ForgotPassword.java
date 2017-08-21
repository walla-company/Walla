package genieus.com.walla.v2.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import genieus.com.walla.R;
import genieus.com.walla.v2.utils.Fonts;
import genieus.com.walla.v2.utils.RegexUtils;

public class ForgotPassword extends AppCompatActivity {
    @BindView(R.id.email)
    EditText email;

    @BindView(R.id.confirm)
    ImageButton confirm;

    @BindView(R.id.back_btn)
    ImageButton back;

    @BindView(R.id.explanation_msg)
    TextView explanation;

    @BindView(R.id.confirmation_msg)
    TextView confirmation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);

        initUi();
    }

    private void initUi() {
        email.setTypeface(Fonts.AzoSansRegular);
        explanation.setTypeface(Fonts.AzoSansRegular);
        confirmation.setTypeface(Fonts.AzoSansRegular);

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
        FirebaseAuth.getInstance().sendPasswordResetEmail(email);
    }

    @OnClick(R.id.confirm)
    public void handleConfirmClick(){
        String emailStr = email.getText().toString().trim().toLowerCase();

        if(RegexUtils.isValidEmail(emailStr)) {
            sendRecoveryEmail(emailStr);
            initPostConfirmState();
        }
        else {
            email.setError(getString(R.string.invalid_email));
        }
    }

    @OnClick(R.id.back_btn)
    public void handleBackClick(){
        startActivity(new Intent(this, LoginActivity.class));
    }

}
