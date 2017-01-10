package genieus.com.walla.v2.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import genieus.com.walla.R;
import genieus.com.walla.v2.adapter.recyclerview.InterestsViewRVAdapter;
import genieus.com.walla.v2.api.WallaApi;
import genieus.com.walla.v2.info.InterestInfo;
import genieus.com.walla.v2.info.UserInfo;
import genieus.com.walla.v2.info.Utility;
import genieus.com.walla.v2.viewholder.InterestsViewHolder;

public class InterestsView extends AppCompatActivity implements InterestsViewRVAdapter.OnInterestStateChangedCListener, MenuItem.OnMenuItemClickListener {
    private RecyclerView interests_rv;
    private InterestsViewRVAdapter adapter;

    private List<InterestInfo> data;

    private List<String> selected;
    private MenuItem done;
    private boolean doneIconVisible;
    private int MAX_INTERESTS = 2;
    private double width;
    private WallaApi api;
    private UserInfo user;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        api = new WallaApi(this);
        interests_rv = (RecyclerView) findViewById(R.id.interests_rv);
        selected = new ArrayList<>();
        data = new ArrayList<>();
        auth = FirebaseAuth.getInstance();

       if(!startedForResult()){
            api.getUserInfo(new WallaApi.OnDataReceived() {
                @Override
                public void onDataReceived(Object result, int call) {
                    user = (UserInfo) result;
                    if(user.getInterests() == null){
                        Toast.makeText(InterestsView.this, "list is null 2", Toast.LENGTH_LONG).show();
                    }
                    initUi();
                }
            }, auth.getCurrentUser().getUid());
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    private void initUi() {
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

        if(!startedForResult()) {
            for (InterestInfo interest : data) {
                if (user.getInterests().contains(interest.getName())) {
                    interest.setSelected(true);
                }
            }

            selected.addAll(user.getInterests());
        }

        adapter = new InterestsViewRVAdapter(this, data, this, width);
        interests_rv.setAdapter(adapter);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        GridLayoutManager grid
                = new GridLayoutManager(this, 3);
        interests_rv.setLayoutManager(grid);

        int parentDp = Utility.pxToDp(interests_rv.getWidth());
        width = Utility.sizeToFit(parentDp, 3, 4);

        if(startedForResult()){
            initUi();
        }

    }

    @Override
    public void onInterestStanceChanged(int pos) {
        boolean chosen = !data.get(pos).isSelected();
        if (startedForResult()) {
            String name = data.get(pos).getName();
            if (!chosen) {
                selected.remove(name);
                if (selected.isEmpty()) {
                    done.setVisible(false);
                    doneIconVisible = false;
                    data.get(pos).setSelected(chosen);
                }
            } else {
                if (selected.size() >= MAX_INTERESTS)
                    Toast.makeText(this, "You can only selected a maximum of " + MAX_INTERESTS + " interests", Toast.LENGTH_LONG).show();
                else {
                    selected.add(name);
                    data.get(pos).setSelected(chosen);
                }
                if (!doneIconVisible) {
                    done.setVisible(true);
                    doneIconVisible = true;
                }
            }
        } else {
            String name = data.get(pos).getName();
            if (!chosen) {
                selected.remove(name);
                data.get(pos).setSelected(chosen);
            } else {
                selected.add(name);
                data.get(pos).setSelected(chosen);
            }
        }


        adapter.notifyDataSetChanged();
        //adapter = new InterestsViewRVAdapter(this, data, this, width);
        //interests_rv.setAdapter(adapter);
    }

    private boolean startedForResult() {
        return getCallingActivity() != null;
    }

    private void changeBackGroundColor(View view, int color) {
        Drawable background = view.getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable) background).getPaint().setColor(color);
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable) background).setColor(color);
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable) background).setColor(color);
        }
    }

    private void returnData() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", listAsString());
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private String listAsString() {
        StringBuffer sb = new StringBuffer();
        for (String name : selected) sb.append(", " + name);
        return sb.toString().substring(2);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (startedForResult()) {
            done = menu.add(0, 0, 0, "DONE");
            done.setIcon(R.drawable.ic_done_all).setOnMenuItemClickListener(this).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            done.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            default:
                returnData();
                break;
        }

        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(!startedForResult()) {
            JSONArray array = new JSONArray(selected);
            api.saveUserInterests(auth.getCurrentUser().getUid(), array);
        }
    }
}
