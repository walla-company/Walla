package genieus.com.walla.v2.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import genieus.com.walla.R;
import genieus.com.walla.v2.adapter.listview.SuggestedGroupsLVAdapter;
import genieus.com.walla.v2.adapter.recyclerview.SuggestFriendsRVAdapter;
import genieus.com.walla.v2.api.WallaApi;
import genieus.com.walla.v2.info.Fonts;
import genieus.com.walla.v2.info.GroupInfo;
import genieus.com.walla.v2.info.MutualFriendInfo;
import genieus.com.walla.v2.info.UserInfo;

public class Search extends AppCompatActivity {
    private Fonts fonts;

    private RecyclerView suggested_friends_rv;
    private SuggestFriendsRVAdapter suggestFriendsAdapter;
    private ListView suggested_group_lv;
    private SuggestedGroupsLVAdapter suggestGroupAdapter;
    private SearchView search;

    private TextView friends_label, group_label;
    private WallaApi api;

    private List<UserInfo> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initUi();
    }

    private void initUi() {
        fonts = new Fonts(this);
        api = new WallaApi(this);

        search = (SearchView) findViewById(R.id.search);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                suggestGroupAdapter.getFilter().filter(search.getQuery());
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                suggestGroupAdapter.getFilter().filter(search.getQuery());
                return true;
            }
        });

        api.getSuggestedUsers(new WallaApi.OnDataReceived() {
            @Override
            public void onDataReceived(Object data, int call) {
                users = (List<UserInfo>) data;
                suggested_friends_rv = (RecyclerView) findViewById(R.id.suggested_friends_rv);
                suggestFriendsAdapter = new SuggestFriendsRVAdapter(Search.this, users);

                LinearLayoutManager horizontal = new LinearLayoutManager(Search.this, LinearLayoutManager.HORIZONTAL, false);
                suggested_friends_rv.setLayoutManager(horizontal);

                suggested_friends_rv.setAdapter(suggestFriendsAdapter);
            }
        });

        /*
        List<MutualFriendInfo> data = new ArrayList<>();
        data.add(new MutualFriendInfo(null, "Shray Gupta", 3));
        data.add(new MutualFriendInfo(null, "Shray Gupta", 3));
        data.add(new MutualFriendInfo(null, "Shray Gupta", 3));
        data.add(new MutualFriendInfo(null, "Shray Gupta", 3));
        data.add(new MutualFriendInfo(null, "Shray Gupta", 3));
        data.add(new MutualFriendInfo(null, "Shray Gupta", 3));
        data.add(new MutualFriendInfo(null, "Shray Gupta", 3));
        data.add(new MutualFriendInfo(null, "Shray Gupta", 3));
        data.add(new MutualFriendInfo(null, "Shray Gupta", 3));
        data.add(new MutualFriendInfo(null, "Shray Gupta", 3));
        */




        /*
        List<GroupInfo> data2 = new ArrayList<>();
        data2.add(new GroupInfo("Something Blue Something Borrowed", "SBSB", "#008080"));
        data2.add(new GroupInfo("Mechanical Engineers", "MechEng", "#FFA07A"));
        data2.add(new GroupInfo("Residential Assistants", "RA", "#1E90FF"));
        data2.add(new GroupInfo("Something Blue Something Borrowed", "SBSB", "#008080"));
        data2.add(new GroupInfo("Mechanical Engineers", "MechEng", "#FFA07A"));
        data2.add(new GroupInfo("Residential Assistants", "RA", "#1E90FF"));

        */

        suggested_group_lv = (ListView) findViewById(R.id.suggested_groups_lv);

        api.getSuggestedGroups(new WallaApi.OnDataReceived() {
            @Override
            public void onDataReceived(Object data, int call) {
                suggestGroupAdapter = new SuggestedGroupsLVAdapter(Search.this, R.layout.single_suggest_group, (List<GroupInfo>) data );
                suggested_group_lv.setAdapter(suggestGroupAdapter);
                suggestGroupAdapter.getFilter().filter("");
            }
        });

        friends_label = (TextView) findViewById(R.id.suggest_friends_label);
        group_label = (TextView) findViewById(R.id.suggest_group_label);
        friends_label.setTypeface(fonts.AzoSansRegular);
        group_label.setTypeface(fonts.AzoSansRegular);

    }

}
