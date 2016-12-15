package genieus.com.walla.v2;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import genieus.com.walla.R;

public class Create extends AppCompatActivity {
    RecyclerView interests_rv;
    final String[] categories = new String[]{"Art", "School", "Sports", "Rides", "Games", "Food", "Other"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initUi();
    }

    private void initUi() {
        interests_rv = (RecyclerView) findViewById(R.id.interests_rv);
        List<String> data = new ArrayList<>(Arrays.asList(categories));
        RVAdapterFilter adapter = new RVAdapterFilter(data, new RVAdapterFilter.ItemClickListener() {
            @Override
            public void onItemClicked(String filter, int pos) {

            }
        }, this);

        LinearLayoutManager horizontal
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        interests_rv.setLayoutManager(horizontal);
        interests_rv.setAdapter(adapter);
    }

}
