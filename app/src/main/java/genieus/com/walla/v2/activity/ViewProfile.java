package genieus.com.walla.v2.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import genieus.com.walla.R;
import genieus.com.walla.v2.adapter.listview.EventsLVAdapter;
import genieus.com.walla.v2.adapter.listview.GroupProfileLVAdapter;
import genieus.com.walla.v2.api.WallaApi;
import genieus.com.walla.v2.info.Event;
import genieus.com.walla.v2.info.Fonts;
import genieus.com.walla.v2.info.GroupInfo;
import genieus.com.walla.v2.info.User;

public class ViewProfile extends AppCompatActivity implements View.OnClickListener, WallaApi.OnDataReceived {
    private String BUTTONBLUE = "#63CAF9";
    private String BUTTONGREY = "#D8D8D8";

    private CircleImageView profile_pic;
    private ListView groups_lv, events_lv;
    private GroupProfileLVAdapter groupsAdapter;
    private EventsLVAdapter adapterEvents;
    private CardView details_container;
    private TextView name, year, major, hometown, details_in, details_label;
    private Button add;
    private ProgressBar progress;
    private RelativeLayout container;
    private Fonts fonts;
    private WallaApi api;
    private User user;
    private FirebaseAuth auth;
    private AlertDialog.Builder confirm;
    private String[] options = {"Yes", "No"};
    private List<Event> events;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        api = WallaApi.getInstance(this);
        auth = FirebaseAuth.getInstance();
        progress = (ProgressBar) findViewById(R.id.progress_bar);
        progress.setVisibility(View.VISIBLE);
        container = (RelativeLayout) findViewById(R.id.data_container);
        container.setVisibility(View.GONE);


        String uid = getIntent().getExtras().getString("uid");
        if(uid != null){
            api.getUserInfo(new WallaApi.OnDataReceived() {
                @Override
                public void onDataReceived(Object data, int call) {
                    user = (User) data;
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
        progress.setVisibility(View.GONE);
        fonts = new Fonts(this);
        getSupportActionBar().setTitle(String.format("%s %s", user.getFirst_name(), user.getLast_name()));
        List<GroupInfo> data = new ArrayList<>();
        events = new ArrayList<>();
        data.add(new GroupInfo("Something Blue Something Borrowed", "SBSB", "#008080"));
        data.add(new GroupInfo("Mechanical Engineers", "MechEng", "#FFA07A"));
        data.add(new GroupInfo("Residential Assistants", "RA", "#1E90FF"));

        events_lv = (ListView) findViewById(R.id.events);

        View header = LayoutInflater.from(this).inflate(R.layout.user_profile_header, null);
        events_lv.addHeaderView(header);

        adapterEvents = new EventsLVAdapter(this, R.layout.single_activity, events);
        events_lv.setAdapter(adapterEvents);
        adapterEvents.getFilter().filter("");

        groups_lv = (ListView) findViewById(R.id.groups_lv);
        groupsAdapter = new GroupProfileLVAdapter(this, R.layout.single_group_profile, data);
        groups_lv.setAdapter(groupsAdapter);

        if(user.getActivities() != null) {
            for (String key : user.getActivities()) {
                api.getActivity(this, key);
            }
        }

        name = (TextView) events_lv.findViewById(R.id.name);
        name.setTypeface(fonts.AzoSansMedium);
        name.setText(user.getFirst_name() + " " +  user.getLast_name());
        year = (TextView) events_lv.findViewById(R.id.year);
        year.setTypeface(fonts.AzoSansRegular);
        year.setText(user.getYear());
        major = (TextView) events_lv.findViewById(R.id.major);
        major.setTypeface(fonts.AzoSansRegular);
        major.setText(user.getMajor());
        hometown = (TextView) events_lv.findViewById(R.id.hometowen);
        hometown.setTypeface(fonts.AzoSansRegular);
        hometown.setText(user.getHometown());
        details_in = (TextView) events_lv.findViewById(R.id.details_in);
        details_in.setTypeface(fonts.AzoSansRegular);
        details_in.setText(user.getDescription());
        details_label = (TextView) events_lv.findViewById(R.id.details_label);
        details_label.setTypeface(fonts.AzoSansMedium);
        if(user.getDescription() == null || user.getDescription().isEmpty())
            details_container.setVisibility(View.GONE);
        add = (Button) findViewById(R.id.add_btn);
        add.setTypeface(fonts.AzoSansBold);
        add.setOnClickListener(this);
        profile_pic = (CircleImageView) events_lv.findViewById(R.id.profile_picture);

        Log.d("reqdata",user.getReceived_requests().toString());

        if(user.getFriends().contains(auth.getCurrentUser().getUid())) {
            add.setVisibility(View.GONE);
        }else if(user.getReceived_requests().contains(auth.getCurrentUser().getUid())){
            add.setEnabled(false);
            changeBackgroundColor(add, BUTTONGREY);
            add.setText("Friend request sent");
        }else{
            changeBackgroundColor(add, BUTTONBLUE);
            add.setText("Add friend");
        }

        if(user.getProfile_url() != null && !user.getProfile_url().equals("")) {
            if(!user.getProfile_url().startsWith("gs://walla-launch.appspot.com")) {
                Picasso.with(ViewProfile.this) //Context
                        .load(user.getProfile_url()) //URL/FILE
                        .into(profile_pic);//an ImageView Object to show the loaded image;
            }else{
                FirebaseStorage storage = FirebaseStorage.getInstance();
                storage.getReferenceFromUrl(user.getProfile_url()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            Picasso.with(ViewProfile.this) //Context
                                    .load(task.getResult().toString()) //URL/FILE
                                    .into(profile_pic);//an ImageView Object to show the loaded image;
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
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

        if(user.getFriends().contains(auth.getCurrentUser().getUid())){
            add.setEnabled(false);
            changeBackgroundColor(add, BUTTONGREY);
            //prevents someone fron removing a friend
        }

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

    @Override
    public void onDataReceived(Object data, int call) {
        events.add((Event) data);
        events_lv.setAdapter(adapterEvents);
        adapterEvents.getFilter().filter("");
    }
}
