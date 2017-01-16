package genieus.com.walla.v2.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import genieus.com.walla.R;
import genieus.com.walla.v2.adapter.listview.GroupProfileLVAdapter;
import genieus.com.walla.v2.api.WallaApi;
import genieus.com.walla.v2.info.Fonts;
import genieus.com.walla.v2.info.GroupInfo;
import genieus.com.walla.v2.info.UserInfo;

public class ViewProfile extends AppCompatActivity implements View.OnClickListener {
    private String BUTTONBLUE = "#63CAF9";
    private String BUTTONGREY = "#D8D8D8";

    private CircleImageView profile_pic;
    private ListView groups_lv;
    private GroupProfileLVAdapter groupsAdapter;
    private TextView name, year, major, hometown, details_in, details_label;
    private Button add;
    private ProgressBar progress;
    private RelativeLayout container;
    private Fonts fonts;
    private WallaApi api;
    private UserInfo user;
    private FirebaseAuth auth;
    private AlertDialog.Builder confirm;
    private String[] options = {"Yes", "No"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        api = new WallaApi(this);
        auth = FirebaseAuth.getInstance();
        progress = (ProgressBar) findViewById(R.id.progress_bar);
        container = (RelativeLayout) findViewById(R.id.data_container);
        container.setVisibility(View.GONE);


        String uid = getIntent().getExtras().getString("uid");
        if(uid != null){
            api.getUserInfo(new WallaApi.OnDataReceived() {
                @Override
                public void onDataReceived(Object data, int call) {
                    user = (UserInfo) data;
                    container.setVisibility(View.VISIBLE);
                    progress.setVisibility(View.GONE);
                    initUi();
                }
            }, uid);

        }else{
            Toast.makeText(this, "Error retrieving user", Toast.LENGTH_LONG).show();
            finish();
        }

    }

    private void initUi() {
        fonts = new Fonts(this);
        getSupportActionBar().setTitle(String.format("%s %s", user.getFirst_name(), user.getLast_name()));
        List<GroupInfo> data = new ArrayList<>();
        data.add(new GroupInfo("Something Blue Something Borrowed", "SBSB", "#008080"));
        data.add(new GroupInfo("Mechanical Engineers", "MechEng", "#FFA07A"));
        data.add(new GroupInfo("Residential Assistants", "RA", "#1E90FF"));

        groups_lv = (ListView) findViewById(R.id.groups_lv);
        groupsAdapter = new GroupProfileLVAdapter(this, R.layout.single_group_profile, data);
        groups_lv.setAdapter(groupsAdapter);

        name = (TextView) findViewById(R.id.name);
        name.setTypeface(fonts.AzoSansMedium);
        name.setText(user.getFirst_name() + " " +  user.getLast_name());
        year = (TextView) findViewById(R.id.year);
        year.setTypeface(fonts.AzoSansRegular);
        year.setText(user.getYear());
        major = (TextView) findViewById(R.id.major);
        major.setTypeface(fonts.AzoSansRegular);
        major.setText(user.getMajor());
        hometown = (TextView) findViewById(R.id.hometowen);
        hometown.setTypeface(fonts.AzoSansRegular);
        hometown.setText(user.getHometown());
        details_in = (TextView) findViewById(R.id.details_in);
        details_in.setTypeface(fonts.AzoSansRegular);
        details_in.setText(user.getDescription());
        details_label = (TextView) findViewById(R.id.details_label);
        details_label.setTypeface(fonts.AzoSansMedium);
        add = (Button) findViewById(R.id.add_btn);
        add.setTypeface(fonts.AzoSansBold);
        add.setOnClickListener(this);
        profile_pic = (CircleImageView) findViewById(R.id.profile_picture);
        if(user.getFriends().contains(auth.getCurrentUser().getUid())) {
            add.setVisibility(View.GONE);
        }else{
            changeBackgroundColor(add, BUTTONBLUE);
            add.setText("Add friend");
        }

        if(user.getProfile_url() != null && !user.getProfile_url().equals("")) {
            Picasso.with(this) //Context
                    .load(user.getProfile_url()) //URL/FILE
                    .into(profile_pic);//an ImageView Object to show the loaded image
        }

        confirm = new AlertDialog.Builder(this);
        confirm.setTitle("Confirm");

        confirm.setTitle("Are you sure you want to friend " + user.getFirst_name());
        confirm.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        //yes
                        addFriend();
                        dialog.cancel();
                        break;
                    case 1:
                        //no
                        dialog.cancel();
                        break;
                    default:
                        dialog.cancel();
                        break;
                }
            }
        });

        confirm.setCancelable(false);

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

    private void addFriend(){
        api.requestFriend(auth.getCurrentUser().getUid(), user.getUid());
        add.setEnabled(false);
        changeBackgroundColor(add, BUTTONGREY);
        add.setText("Friend request sent");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.add_btn:
                confirm.show();
                break;
            default:
                break;
        }
    }
}
