package genieus.com.walla.v2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import genieus.com.walla.R;

public class MainContainer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Home.OnFragmentInteractionListener, Calendar.OnFragmentInteractionListener, Notifications.OnFragmentInteractionListener, View.OnClickListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private com.github.clans.fab.FloatingActionMenu fab;

    private MenuItem filter_icon;

    private int[] tabIcons, tabIconsColored;
    private String[] tabNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_container);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
        fab.setMenuButtonColorNormal(getResources().getColor(R.color.DodgerBlue));
        fab.setMenuButtonColorPressed(getResources().getColor(R.color.DodgerBlue));
        fab.setClosedOnTouchOutside(true);

        com.github.clans.fab.FloatingActionButton createGroup = new com.github.clans.fab.FloatingActionButton(this);
        createGroup.setLabelText("Create a group");
        createGroup.setImageResource(R.drawable.ic_group);

        com.github.clans.fab.FloatingActionButton createPost = new com.github.clans.fab.FloatingActionButton(this);
        createPost.setLabelText("Create an event");
        createPost.setImageResource(R.drawable.ic_create);

        createGroup.setColorNormal(getResources().getColor(R.color.DodgerBlue));
        createPost.setColorNormal(getResources().getColor(R.color.DodgerBlue));
        createGroup.setColorPressed(getResources().getColor(R.color.DodgerBlue));
        createPost.setColorPressed(getResources().getColor(R.color.DodgerBlue));

        createPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab.close(true);
                startActivity(new Intent(MainContainer.this, Create.class));
            }
        });
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab.close(true);
            }
        });

        fab.addMenuButton(createPost);
        fab.addMenuButton(createGroup);

        fab.setClosedOnTouchOutside(true);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(Home.newInstance("home", ""), "Home");
        adapter.addFragment(Calendar.newInstance("calendar", ""), "Calendar");
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
        int id = item.getItemId();
        switch(id){
            case R.id.action_search:
                switchToSearchActivity();
                break;
            case R.id.action_filter:
                Home.showFilter();
                break;
            default:
                break;
        }

        return true;
    }

    private void switchToSearchActivity() {
        Intent intent = new Intent(this, Search.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch(id){
            case R.id.nav_my_interests:
                startActivity(new Intent(this, InterestsView.class));
                break;
            case R.id.nav_edit_profile:
                startActivity(new Intent(this, EditProfile.class));
                break;
            case R.id.nav_my_friends:
                startActivity(new Intent(this, Friends.class));
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

    }
}
