package genieus.com.walla.v2.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import genieus.com.walla.R;
import genieus.com.walla.v2.utils.Fonts;
import genieus.com.walla.v2.customviews.DotIndicatorView;

public class LoginActivity extends AppCompatActivity {
    private final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private int currentSlide = 1;
    private final int TOTAL_SLIDES = 3;

    private final int EMAIL_INVALID = 0;
    private final int EMAIL_NOT_EXISTS = 1;

    @BindView(R.id.more)
    TextView learnMore;

    @BindView(R.id.action_skip)
    Button actionSkip;

    @BindView(R.id.login_container)
    LinearLayout loginContainer;

    @BindView(R.id.login_slides)
    View loginSlides;

    @BindView(R.id.email_container)
    View emailContainer;

    @BindView(R.id.action)
    Button action;

    @BindView(R.id.text)
    TextView slideText;

    @BindView(R.id.image)
    ImageView slideImage;

    @BindView(R.id.email)
    EditText email;

    @BindView(R.id.dot_view)
    DotIndicatorView dotView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        ButterKnife.bind(this);

        initUi();
    }

    @OnClick(R.id.signup)
    public void signUp() {
        final Intent intent = new Intent(this, SignUp.class);
        intent.putExtra("email", email.getText().toString().trim());
        startActivity(intent);
    }


    @OnClick({R.id.login, R.id.action_skip})
    public void showEmailLogin() {
        loginContainer.setVisibility(View.GONE);
        loginSlides.setVisibility(View.GONE);
        emailContainer.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.confirm)
    public void onConfirmEmailClicked() {
        attemptLoginWithEmail(
                email.getText().toString().trim().toLowerCase()
        );
    }

    @OnClick(R.id.more)
    public void onLearnMoreClicked() {
        emailContainer.setVisibility(View.GONE);
        loginContainer.setVisibility(View.GONE);
        loginSlides.setVisibility(View.VISIBLE);

        slideText.setText(getResources().getString(R.string.slide_text_1));
    }

    @OnClick(R.id.action)
    public void onActionClicked() {
        if (currentSlide < TOTAL_SLIDES) {
            currentSlide++;
        } else {
            showEmailLogin();
            currentSlide = 1;
        }

        switch (currentSlide) {
            case 1:
                slideText.setText(getResources().getString(R.string.slide_text_1));
                slideImage.setImageDrawable(getResources().getDrawable(R.mipmap.login_slides_1));
                dotView.setActiveDot(1);
                actionSkip.setVisibility(View.VISIBLE);
                break;
            case 2:
                slideText.setText(getResources().getString(R.string.slide_text_2));
                slideImage.setImageDrawable(getResources().getDrawable(R.mipmap.walla_baketball));
                dotView.setActiveDot(2);
                break;
            case 3:
                slideText.setText(getResources().getString(R.string.slide_text_3));
                slideImage.setImageDrawable(getResources().getDrawable(R.mipmap.walla_heart));
                dotView.setActiveDot(3);
                actionSkip.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }
    }

    private void initUi() {
        slideText.setTypeface(Fonts.AzoSansRegular);
    }

    private boolean isValidEmail(String email){
        if(email == null || email.isEmpty()) return false;
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
        return matcher.find();
    }

    private void attemptLoginWithEmail(String emailStr){
        if(!isValidEmail(emailStr)){
            invalidLogin(EMAIL_INVALID);
            return;
        }

        final ProgressDialog loadingDialog;
        loadingDialog = ProgressDialog.show(this, "", "Authenticating...", true);
        loadingDialog.cancel();

        loadingDialog.show();
        FirebaseAuth.getInstance()
                .fetchProvidersForEmail(emailStr)
                .addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                loadingDialog.cancel();
                if(!task.isSuccessful()){
                    invalidLogin(EMAIL_INVALID);
                }else{
                    List<String> providers = task.getResult().getProviders();
                    if(providers.isEmpty()){
                        invalidLogin(EMAIL_NOT_EXISTS);
                    }else{
                        successfulLogin();
                    }
                }
            }
        });
    }

    private void invalidLogin(final int reason) {
        switch (reason) {
            case EMAIL_INVALID:
                email.setError("Invalid email");
                break;
            case EMAIL_NOT_EXISTS:
                signUp();
                break;
            default:
                break;
        }
    }

    private void successfulLogin() {
        final Intent intent = new Intent(this, LoginScreenPassword.class);
        intent.putExtra("email", email.getText().toString().trim());
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (loginContainer.getVisibility() == View.VISIBLE) {
            super.onBackPressed();
        } else {
            emailContainer.setVisibility(View.GONE);
            loginContainer.setVisibility(View.VISIBLE);
            loginSlides.setVisibility(View.GONE);
        }
    }
}
