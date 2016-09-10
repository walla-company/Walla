package genieus.com.walla;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

public class MyInterests extends AppCompatActivity {
    final String[] interests = new String[]{"Art", "School", "Sports", "Rides", "Games", "Food", "Other"};
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_interests);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initUi();
    }

    private void initUi(){
        lv = (ListView) findViewById(R.id.my_interests);
        MyInterestsAdapter adapter = new MyInterestsAdapter(this, R.layout.my_interests_template, interests);
        lv.setAdapter(adapter);
    }

}
