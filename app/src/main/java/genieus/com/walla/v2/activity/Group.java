package genieus.com.walla.v2.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import genieus.com.walla.R;
import genieus.com.walla.v2.adapter.listview.EventsLVAdapter;
import genieus.com.walla.v2.info.EventInfo;
import genieus.com.walla.v2.info.Fonts;

public class Group extends AppCompatActivity {
    private String BUTTONBLUE = "#63CAF9";
    private Fonts fonts;

    private ListView activities_lv;
    private Button join_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initUi();

    }

    private void initUi() {
        fonts = new Fonts(this);

        activities_lv = (ListView) findViewById(R.id.group_activities_lv);
        List<EventInfo> list = new ArrayList<>();

        EventInfo event1 = new EventInfo();
        event1.setTitle("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
        event1.setInterests(Arrays.asList("Movies", "Academics"));
        event1.setInterested(7);
        event1.setGoing(5);
        event1.setStart_time((long) 742385);
        event1.setEnd_time((long) 749999);
        event1.setIs_public(true);

        EventInfo event2 = new EventInfo();
        event2.setTitle("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
        event2.setInterests(Arrays.asList("Movies", "Academics"));
        event2.setInterested(7);
        event2.setGoing(5);
        event2.setStart_time((long) 742385);
        event2.setEnd_time((long) 749999);
        event2.setIs_public(true);

        EventInfo event3 = new EventInfo();
        event3.setTitle("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
        event3.setInterests(Arrays.asList("Movies", "Academics"));
        event3.setInterested(7);
        event3.setGoing(5);
        event3.setStart_time((long) 742385);
        event3.setEnd_time((long) 749999);
        event3.setIs_public(true);

        List<EventInfo> data = new ArrayList<>();
        data.add(event1);
        data.add(event2);
        data.add(event3);

        LayoutInflater myinflater = getLayoutInflater();
        ViewGroup myHeader = (ViewGroup)myinflater.inflate(R.layout.group_header_title, activities_lv, false);

        activities_lv.addHeaderView(myHeader, null, false);

        TextView title = (TextView) activities_lv.findViewById(R.id.title);
        title.setTypeface(fonts.AzoSansRegular);
        TextView memberInfo = (TextView) activities_lv.findViewById(R.id.members_info);
        memberInfo.setTypeface(fonts.AzoSansRegular);
        TextView abbr = (TextView) activities_lv.findViewById(R.id.abbr);
        abbr.setTypeface(fonts.AzoSansRegular);
        TextView detailsLabel = (TextView) activities_lv.findViewById(R.id.details_label);
        detailsLabel.setTypeface(fonts.AzoSansRegular);
        TextView detailsIn = (TextView) activities_lv.findViewById(R.id.details_in);
        detailsIn.setTypeface(fonts.AzoSansMedium);

        EventsLVAdapter adapter = new EventsLVAdapter(this, R.layout.single_activity, list);
        activities_lv.setAdapter(adapter);

        adapter.getFilter().filter("");

        join_btn = (Button) findViewById(R.id.join_btn);
        changeBackgroundColor(join_btn, BUTTONBLUE);
    }

    private void changeBackgroundColor(View view, String color){
        Drawable background = view.getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable)background).getPaint().setColor(Color.parseColor(color));
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable)background).setColor(Color.parseColor(color));
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable)background).setColor(Color.parseColor(color));
        }
    }

}
