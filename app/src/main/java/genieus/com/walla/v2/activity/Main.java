package genieus.com.walla.v2.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;

import genieus.com.walla.*;
import genieus.com.walla.v2.fragment.Notifications;
import genieus.com.walla.v2.adapter.viewpager.ViewPagerAdapter;
import genieus.com.walla.v2.fragment.Home;

public class Main extends AppCompatActivity implements Home.OnFragmentInteractionListener, genieus.com.walla.v2.fragment.Calendar.OnFragmentInteractionListener, Notifications.OnFragmentInteractionListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private com.github.clans.fab.FloatingActionMenu fab;

    private MenuItem filter_icon;

    private int[] tabIcons, tabIconsColored;
    private String[] tabNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initUi();
    }

    private void initUi() {
        tabIcons = new int[]{R.mipmap.ic_home, R.mipmap.ic_calendar, R.mipmap.ic_notifications,};
        tabIconsColored = new int[]{R.mipmap.ic_home_c, R.mipmap.ic_calendar_c, R.mipmap.ic_notifications_c,};
        tabNames = new String[]{"Activities", "Calendar", "Notifications"};

        getSupportActionBar().setTitle(tabNames[0]);

        setupFab();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    private void setupFab() {
        fab = (com.github.clans.fab.FloatingActionMenu) findViewById(R.id.fab);

        FloatingActionButton createGroup = new FloatingActionButton(this);
        createGroup.setLabelText("Create a group");
        createGroup.setImageResource(R.drawable.ic_group);

        FloatingActionButton createPost = new FloatingActionButton(this);
        createPost.setLabelText("Create an event");
        createPost.setImageResource(R.drawable.ic_create);


        fab.addMenuButton(createGroup);
        fab.addMenuButton(createPost);

        fab.setClosedOnTouchOutside(true);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(Home.newInstance("home", ""), "Home");
        adapter.addFragment(genieus.com.walla.v2.fragment.Calendar.newInstance("calendar", ""), "Calendar");
        adapter.addFragment(Notifications.newInstance("notifications", ""), "Notifications");

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                switchTabIcons(position);
                changeActionBarTitle(position);
                changeTabMenuItems(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        viewPager.setAdapter(adapter);
    }

    private void changeTabMenuItems(int position) {
        switch(position){
            case 0:
                filter_icon.setVisible(true);
                break;
            case 1:
                filter_icon.setVisible(false);
                break;
            case 2:
                filter_icon.setVisible(false);
                break;
        }
    }

    private void changeActionBarTitle(int position) {
        switch(position){
            case 0:
                getSupportActionBar().setTitle(tabNames[0]);
                break;
            case 1:
                getSupportActionBar().setTitle(tabNames[1]);
                break;
            case 2:
                getSupportActionBar().setTitle(tabNames[2]);
                break;
        }
    }

    private void switchTabIcons(int position) {
        switch(position){
            case 0:
                tabLayout.getTabAt(0).setIcon(tabIconsColored[0]);
                tabLayout.getTabAt(1).setIcon(tabIcons[1]);
                tabLayout.getTabAt(2).setIcon(tabIcons[2]);
                break;
            case 1:
                tabLayout.getTabAt(0).setIcon(tabIcons[0]);
                tabLayout.getTabAt(1).setIcon(tabIconsColored[1]);
                tabLayout.getTabAt(2).setIcon(tabIcons[2]);
                break;
            case 2:
                tabLayout.getTabAt(0).setIcon(tabIcons[0]);
                tabLayout.getTabAt(1).setIcon(tabIcons[1]);
                tabLayout.getTabAt(2).setIcon(tabIconsColored[2]);
                break;
            default:
                break;
        }
    }

    private void setupTabIcons() {

        tabLayout.getTabAt(0).setIcon(tabIconsColored[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Toast.makeText(this, uri.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        filter_icon = menu.findItem(R.id.action_filter);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return true;
    }

}