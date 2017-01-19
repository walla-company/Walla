package genieus.com.walla.v2.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import genieus.com.walla.R;
import genieus.com.walla.v2.adapter.listview.MyGroupsLVAdapter;
import genieus.com.walla.v2.api.WallaApi;
import genieus.com.walla.v2.info.GroupInfo;
import genieus.com.walla.v2.info.UserInfo;

public class MyGroups extends AppCompatActivity implements MyGroupsLVAdapter.OnGroupStateChangeListener, MenuItem.OnMenuItemClickListener {
    private ListView group_lv;
    private ProgressBar progress;
    private RelativeLayout container;
    private MyGroupsLVAdapter adapter;
    private List<Integer> selected;
    private MenuItem done;
    private boolean doneIconVisible;
    private List<GroupInfo> data;
    private int max;
    private WallaApi api;
    private UserInfo user;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_groups);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        auth = FirebaseAuth.getInstance();
        progress = (ProgressBar) findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("My Groups");

        api.getUserInfo(new WallaApi.OnDataReceived() {
            @Override
            public void onDataReceived(Object data, int call) {
                user = (UserInfo) data;
                initUi();
            }
        }, auth.getCurrentUser().getUid());

    }

    private void initUi() {
        data = new ArrayList<>();
        progress.setVisibility(View.GONE);

        /*
        data.add(new GroupInfo("Something Blue Something Borrowed", "SBSB", "#008080"));
        data.add(new GroupInfo("Mechanical Engineers", "MechEng", "#FFA07A"));
        data.add(new GroupInfo("Residential Assistants", "RA", "#1E90FF"));
        data.add(new GroupInfo("Something Blue Something Borrowed", "SBSB", "#008080"));
        data.add(new GroupInfo("Mechanical Engineers", "MechEng", "#FFA07A"));
        data.add(new GroupInfo("Residential Assistants", "RA", "#1E90FF"));
        data.add(new GroupInfo("Something Blue Something Borrowed", "SBSB", "#008080"));
        data.add(new GroupInfo("Mechanical Engineers", "MechEng", "#FFA07A"));
        data.add(new GroupInfo("Residential Assistants", "RA", "#1E90FF"));
        */

        group_lv = (ListView) findViewById(R.id.my_groups_lv);
        selected = new ArrayList<>();

        if(startedForResult()) {
            max = getIntent().getExtras().getInt("max");
        }

        for(String key : user.getGroups()){
            api.getGroup(new WallaApi.OnDataReceived() {
                @Override
                public void onDataReceived(Object group, int call) {
                    data.add((GroupInfo) group);

                    if(startedForResult()) {
                        adapter = new MyGroupsLVAdapter(MyGroups.this, MyGroups.this, R.layout.single_group_select, data);

                        max = getIntent().getExtras().getInt("max");
                    }
                    else {
                        adapter = new MyGroupsLVAdapter(MyGroups.this, MyGroups.this, R.layout.single_group, data);
                    }

                    group_lv.setAdapter(adapter);
                }
            }, key);
        }
    }

    private boolean startedForResult(){
        return getCallingActivity() != null;
    }

    private void returnData() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", listAsString());
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    private String listAsString(){
        JSONArray array = new JSONArray();
       for(int elm : selected){
           JSONObject group = new JSONObject();
           try {
               group.put("guid", data.get(elm).getGuid());
               group.put("name", data.get(elm).getName());
               group.put("abbr", data.get(elm).getAbbr());
               group.put("color", data.get(elm).getColor());
           } catch (JSONException e) {
               e.printStackTrace();
           }

           array.put(group);
       }

        return array.toString();
    }

    @Override
    public boolean onGroupStateChange(int pos, boolean checked) {
        if(!checked){
            if(selected.contains(pos))
                selected.remove(new Integer(pos));
            if(selected.isEmpty()){
                done.setVisible(false);
                doneIconVisible = false;
            }
        }else{
            if(max > 0 && selected.size() >= max){
                Toast.makeText(this, "You can host on behalf of only 1 group", Toast.LENGTH_LONG).show();
                return false;
            }
            selected.add(pos);
            if(!doneIconVisible){
                done.setVisible(true);
                doneIconVisible = true;
            }
        }

        return true;
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
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            default:
                returnData();
                break;
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
}
