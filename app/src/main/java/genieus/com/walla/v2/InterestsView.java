package genieus.com.walla.v2;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import genieus.com.walla.InterestsViewHolder;
import genieus.com.walla.R;

public class InterestsView extends AppCompatActivity implements View.OnClickListener {
    RelativeLayout movies, food, academic, study, sports, rides, exhibition, music, games, dance, socialize, other;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initUi();

    }

    private void initUi() {
        movies = (RelativeLayout) findViewById(R.id.movies);
        food = (RelativeLayout) findViewById(R.id.food);
        academic = (RelativeLayout) findViewById(R.id.academic);
        study = (RelativeLayout) findViewById(R.id.study);
        sports = (RelativeLayout) findViewById(R.id.sports);
        rides = (RelativeLayout) findViewById(R.id.rides);
        exhibition = (RelativeLayout) findViewById(R.id.exhibition);
        music = (RelativeLayout) findViewById(R.id.music);
        games = (RelativeLayout) findViewById(R.id.games);
        dance = (RelativeLayout) findViewById(R.id.dance);
        socialize = (RelativeLayout) findViewById(R.id.socialize);
        other = (RelativeLayout) findViewById(R.id.other);

        movies.setOnClickListener(this);
        food.setOnClickListener(this);
        academic.setOnClickListener(this);
        study.setOnClickListener(this);
        sports.setOnClickListener(this);
        rides.setOnClickListener(this);
        exhibition.setOnClickListener(this);
        music.setOnClickListener(this);
        games.setOnClickListener(this);
        dance.setOnClickListener(this);
        socialize.setOnClickListener(this);
        other.setOnClickListener(this);
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
