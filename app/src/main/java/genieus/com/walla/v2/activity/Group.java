package genieus.com.walla.v2.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import genieus.com.walla.R;
import genieus.com.walla.v2.adapter.listview.EventsLVAdapter;
import genieus.com.walla.v2.info.EventInfo;

public class Group extends AppCompatActivity {
    ListView activities_lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initUi();

    }

    private void initUi() {
        activities_lv = (ListView) findViewById(R.id.group_activities_lv);
        List<EventInfo> list = new ArrayList<>();

        list.add(new EventInfo("Dance", 4, "Lorem ipsum dolor sit amet, consectetur adipiscing elit.", EventInfo.Type.LIT, "6:45 PM", "7:55",2, 11, new ArrayList<String>(Arrays.asList("Movies", "Academics"))));
        list.add(new EventInfo("Academics", 6, "Lorem ipsum dolor sit amet, consectetur adipiscing elit.", EventInfo.Type.CHILL, "3:45 PM", "4:30", 4, 2, new ArrayList<String>(Arrays.asList("Sports","CS201"))));
        list.add(new EventInfo("Food", 6, "Lorem ipsum dolor sit amet, consectetur adipiscing elit.", EventInfo.Type.CHILL, "8:45 AM", "10:00", 7, 5, new ArrayList<String>(Arrays.asList("Dance", "Music", "Socialize"))));
        list.add(new EventInfo("Music", 7, "Lorem ipsum dolor sit amet, consectetur adipiscing elit.", EventInfo.Type.LIT, "12:45 PM", "2:30", 3, 13, new ArrayList<String>(Arrays.asList("Free Food"))));
        list.add(new EventInfo("Socialize", 11, "Lorem ipsum dolor sit amet, consectetur adipiscing elit.", EventInfo.Type.CHILL, "11:00 AM", "1:55", 7, 4, new ArrayList<String>(Arrays.asList("Music","Dab Squad"))));

        LayoutInflater myinflater = getLayoutInflater();
        ViewGroup myHeader = (ViewGroup)myinflater.inflate(R.layout.group_header_title, activities_lv, false);
        activities_lv.addHeaderView(myHeader, null, false);

        EventsLVAdapter adapter = new EventsLVAdapter(this, R.layout.single_activity, list);
        activities_lv.setAdapter(adapter);
    }

}
