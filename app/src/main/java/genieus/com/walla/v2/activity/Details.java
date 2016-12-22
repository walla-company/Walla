package genieus.com.walla.v2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Arrays;

import genieus.com.walla.R;
import genieus.com.walla.v2.adapter.recyclerview.TabRVAdapter;

public class Details extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView tabs;
    private RelativeLayout host_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initUi();
    }

    private void initUi() {
        tabs = (RecyclerView) findViewById(R.id.tabs_rv);
        LinearLayoutManager horizontal
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        tabs.setLayoutManager(horizontal);

        TabRVAdapter tabAdapter = new TabRVAdapter(this, new ArrayList<String>(Arrays.asList("Movies", "Academics")));
        tabs.setAdapter(tabAdapter);

        host_container = (RelativeLayout) findViewById(R.id.host_container);
        host_container.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id){
            case R.id.host_container:
                startActivity(new Intent(this, ViewProfile.class));
                break;
        }
    }
}
