package genieus.com.walla.v2.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import genieus.com.walla.R;
import genieus.com.walla.v2.adapter.recyclerview.InterestsViewRVAdapter;
import genieus.com.walla.v2.info.InterestInfo;
import genieus.com.walla.v2.info.Utility;
import genieus.com.walla.v2.viewholder.InterestsViewHolder;

public class InterestsView extends AppCompatActivity implements InterestsViewRVAdapter.OnInterestStateChangedCListener {
    private RecyclerView interests_rv;
    private InterestsViewRVAdapter adapter;

    private List<InterestInfo> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initUi();

    }

    private void initUi() {
        data = new ArrayList<>();
        data.add(new InterestInfo("Movies", R.mipmap.movieicon, false));
        data.add(new InterestInfo("Food", R.mipmap.foodicon, false));
        data.add(new InterestInfo("Academics", R.mipmap.academicicon, false));
        data.add(new InterestInfo("Study", R.mipmap.studyicon, false));
        data.add(new InterestInfo("Sports", R.mipmap.sportsicon, false));
        data.add(new InterestInfo("Rides", R.mipmap.ridesicon, false));
        data.add(new InterestInfo("Exhibition", R.mipmap.exhibitionicon, false));
        data.add(new InterestInfo("Music", R.mipmap.musicicon, false));
        data.add(new InterestInfo("Games", R.mipmap.gamesicon, false));
        data.add(new InterestInfo("Dance", R.mipmap.danceicon, false));
        data.add(new InterestInfo("Socialize", R.mipmap.socializeicon, false));
        data.add(new InterestInfo("Volunteer", R.mipmap.volunteeringicon, false));
        data.add(new InterestInfo("Other", R.mipmap.othericon, false));

        interests_rv = (RecyclerView) findViewById(R.id.interests_rv);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        GridLayoutManager grid
                = new GridLayoutManager(this, 3);
        interests_rv.setLayoutManager(grid);

        int parentDp = Utility.pxToDp(interests_rv.getWidth());
        double width = Utility.sizeToFit(parentDp, 3, 4);
        adapter = new InterestsViewRVAdapter(this, data, this, width);
        interests_rv.setAdapter(adapter);
    }

    @Override
    public void onInterestStanceChanged(int pos) {
        boolean selected = !data.get(pos).isSelected();
        data.get(pos).setSelected(selected);
        Toast.makeText(this, "clicked " + pos, Toast.LENGTH_LONG).show();
        adapter.notifyDataSetChanged();
    }

    private void changeBackGroundColor(View view, int color){
        Drawable background = view.getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable)background).getPaint().setColor(color);
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable)background).setColor(color);
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable)background).setColor(color);
        }
    }
}
