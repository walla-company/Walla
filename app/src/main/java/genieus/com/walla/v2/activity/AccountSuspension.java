package genieus.com.walla.v2.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import genieus.com.walla.R;
import genieus.com.walla.v2.info.Fonts;

import static genieus.com.walla.v1.FirebaseInit.init;

public class AccountSuspension extends AppCompatActivity implements View.OnClickListener {

    private Button logout_btn;
    private Fonts fonts;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_suspension);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initUi();

    }

    private void initUi(){
        fonts = new Fonts(this);
        auth = FirebaseAuth.getInstance();

        logout_btn = (Button) findViewById(R.id.logout_btn);
        changeBackGroundColor(logout_btn, getResources().getColor(R.color.lightred));
        logout_btn.setTypeface(fonts.AzoSansBold);
        logout_btn.setOnClickListener(this);
    }

    private void logout(){
        auth.signOut();
        Intent intent = new Intent(this, LoginScreen.class);
        startActivity(intent);
    }

    private void changeBackGroundColor(View view, int color) {
        Drawable background = view.getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable) background).getPaint().setColor(color);
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable) background).setColor(color);
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable) background).setColor(color);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.logout_btn:
                logout();
                break;
            default:
                break;
        }
    }


    @Override
    public void onBackPressed() {
        logout();
    }
}
