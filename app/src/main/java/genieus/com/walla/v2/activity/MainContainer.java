package genieus.com.walla.v2.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import genieus.com.walla.R;
import genieus.com.walla.v2.api.WallaApi;
import genieus.com.walla.v2.fragment.Notifications;
import genieus.com.walla.v2.adapter.viewpager.ViewPagerAdapter;
import genieus.com.walla.v2.fragment.Calendar;
import genieus.com.walla.v2.fragment.Home;
import genieus.com.walla.v2.info.DomainInfo;
import genieus.com.walla.v2.info.EventInfo;
import genieus.com.walla.v2.info.Fonts;
import genieus.com.walla.v2.info.UserInfo;
import genieus.com.walla.v2.info.Utility;

public class MainContainer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Home.OnFragmentInteractionListener, Calendar.OnFragmentInteractionListener, Notifications.OnFragmentInteractionListener, View.OnClickListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private com.github.clans.fab.FloatingActionMenu fab;
    private static CircleImageView profile_pic;
    private static TextView name;
    private NavigationView navigationView;
    private View navHeader;
    private static UserInfo user;

    private MenuItem filter_icon;
    private Fonts fonts;
    private static WallaApi api;
    private static FirebaseAuth auth;
    private static Context context;

    private int[] tabIcons, tabIconsColored;
    private String[] tabNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_container);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupFab();

        auth = FirebaseAuth.getInstance();
        if(!isLoggedIn()){
            startActivity(new Intent(this, LoginScreen.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        context = getApplicationContext();

        initUi();
        initShortcuts();
        //testApi();
    }

    private boolean isLoggedIn(){
        return auth != null && auth.getCurrentUser() != null;
    }

    private void testApi() {
        WallaApi api = new WallaApi(this);
        api.getMinVersion(new WallaApi.OnDataReceived() {
            @Override
            public void onDataReceived(Object data, int call) {
                Log.d("apidata", (String) data);
            }
        });


        api.getAllowedDomains(new WallaApi.OnDataReceived() {
            @Override
            public void onDataReceived(Object data, int call) {
                Log.d("apidata", ((List<DomainInfo>) data).toString());
            }
        });

        api.getActivities(new WallaApi.OnDataReceived() {
            @Override
            public void onDataReceived(Object data, int call) {
                Log.d("apidata", ((List<EventInfo>) data).toString());
            }
        }, 224);

        api.getUserInfo(new WallaApi.OnDataReceived() {
            @Override
            public void onDataReceived(Object data, int call) {
                Log.d("apidata", ((UserInfo) data).toString());
            }
        }, "4jAEwvyIAdMNJxS5LEG4ynC9SaX2");

        api.isAttendingEvent(new WallaApi.OnDataReceived() {
            @Override
            public void onDataReceived(Object data, int call) {
                Log.d("apidata", "" + (boolean) data);
            }
        }, "108XpHhxDyWkhbCHWvRK7zPqH8f1", "-KVYOoA3hxvZxHcmVF84");


        api.getAttendees(new WallaApi.OnDataReceived() {
            @Override
            public void onDataReceived(Object data, int call) {
                Log.d("apidata", ((List<UserInfo>) data).toString());
            }
        }, "-KVYMmeugVkQLtLuuAsq");

        api.verifyEmail("ZY59phLqRcNLPuEnTFDY0aym6MJ3", "mafuvadzeanesu@gmail.com");

        api.reportPost("-KVYOoA3hxvZxHcmVF84", "ZY59phLqRcNLPuEnTFDY0aym6MJ3");

        api.isVerified(new WallaApi.OnDataReceived() {
            @Override
            public void onDataReceived(Object data, int call) {
                Log.d("apidata", "verified: " + (boolean) data);
            }
        }, "ZY59phLqRcNLPuEnTFDY0aym6MJ3");
    }

    private void initShortcuts() {
        /*
        try {
            ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
            Intent shortcutIntent = new Intent(this, Create.class);
            shortcutIntent.setAction(Intent.ACTION_VIEW);


            ShortcutInfo shortcut = new ShortcutInfo.Builder(this, "compose")
                    .setShortLabel("Compose")
                    .setLongLabel("Create event")
                    .setIcon(Icon.createWithResource(this, R.mipmap.circle_add))
                    .setIntent(shortcutIntent)
                    .build();

            shortcutManager.setDynamicShortcuts(Arrays.asList(shortcut));
        }catch (Exception e){

        }
        */
    }

    private void initUi() {
        fonts = new Fonts(this);
        api = new WallaApi(this);

        if(auth == null || auth.getCurrentUser() == null){
            startActivity(new Intent(this, LoginScreen.class));
            finish();
        }else {

            api.getUserInfo(new WallaApi.OnDataReceived() {
                @Override
                public void onDataReceived(Object data, int call) {
                    user = (UserInfo) data;
                    name.setText(String.format("%s %s", user.getFirst_name(), user.getLast_name()));

                    if (user.getProfile_url() != null && !user.getProfile_url().equals("")) {
                        showWelcomeMessage();
                        Picasso.with(MainContainer.this) //Context
                                .load(user.getProfile_url()) //URL/FILE
                                .into(profile_pic);//an ImageView Object to show the loaded image
                    }
                }
            }, auth.getCurrentUser().getUid());
        }

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navHeader = navigationView.getHeaderView(0);
        name = (TextView) navHeader.findViewById(R.id.name);
        name.setOnClickListener(this);
        profile_pic = (CircleImageView) navHeader.findViewById(R.id.profile_image);
        profile_pic.setOnClickListener(this);
        name.setTypeface(fonts.AzoSansRegular);

        tabIcons = new int[]{R.mipmap.ic_home, R.mipmap.ic_calendar, R.mipmap.ic_notifications,};
        tabIconsColored = new int[]{R.mipmap.ic_home_c, R.mipmap.ic_calendar_c, R.mipmap.ic_notifications_c,};
        tabNames = new String[]{"Activities", "Calendar", "Notifications"};

        getSupportActionBar().setTitle(tabNames[0]);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    public static void refresh(){
        api.getUserInfo(new WallaApi.OnDataReceived() {
            @Override
            public void onDataReceived(Object data, int call) {
                user = (UserInfo) data;
                name.setText(String.format("%s %s", user.getFirst_name(), user.getLast_name()));

                if (user.getProfile_url() != null && !user.getProfile_url().equals("")) {
                    Picasso.with(context) //Context
                            .load(user.getProfile_url()) //URL/FILE
                            .into(profile_pic);//an ImageView Object to show the loaded image
                }
            }
        }, auth.getCurrentUser().getUid());
    }

    private void showWelcomeMessage() {
        Snackbar snack = Snackbar.make(navigationView, String.format("Welcome %s %s", user.getFirst_name(), user.getLast_name()), Snackbar.LENGTH_LONG);
        View view = snack.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTypeface(fonts.AzoSansRegular);
        tv.setTextColor(getResources().getColor(R.color.colorPrimary));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        snack.show();

    }

    private void setupFab() {
        fab = (com.github.clans.fab.FloatingActionMenu) findViewById(R.id.fab);
        fab.setMenuButtonColorNormal(getResources().getColor(R.color.lightblue));
        fab.setMenuButtonColorPressed(getResources().getColor(R.color.lightblue));
        fab.setClosedOnTouchOutside(true);

        com.github.clans.fab.FloatingActionButton createGroup = new com.github.clans.fab.FloatingActionButton(this);
        createGroup.setLabelText("Create a group");
        createGroup.setImageResource(R.drawable.ic_group);

        com.github.clans.fab.FloatingActionButton createPost = new com.github.clans.fab.FloatingActionButton(this);
        createPost.setLabelText("Create an event");
        createPost.setImageResource(R.drawable.ic_create);

        createGroup.setColorNormal(getResources().getColor(R.color.lightblue));
        createPost.setColorNormal(getResources().getColor(R.color.lightblue));
        createGroup.setColorPressed(getResources().getColor(R.color.lightblue));
        createPost.setColorPressed(getResources().getColor(R.color.lightblue));

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
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switchTabIcons(position);
                changeActionBarTitle(position);
                changeTabMenuItems(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        viewPager.setAdapter(adapter);
    }

    private void changeTabMenuItems(int position) {
        switch (position) {
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
        switch (position) {
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
        switch (position) {
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
        switch (id) {
            case R.id.action_search:
                switchToSearchActivity();
                break;
            case R.id.action_filter:
                Home.showFilter();
                break;
            case R.id.name:
                startActivity(new Intent(this, EditProfile.class));
                break;
            case R.id.profile_image:
                startActivity(new Intent(this, EditProfile.class));
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

        switch (id) {
            case R.id.nav_my_interests:
                startActivity(new Intent(this, InterestsView.class));
                break;
            case R.id.nav_edit_profile:
                startActivity(new Intent(this, EditProfile.class));
                break;
            case R.id.nav_my_friends:
                startActivity(new Intent(this, Friends.class));
                break;
            case R.id.nav_my_groups:
                startActivity(new Intent(this, MyGroups.class));
                break;
            case R.id.nav_share:
                shareWalla();
                break;
            case R.id.nav_contact:
                contactWalla();
                break;
            case R.id.nav_logout:
                logout();
                break;
            default:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        auth.signOut();
        startActivity(new Intent(this, LoginScreen.class));
    }

    private void contactWalla() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:hollawalladuke@gmail.com"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Walla v" + getVersion() + " Android, Report a Problem");

        startActivity(Intent.createChooser(intent, "Send Email"));
    }

    private void shareWalla() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "Hey check out the Walla app at: https://play.google.com/store/apps/details?id=genieus.com.walla");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    private String getVersion() {
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
        String version = pInfo.versionName;
        return version;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

    }
}
