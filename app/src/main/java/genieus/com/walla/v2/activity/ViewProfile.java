package genieus.com.walla.v2.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import genieus.com.walla.R;
import genieus.com.walla.v2.adapter.listview.GroupProfileLVAdapter;
import genieus.com.walla.v2.info.GroupInfo;

public class ViewProfile extends AppCompatActivity {
    private ListView groups_lv;
    private GroupProfileLVAdapter groupsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initUi();
    }

    private void initUi() {
        List<GroupInfo> data = new ArrayList<>();
        data.add(new GroupInfo("Something Blue Something Borrowed", "SBSB", "#008080"));
        data.add(new GroupInfo("Mechanical Engineers", "MechEng", "#FFA07A"));
        data.add(new GroupInfo("Residential Assistants", "RA", "#1E90FF"));

        groups_lv = (ListView) findViewById(R.id.groups_lv);
        groupsAdapter = new GroupProfileLVAdapter(this, R.layout.single_group_profile, data);
        groups_lv.setAdapter(groupsAdapter);

    }

}
