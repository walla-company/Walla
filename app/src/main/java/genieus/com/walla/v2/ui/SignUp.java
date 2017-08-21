package genieus.com.walla.v2.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import genieus.com.walla.R;
import genieus.com.walla.v2.api.WallaApi;
import genieus.com.walla.v2.datatypes.Domain;
import genieus.com.walla.v2.utils.Fonts;

public class SignUp extends AppCompatActivity implements View.OnClickListener, OnCompleteListener<AuthResult>, OnFailureListener {
    private EditText password, fname, lname;
    private TextView explanation, confirmation;
    private ImageButton confirm, back;

    private WallaApi api;
    private JSONObject data;
    private FirebaseAuth auth;
    private Fonts fonts;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private ProgressDialog loading;

    private static final int SIGN_UP_SUCCESS = 0;
    private static final int SIGN_UP_FAIL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initUi();
    }

    private void initUi() {
        api = WallaApi.getInstance(this);
        data = new JSONObject();
        fonts = new Fonts(this);
        auth = FirebaseAuth.getInstance();

        loading = ProgressDialog.show(this, "", "Creating account...", true);
        loading.cancel();

        confirm = (ImageButton) findViewById(R.id.confirm);
        confirm.setOnClickListener(this);

        fname = (EditText) findViewById(R.id.first_name);
        fname.setTypeface(fonts.AzoSansRegular);

        lname = (EditText) findViewById(R.id.last_name);
        lname.setTypeface(fonts.AzoSansRegular);

        password = (EditText) findViewById(R.id.password);
        password.setTypeface(fonts.AzoSansRegular);

        explanation = (TextView) findViewById(R.id.explation_msg);
        explanation.setTypeface(fonts.AzoSansRegular);
        explanation.setOnClickListener(this);

        confirmation = (TextView) findViewById(R.id.confirmation_msg);
        confirmation.setTypeface(fonts.AzoSansRegular);

        back = (ImageButton) findViewById(R.id.back_btn);
        back.setOnClickListener(this);

        initPreConfirmState();
    }

    private void initPreConfirmState(){
        confirmation.setVisibility(View.GONE);
        fname.setVisibility(View.VISIBLE);
        lname.setVisibility(View.VISIBLE);
        password.setVisibility(View.VISIBLE);
        confirm.setVisibility(View.VISIBLE);
    }

    private void initPostConfirmState(){
        confirmation.setVisibility(View.VISIBLE);
        fname.setVisibility(View.GONE);
        lname.setVisibility(View.GONE);
        password.setVisibility(View.GONE);
        confirm.setVisibility(View.GONE);
    }



    private void attemptSignup() {
        final String emailStr = getEmail();
        final String emailDomain = getDomailFromEmail(emailStr);

        if (!isValidEmail(emailStr)) {
            Toast.makeText(this, "invalid email: " + emailStr, Toast.LENGTH_LONG).show();
            return;
        }

        api.getAllowedDomains(new WallaApi.OnDataReceived() {
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
        String emailStr = getEmail();
        String passStr = password.getText().toString();

        auth.createUserWithEmailAndPassword(emailStr, passStr)
                .addOnCompleteListener(this)
                .addOnFailureListener(this);
    }

    private String getEmail() {
        Bundle extras = getIntent().getExtras();

        String emailStr = "";
        if (extras != null && extras.containsKey("email")) {
            emailStr = extras.getString("email");
        } else {
            Log.d("email bug", "email not passed in intent");
        }

        return emailStr;
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

    private String getDomailFromEmail(String emailStr) {
        String[] splitEmail = emailStr.split("(\\.|@)");

        String domain = "";
        if (splitEmail.length >= 2) {
            int len = splitEmail.length;
            domain = String.format("%s.%s", splitEmail[len - 2], splitEmail[len - 1]);
        }

        return domain;
    }

    private boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) return false;
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find();
    }

    private void handleSignUpResult(int state) {
        switch (state) {
            case SIGN_UP_SUCCESS:
                onSignUpSuccess();
                break;
            case SIGN_UP_FAIL:
                onSignUpFail();
                break;
            default:
                break;
        }

    }

    private void setOnBoardingCompleteListener(){
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(String.format("app_settings/schools/%s/%s/%s", api.getDomain(), auth.getCurrentUser().getUid(), "verified")).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean isVerified = (boolean) dataSnapshot.getValue();
                        if(isVerified){

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("Cancelled", "getUser:onCancelled", databaseError.toException());
                        // ...
                    }
                });
    }

    private void onSignUpSuccess() {
        Toast.makeText(this, "welcome to Walla!", Toast.LENGTH_LONG).show();
        initPostConfirmState();
    }

    private void onSignUpFail() {
        Toast.makeText(this, "Failed to create account", Toast.LENGTH_LONG).show();
    }

    private void loginScreen(){
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.confirm:
                attemptSignup();
                break;
            case R.id.back_btn:
                loginScreen();
                break;
            case R.id.explation_msg:
                loginScreen();
                break;
            default:
                break;
        }
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful() && task.isComplete()) {
            api.resetDomain(getEmail());

            try {
                data.put("uid", auth.getCurrentUser().getUid());
                data.put("first_name", fname.getText().toString());
                data.put("last_name", lname.getText().toString());
                data.put("email", getEmail());
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("json param", e.toString());
            }

            api.addUser(new WallaApi.OnDataReceived() {
                @Override
                public void onDataReceived(Object data, int call) {
                    Log.d("data json", data.toString());
                }
            }, data);

            handleSignUpResult(SIGN_UP_SUCCESS);

        } else {
            Log.d("signup fail", task.toString());
            handleSignUpResult(SIGN_UP_FAIL);
        }
    }

    @Override
    public void onFailure(@NonNull Exception e) {

    }
}
