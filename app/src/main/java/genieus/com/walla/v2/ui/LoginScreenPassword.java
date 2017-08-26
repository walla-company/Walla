package genieus.com.walla.v2.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.common.base.Optional;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import genieus.com.walla.R;
import genieus.com.walla.v2.api.WallaApi;
import genieus.com.walla.v2.utils.Fonts;

public class LoginScreenPassword extends AppCompatActivity {
    @BindView(R.id.explanation_msg)
    TextView explanation;

    @BindView(R.id.password)
    EditText passwordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen_password);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initUi();
    }

    private void initUi() {
        Fonts.applyFont(Fonts.AzoSansRegular, passwordView, explanation);
        passwordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    onConfirmClick();
                }
                return true;
            }
        });
    }

    private boolean isValidPassword(final String password) {
        return !(password == null || password.isEmpty());
    }

    @OnClick(R.id.confirm)
    void onConfirmClick() {
        final Optional<String> emailOptional = getEmail();
        if (!emailOptional.isPresent()) {
            finish();
            return;
        }

        final String password = passwordView.getText().toString();
        attemptLogin(emailOptional.get(), password);
    }

    private Optional<String> getEmail() {
        final Bundle extras = getIntent().getExtras();

        if (extras != null && extras.containsKey("email")) {
            return Optional.of(extras.getString("email"));
        } else {
            Log.e("email error", "email is required, but not passed in intent");
            return Optional.absent();
        }
    }

    private void onLoginSuccess() {
        final Optional<String> emailOptional = getEmail();
        if (emailOptional.isPresent()) {
            WallaApi.resetDomain(emailOptional.get());
            startActivity(new Intent(LoginScreenPassword.this, MainContainer.class));
        } else {
            finish();
        }
    }

    private void onLoginFail() {
        Toast.makeText(this, "Authentication failed", Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.confirm)
    void onConfirmClicked() {
        if (getEmail().isPresent()) {
            attemptLogin(getEmail().get(), passwordView.getText().toString());
        } else {
            finish();
        }
    }
    void attemptLogin(final String email, final String password) {
        if (!isValidPassword(password)) {
            onLoginFail();
            return;
        }

        final ProgressDialog loadingDialog = ProgressDialog.show(this, "Loging in...", "", true);
        FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        loadingDialog.cancel();
                        if (task.isSuccessful()) {
                            onLoginSuccess();
                        } else {
                            onLoginFail();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onLoginFail();
                loadingDialog.cancel();
            }
        });

    }

    @OnClick(R.id.explanation_msg)
    void onForgotPasswordClicked() {
        startActivity(new Intent(this, ForgotPassword.class));
    }
}
