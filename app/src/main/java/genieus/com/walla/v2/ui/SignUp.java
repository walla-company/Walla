package genieus.com.walla.v2.ui;

import android.content.Intent;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.common.base.Optional;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import genieus.com.walla.R;
import genieus.com.walla.v2.api.WallaApi;
import genieus.com.walla.v2.datatypes.Domain;
import genieus.com.walla.v2.utils.Fonts;
import genieus.com.walla.v2.utils.RegexUtils;

public class SignUp extends AppCompatActivity
        implements OnCompleteListener<AuthResult>, OnFailureListener {
    @BindView(R.id.password)
    EditText passwordView;

    @BindView(R.id.first_name)
    EditText firstNameView;

    @BindView(R.id.last_name)
    EditText lastNameView;

    @BindView(R.id.explanation_msg)
    TextView explanationView;

    @BindView(R.id.confirmation_msg)
    TextView confirmationView;

    @BindView(R.id.first_name_label)
    TextView firstNameLabel;

    @BindView(R.id.last_name_label)
    TextView lastNameLabel;

    @BindView(R.id.password_label)
    TextView passwordLabel;

    private JSONObject data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        initUi();
    }

    private void initUi() {
        data = new JSONObject();
        Fonts.applyFont(Fonts.AzoSansRegular,
                firstNameView, lastNameView, passwordView,
                firstNameLabel, lastNameLabel, passwordLabel);

        confirmationView.setVisibility(View.GONE);
    }

    @OnClick(R.id.confirm)
    void attemptSignup() {
        final String email = getEmail().get();
        final String emailDomain = getDomailFromEmail(email);

        if (!RegexUtils.isValidEmail(email)) {
            Toast.makeText(this, "invalid email: " + email, Toast.LENGTH_LONG).show();
            return;
        }

        WallaApi.getAllowedDomains(new WallaApi.OnDataReceived() {
            @Override
            public void onDataReceived(Object data, int call) {
                List<Domain> domains = (List<Domain>) data;
                String domain = getDomailFromEmail(emailDomain);

                if (isDomainAllowed(domains, domain)) {
                    signUpUser();
                } else {
                    Toast.makeText(SignUp.this, String.format("%s is not supported at this time", domain), Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private void signUpUser() {
        if (getEmail().isPresent()) {
            final String email = getEmail().get();
            final String password = passwordView.getText().toString();

            FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this)
                    .addOnFailureListener(this);
        } else {
            finish();
        }
    }

    private Optional<String> getEmail() {
        final Bundle extras = getIntent().getExtras();

        if (extras != null && extras.containsKey("email")) {
            return Optional.of(extras.getString("email"));
        } else {
            Log.d("email error", "email not passed in intent");
            return Optional.absent();
        }
    }

    private boolean isDomainAllowed(List<Domain> domains, String search) {
        boolean found = false;
        for (Domain domain : domains) {
            if (search.equalsIgnoreCase(domain.getDomain())) {
                found = true;
                break;
            }
        }

        return found;
    }

    private String getDomailFromEmail(final String email) {
        String[] splitEmail = email.split("(\\.|@)");

        if (splitEmail.length >= 2) {
            final int len = splitEmail.length;
            return String.format("%s.%s", splitEmail[len - 2], splitEmail[len - 1]);
        }

        return null;
    }

    private void onSignUpSuccess() {
        Toast.makeText(this, "welcome to Walla!", Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, MainContainer.class));
    }

    private void onSignUpFail() {
        Toast.makeText(this, "Failed to create account", Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.explanation_msg)
    void onExplanationMessageClicked() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful() && task.isComplete()) {
            WallaApi.resetDomain(getEmail().get());

            try {
                data.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                data.put("first_name", firstNameView.getText().toString());
                data.put("last_name", lastNameView.getText().toString());
                data.put("email", getEmail().get());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            WallaApi.addUser(new WallaApi.OnDataReceived() {
                @Override
                public void onDataReceived(Object data, int call) {
                    Log.d("data json", data.toString());
                }
            }, data);

            onSignUpSuccess();

        } else {
            onSignUpFail();
        }
    }

    @Override
    public void onFailure(@NonNull Exception e) {

    }
}
