package genieus.com.walla.v2.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import genieus.com.walla.R;
import genieus.com.walla.v2.adapter.listview.FriendsLVAdapter;
import genieus.com.walla.v2.info.FriendInfo;

public class Friends extends AppCompatActivity {
    private ListView friends_lv;
    private FriendsLVAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initUi();
    }

    private void initUi() {
        List<FriendInfo> data = new ArrayList<>();
        data.add(new FriendInfo("Ben Yang", "Freshman", "Computer Science"));
        data.add(new FriendInfo("Ben Yang", "Sophomore", "Pre-med"));
        data.add(new FriendInfo("Ben Yang", "Junior", "Econ"));
        data.add(new FriendInfo("Ben Yang", "Senior", "Undecided"));
        data.add(new FriendInfo("Ben Yang", "Junior", "Music"));
        data.add(new FriendInfo("Ben Yang", "Freshman", "Memology"));


        friends_lv = (ListView) findViewById(R.id.friends_lv);
        adapter = new FriendsLVAdapter(this, R.layout.single_friend, data);
        friends_lv.setAdapter(adapter);

    }

}
