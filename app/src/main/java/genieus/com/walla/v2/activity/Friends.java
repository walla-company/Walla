package genieus.com.walla.v2.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import genieus.com.walla.R;
import genieus.com.walla.v1.Interests;
import genieus.com.walla.v2.adapter.listview.FriendsLVAdapter;
import genieus.com.walla.v2.api.WallaApi;
import genieus.com.walla.v2.info.FriendInfo;
import genieus.com.walla.v2.info.UserInfo;

public class Friends extends AppCompatActivity implements FriendsLVAdapter.OnFriendStateListener, MenuItem.OnMenuItemClickListener {
    private ListView friends_lv;
    private ProgressBar progress;
    private FriendsLVAdapter adapter;
    private List<String> selected;
    private MenuItem done;
    private boolean doneIconVisible;
    private UserInfo user;
    private WallaApi api;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("My Friends");

        auth = FirebaseAuth.getInstance();
        progress = (ProgressBar) findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);

        api = new WallaApi(this);
        String uid = auth.getCurrentUser().getUid();
        api.getUserInfo(new WallaApi.OnDataReceived() {
            @Override
            public void onDataReceived(Object data, int call) {
                user = (UserInfo) data;

                initUi();
            }
        }, uid);

    }

    private void initUi() {
        progress.setVisibility(View.GONE);
        final List<FriendInfo> list = new ArrayList<>();

        friends_lv = (ListView) findViewById(R.id.friends_lv);

        if(startedForResult()){
            selected = new ArrayList<>();
            adapter = new FriendsLVAdapter(this, this, R.layout.single_friend_select, list);
        }else {
            adapter = new FriendsLVAdapter(this, this, R.layout.single_friend, list);
        }

        friends_lv.setAdapter(adapter);

        for(String id : user.getFriends()){
            final String nuid = id;
            api.getUserInfo(new WallaApi.OnDataReceived() {
                @Override
                public void onDataReceived(Object data, int call) {
                    UserInfo friend = (UserInfo) data;
                    list.add(new FriendInfo(String.format("%s %s", friend.getFirst_name(), friend.getLast_name()),
                            friend.getYear(), friend.getMajor(), friend.getProfile_url(), nuid));

                    adapter.notifyDataSetChanged();
                }
            }, id);
        }

        /*
        data.add(new FriendInfo("Ben Yang", "Freshman", "Computer Science"));
        data.add(new FriendInfo("Ben Yang", "Sophomore", "Pre-med"));
        data.add(new FriendInfo("Ben Yang", "Junior", "Econ"));
        data.add(new FriendInfo("Ben Yang", "Senior", "Undecided"));
        data.add(new FriendInfo("Ben Yang", "Junior", "Music"));
        data.add(new FriendInfo("Ben Yang", "Freshman", "Memology"));
        */

    }

    private boolean startedForResult(){
        return getCallingActivity() != null;
    }

    private String listAsString(){
        StringBuffer sb = new StringBuffer();
        for(String name : selected) sb.append(", " + name);
        return sb.toString().substring(2);
    }

    private void returnData() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", listAsString());
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(startedForResult()) {
            done = menu.add(0, 0, 0, "DONE");
            done.setIcon(R.drawable.ic_done_all).setOnMenuItemClickListener(this).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            done.setVisible(false);
        }
        return true;
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
    public void onFriendStateChanged(String name, boolean checked) {
        if(!checked){
            selected.remove(name);
            if(selected.isEmpty()){
                done.setVisible(false);
                doneIconVisible = false;
            }
        }else{
            selected.add(name);
            if(!doneIconVisible){
                done.setVisible(true);
                doneIconVisible = true;
            }
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            default:
                returnData();
                break;
        }

        return true;
    }

}
