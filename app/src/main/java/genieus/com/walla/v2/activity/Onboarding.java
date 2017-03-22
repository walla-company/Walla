package genieus.com.walla.v2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;

import genieus.com.walla.R;
import genieus.com.walla.v2.api.WallaApi;

public class Onboarding extends AppCompatActivity {
    private RelativeLayout container;
    private ImageButton next, complete;
    private int page = 1;
    private WallaApi api;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        container = (RelativeLayout) findViewById(R.id.content_onboarding);
        next = (ImageButton) findViewById(R.id.next_btn);
        api = WallaApi.getInstance(this);
        auth = FirebaseAuth.getInstance();
        complete = (ImageButton)  findViewById(R.id.complete);
        complete.setVisibility(View.GONE);

        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Onboarding.this, EditProfile.class));
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page++;
                nextPage();
            }
        });
    }

    private void nextPage(){
        switch (page){
            case 2:
                container.setBackgroundResource(R.mipmap.onboarding_2);
                break;
            case 3:
                container.setBackgroundResource(R.mipmap.onboarding_3);
                break;
            case 4:
                complete.setVisibility(View.VISIBLE);
                container.setBackgroundResource(R.mipmap.onboarding_4);
                break;
            default:
                api.updateIntroComplete(auth.getCurrentUser().getUid());
                finish();
                break;
        }
    }

}
