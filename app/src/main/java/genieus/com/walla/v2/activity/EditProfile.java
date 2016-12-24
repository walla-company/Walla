package genieus.com.walla.v2.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import genieus.com.walla.R;
import genieus.com.walla.v2.info.Fonts;

public class EditProfile extends AppCompatActivity{
    private Fonts fonts;
    private TextView profile_pic_label, year_label, major_label, hometown_label, description_label, year_in, major_in, hometown_in, description_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initUi();
    }

    private void initUi() {
        fonts = new Fonts(this);

        profile_pic_label = (TextView) findViewById(R.id.profile_picture_label);
        profile_pic_label.setTypeface(fonts.AzoSansRegular);
        year_label = (TextView) findViewById(R.id.year_label);
        year_label.setTypeface(fonts.AzoSansRegular);
        major_label = (TextView) findViewById(R.id.major_label);
        major_label.setTypeface(fonts.AzoSansRegular);
        hometown_label = (TextView) findViewById(R.id.hometown_label);
        hometown_label.setTypeface(fonts.AzoSansRegular);
        description_label = (TextView) findViewById(R.id.description_label);
        description_label.setTypeface(fonts.AzoSansRegular);
        year_in = (TextView) findViewById(R.id.year_in);
        year_in.setTypeface(fonts.AzoSansRegular);
        major_in = (TextView) findViewById(R.id.major_in);
        major_in.setTypeface(fonts.AzoSansRegular);
        hometown_in = (TextView) findViewById(R.id.hometown_in);
        hometown_in.setTypeface(fonts.AzoSansRegular);
        description_in = (TextView) findViewById(R.id.description_in);
        description_in.setTypeface(fonts.AzoSansRegular);
    }

}
