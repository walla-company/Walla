package genieus.com.walla;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ActivityDetails extends AppCompatActivity {
    String description, time, location, category, color;
    long people;

    TextView desc_tv, time_tv, location_tv, category_tv, people_tv, peopleList_tv;
    Button interested;
    RelativeLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            description = extras.getString("description");
            time = extras.getString("time");
            location = extras.getString("location");
            category = extras.getString("category");
            people = extras.getLong("people");
            color = extras.getString("color");
        }

        initUi();

    }

    private void initUi(){
        desc_tv = (TextView) findViewById(R.id.description);
        time_tv = (TextView) findViewById(R.id.time);
        location_tv = (TextView) findViewById(R.id.location);
        category_tv = (TextView) findViewById(R.id.category);
        people_tv = (TextView) findViewById(R.id.going);
        peopleList_tv = (TextView) findViewById(R.id.list_going);
        interested = (Button) findViewById(R.id.interested_btn);
        container = (RelativeLayout) findViewById(R.id.cat_container);

        desc_tv.setText(description);
        time_tv.setText(time);
        location_tv.setText("Location: " + location);
        category_tv.setText(category);
        people_tv.setText("People going: " + people);
        container.setBackgroundColor(Color.parseColor(color));
        category_tv.setTextColor(Color.parseColor(color));

    }


}
