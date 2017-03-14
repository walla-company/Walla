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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import genieus.com.walla.R;
import genieus.com.walla.v1.Interests;
import genieus.com.walla.v2.adapter.listview.FriendsLVAdapter;
import genieus.com.walla.v2.api.WallaApi;
import genieus.com.walla.v2.info.Fonts;
import genieus.com.walla.v2.info.FriendInfo;
import genieus.com.walla.v2.info.UserInfo;

public class Friends extends AppCompatActivity implements FriendsLVAdapter.OnFriendStateListener, MenuItem.OnMenuItemClickListener {
    private ListView friends_lv;
    private TextView no_data;
    private ProgressBar progress;
    private FriendsLVAdapter adapter;
    private List<Integer> selected;
    private MenuItem done;
    private boolean doneIconVisible;
    private UserInfo user;
    private WallaApi api;
    private Fonts fonts;
    private FirebaseAuth auth;
    private JSONArray array;
    private List<FriendInfo> list;

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

        api = WallaApi.getInstance(this);
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
        fonts = new Fonts(this);
        progress.setVisibility(View.GONE);
        list = new ArrayList<>();

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

        no_data = (TextView) findViewById(R.id.no_data);
        no_data.setTypeface(fonts.AzoSansRegular);
        if(user.getFriends().isEmpty()){
            no_data.setVisibility(View.VISIBLE);
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

    private void returnData() {
        Intent returnIntent = new Intent();

        array = new JSONArray();
        for(int elm : selected){
            FriendInfo info = list.get(elm);
            JSONObject obj = new JSONObject();
            try {
                obj.put("uid", info.getUid());
                obj.put("name", info.getName());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            array.put(obj);
        }
        returnIntent.putExtra("result", array.toString());
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    private void getElm(){

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
    public void onFriendStateChanged(int pos, boolean checked) {
        if(!checked){
            selected.remove(new Integer(pos));
            if(selected.isEmpty()){
                done.setVisible(false);
                doneIconVisible = false;
            }
        }else{
            selected.add(pos);
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
