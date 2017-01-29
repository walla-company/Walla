package genieus.com.walla.v2.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import genieus.com.walla.R;
import genieus.com.walla.v2.adapter.listview.EventsLVAdapter;
import genieus.com.walla.v2.api.WallaApi;
import genieus.com.walla.v2.info.EventInfo;
import genieus.com.walla.v2.info.Fonts;
import genieus.com.walla.v2.info.GroupInfo;

public class Group extends AppCompatActivity implements View.OnClickListener {
    private String BUTTONBLUE = "#63CAF9";
    private String BUTTONGREY = "#D8D8D8";
    private Fonts fonts;

    private ListView activities_lv;
    TextView memberInfo;
    private Button join_btn;
    private ProgressBar progress;
    private FirebaseAuth auth;

    private GroupInfo info;
    private boolean userInGroup;

    private WallaApi api;
    private String guid;

    private List<EventInfo> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        api = new WallaApi(this);
        auth = FirebaseAuth.getInstance();


        //String guid = "-K_QNzIlm31gNgepdtTk";
        Bundle extras = getIntent().getExtras();

        if(extras.containsKey("guid"))
            guid = extras.getString("guid");
        else
            finish();

        api.getGroup(new WallaApi.OnDataReceived() {
            @Override
            public void onDataReceived(Object data, int call) {
                info = (GroupInfo) data;
                initUi();
            }
        }, guid);
    }

    private void initUi() {
        fonts = new Fonts(this);

        progress = (ProgressBar) findViewById(R.id.progress);
        progress.setVisibility(View.GONE);

        activities_lv = (ListView) findViewById(R.id.group_activities_lv);
        list = new ArrayList<>();

        /*
        EventInfo event1 = new EventInfo();
        event1.setTitle("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
        event1.setInterests(Arrays.asList("Movies", "Academics"));
        event1.setInterested(7);
        event1.setGoing(5);
        event1.setStart_time((long) 742385);
        event1.setEnd_time((long) 749999);
        event1.setIs_public(true);

        EventInfo event2 = new EventInfo();
        event2.setTitle("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
        event2.setInterests(Arrays.asList("Movies", "Academics"));
        event2.setInterested(7);
        event2.setGoing(5);
        event2.setStart_time((long) 742385);
        event2.setEnd_time((long) 749999);
        event2.setIs_public(true);

        EventInfo event3 = new EventInfo();
        event3.setTitle("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
        event3.setInterests(Arrays.asList("Movies", "Academics"));
        event3.setInterested(7);
        event3.setGoing(5);
        event3.setStart_time((long) 742385);
        event3.setEnd_time((long) 749999);
        event3.setIs_public(true);

        List<EventInfo> data = new ArrayList<>();
        data.add(event1);
        data.add(event2);
        data.add(event3);
        */

        LayoutInflater myinflater = getLayoutInflater();
        ViewGroup myHeader = (ViewGroup)myinflater.inflate(R.layout.group_header_title, activities_lv, false);

        activities_lv.addHeaderView(myHeader, null, false);

        join_btn = (Button) activities_lv.findViewById(R.id.join_btn);
        join_btn.setOnClickListener(this);

        TextView title = (TextView) activities_lv.findViewById(R.id.title);
        title.setText(info.getName());
        title.setTypeface(fonts.AzoSansRegular);
        memberInfo = (TextView) activities_lv.findViewById(R.id.members_info);
        memberInfo.setTypeface(fonts.AzoSansRegular);
        if(info.getMembers() != null)
            memberInfo.setText(info.getMembers().size() + (info.getMembers().size() == 1 ? " member" : " members"));
        else
            memberInfo.setText("0 members");

        TextView abbr = (TextView) activities_lv.findViewById(R.id.abbr);
        abbr.setText(info.getAbbr());
        abbr.setTypeface(fonts.AzoSansRegular);
        TextView detailsLabel = (TextView) activities_lv.findViewById(R.id.details_label);
        detailsLabel.setTypeface(fonts.AzoSansRegular);
        TextView detailsIn = (TextView) activities_lv.findViewById(R.id.details_in);
        detailsIn.setTypeface(fonts.AzoSansMedium);
        detailsIn.setText(info.getDescription());
        RelativeLayout abbrContainer = (RelativeLayout) activities_lv.findViewById(R.id.group_icon_container);

        final EventsLVAdapter adapter = new EventsLVAdapter(this, R.layout.single_activity, list);
        activities_lv.setAdapter(adapter);

        adapter.getFilter().filter("");

        join_btn = (Button) findViewById(R.id.join_btn);

        try {
            changeBackgroundColor(abbrContainer, info.getColor());
        }catch(Exception e){
            Log.d("groupcolor", e.toString());
        }

        changeBackgroundColor(join_btn, BUTTONBLUE);

        if(info.getMembers() != null){
            if(info.getMembers().contains(auth.getCurrentUser().getUid())){
                join_btn.setText("Leave");
                changeBackgroundColor(join_btn, BUTTONGREY);
                userInGroup = true;
            }
        }else{
            info.setMembers(new ArrayList<String>());
        }

        for(String key : info.getActivities()){
            api.getActivity(new WallaApi.OnDataReceived() {
                @Override
                public void onDataReceived(Object data, int call) {
                    list.add((EventInfo) data);
                    adapter.notifyDataSetChanged();
                    adapter.getFilter().filter("");
                    Log.d("actdata", list.toString());
                }
            }, key);
        }

        getSupportActionBar().setTitle(info.getAbbr());
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

    private void joinGroup(){
        api.joinGroup(auth.getCurrentUser().getUid(), guid);
        userInGroup = true;
        join_btn.setText("Leave");
        changeBackgroundColor(join_btn, BUTTONGREY);

        info.getMembers().add(auth.getCurrentUser().getUid());
        memberInfo.setTypeface(fonts.AzoSansRegular);
        memberInfo.setText(info.getMembers().size() + (info.getMembers().size() == 1 ? " member" : " members"));
    }

    private void leaveGroup(){
        api.leaveGroup(auth.getCurrentUser().getUid(), guid);
        userInGroup = false;
        join_btn.setText("Join");
        changeBackgroundColor(join_btn, BUTTONBLUE);

        info.getMembers().remove(auth.getCurrentUser().getUid());
        memberInfo.setTypeface(fonts.AzoSansRegular);
        memberInfo.setText(info.getMembers().size() + (info.getMembers().size() == 1 ? " member" : " members"));
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
            case R.id.join_btn:
                if(userInGroup)
                    leaveGroup();
                else
                    joinGroup();
                break;
        }
    }
}
