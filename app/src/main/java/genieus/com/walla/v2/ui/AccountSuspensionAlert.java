package genieus.com.walla.v2.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import genieus.com.walla.R;
import genieus.com.walla.v2.utils.Fonts;
import genieus.com.walla.v2.utils.ImageUtils;

public class AccountSuspensionAlert extends AppCompatActivity {

    @BindView(R.id.logout_btn)
    Button logoutBtn;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_suspension_alert);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initUi();
    }

    private void initUi(){
        auth = FirebaseAuth.getInstance();
        ImageUtils.changeDrawableColor(
                logoutBtn.getBackground(),getResources().getColor(R.color.lightRed)
        );

        logoutBtn.setTypeface(Fonts.AzoSansBold);
    }

    @OnClick(R.id.logout_btn)
    public void logout(){
        auth.signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        logout();
    }
}
