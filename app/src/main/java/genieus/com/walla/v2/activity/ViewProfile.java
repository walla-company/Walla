package genieus.com.walla.v2.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import genieus.com.walla.R;
import genieus.com.walla.v2.adapter.listview.GroupProfileLVAdapter;
import genieus.com.walla.v2.api.WallaApi;
import genieus.com.walla.v2.info.Fonts;
import genieus.com.walla.v2.info.GroupInfo;

public class ViewProfile extends AppCompatActivity {
    private String BUTTONBLUE = "#63CAF9";

    private ListView groups_lv;
    private GroupProfileLVAdapter groupsAdapter;
    private TextView name, year, major, hometown, details_in, details_label;
    private Button add;
    private Fonts fonts;
    private WallaApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initUi();
    }

    private void initUi() {
        api = new WallaApi(this);
        fonts = new Fonts(this);

        String uid = getIntent().getExtras().getString("uid");
        if(uid != null){

        }else{
            Toast.makeText(this, "Error retrieving user", Toast.LENGTH_LONG).show();
        }

        List<GroupInfo> data = new ArrayList<>();
        data.add(new GroupInfo("Something Blue Something Borrowed", "SBSB", "#008080"));
        data.add(new GroupInfo("Mechanical Engineers", "MechEng", "#FFA07A"));
        data.add(new GroupInfo("Residential Assistants", "RA", "#1E90FF"));

        groups_lv = (ListView) findViewById(R.id.groups_lv);
        groupsAdapter = new GroupProfileLVAdapter(this, R.layout.single_group_profile, data);
        groups_lv.setAdapter(groupsAdapter);

        name = (TextView) findViewById(R.id.name);
        name.setTypeface(fonts.AzoSansMedium);
        year = (TextView) findViewById(R.id.year);
        year.setTypeface(fonts.AzoSansRegular);
        major = (TextView) findViewById(R.id.major);
        major.setTypeface(fonts.AzoSansRegular);
        hometown = (TextView) findViewById(R.id.hometowen);
        hometown.setTypeface(fonts.AzoSansRegular);
        details_in = (TextView) findViewById(R.id.details_in);
        details_in.setTypeface(fonts.AzoSansRegular);
        details_label = (TextView) findViewById(R.id.details_label);
        details_label.setTypeface(fonts.AzoSansMedium);
        add = (Button) findViewById(R.id.add_btn);
        add.setTypeface(fonts.AzoSansBold);
        changeBackgroundColor(add, BUTTONBLUE);

    }

    private void changeBackgroundColor(View view, String color){
        Drawable background = view.getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable)background).getPaint().setColor(Color.parseColor(color));
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable)background).setColor(Color.parseColor(color));
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable)background).setColor(Color.parseColor(color));
        }
    }

}
