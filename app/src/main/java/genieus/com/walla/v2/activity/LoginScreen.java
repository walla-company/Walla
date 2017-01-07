package genieus.com.walla.v2.activity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import genieus.com.walla.R;

public class LoginScreen extends AppCompatActivity implements View.OnClickListener {

    private ImageButton login, signup, forgot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        initUi();
    }

    private void initUi() {
        login = (ImageButton) findViewById(R.id.login);
        login.setOnClickListener(this);
        signup = (ImageButton) findViewById(R.id.signup);
        signup.setOnClickListener(this);
        forgot = (ImageButton) findViewById(R.id.forgot_password);
        forgot.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.login:
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
