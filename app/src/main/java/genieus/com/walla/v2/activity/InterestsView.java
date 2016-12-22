package genieus.com.walla.v2.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import genieus.com.walla.R;
import genieus.com.walla.v2.adapter.recyclerview.InterestsViewRVAdapter;
import genieus.com.walla.v2.info.InterestInfo;

public class InterestsView extends AppCompatActivity implements View.OnClickListener {
    RecyclerView interests_rv;
    InterestsViewRVAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initUi();

    }

    private void initUi() {
        List<InterestInfo> data = new ArrayList<>();
        data.add(new InterestInfo("Movies", R.mipmap.movieicon));
        data.add(new InterestInfo("Food", R.mipmap.foodicon));
        data.add(new InterestInfo("Academics", R.mipmap.academicicon));
        data.add(new InterestInfo("Study", R.mipmap.studyicon));
        data.add(new InterestInfo("Sports", R.mipmap.sportsicon));
        data.add(new InterestInfo("Rides", R.mipmap.ridesicon));
        data.add(new InterestInfo("Exhibition", R.mipmap.exhibitionicon));
        data.add(new InterestInfo("Music", R.mipmap.musicicon));
        data.add(new InterestInfo("Games", R.mipmap.gamesicon));
        data.add(new InterestInfo("Dance", R.mipmap.danceicon));
        data.add(new InterestInfo("Socialize", R.mipmap.socializeicon));
        data.add(new InterestInfo("Volunteer", R.mipmap.volunteeringicon));
        data.add(new InterestInfo("Other", R.mipmap.othericon));

        GridLayoutManager grid
                = new GridLayoutManager(this, 3);

        interests_rv = (RecyclerView) findViewById(R.id.interests_rv);
        interests_rv.setLayoutManager(grid);

        adapter = new InterestsViewRVAdapter(this, data);
        interests_rv.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {
        int color = Color.TRANSPARENT;
        Drawable background = v.getBackground();
        if (background instanceof ColorDrawable)
            color = ((ColorDrawable) background).getColor();
        v.setBackgroundColor(getResources().getColor(R.color.pinklight));
    }
}
