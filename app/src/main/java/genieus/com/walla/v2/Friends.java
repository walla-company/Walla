package genieus.com.walla.v2;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import genieus.com.walla.R;

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
        List<Friend> data = new ArrayList<>();
        data.add(new Friend("Ben Yang", "Freshman", "Computer Science"));
        data.add(new Friend("Ben Yang", "Sophomore", "Pre-med"));
        data.add(new Friend("Ben Yang", "Junior", "Econ"));
        data.add(new Friend("Ben Yang", "Senior", "Undecided"));
        data.add(new Friend("Ben Yang", "Junior", "Music"));
        data.add(new Friend("Ben Yang", "Freshman", "Memology"));


        friends_lv = (ListView) findViewById(R.id.friends_lv);
        adapter = new FriendsLVAdapter(this, R.layout.single_friend, data);
        friends_lv.setAdapter(adapter);

    }

}
