package genieus.com.walla.v2.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;


import de.hdodenhof.circleimageview.CircleImageView;
import genieus.com.walla.R;
import genieus.com.walla.v1.OutdatedVersion;
import genieus.com.walla.v2.api.WallaApi;
import genieus.com.walla.v2.fragment.Notifications;
import genieus.com.walla.v2.adapter.viewpager.ViewPagerAdapter;
import genieus.com.walla.v2.fragment.Calendar;
import genieus.com.walla.v2.fragment.Home;
import genieus.com.walla.v2.fragment.UserProfile;
import genieus.com.walla.v2.info.Fonts;
import genieus.com.walla.v2.info.MyFirebaseInstanceidService;
import genieus.com.walla.v2.info.MyFirebaseMessagingService;
import genieus.com.walla.v2.info.UserInfo;

public class MainContainer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Home.OnFragmentInteractionListener, Calendar.OnFragmentInteractionListener, Notifications.OnFragmentInteractionListener, UserProfile.OnFragmentInteractionListener, View.OnClickListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private com.github.clans.fab.FloatingActionButton fab;
    private static CircleImageView profile_pic;
    private static TextView name;
    private NavigationView navigationView;
    private View navHeader;
    private static UserInfo user;

    public static MenuItem filter_icon;
    private Fonts fonts;
    private static WallaApi api;
    private static FirebaseAuth auth;
    private static Context context;

    private int[] tabIcons, tabIconsColored;
    private String[] tabNames;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_container);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupFab();

        auth = FirebaseAuth.getInstance();
        if (!isLoggedIn()) {
            startActivity(new Intent(this, LoginScreenEmail.class));
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
        api = WallaApi.getInstance(this);

        Intent intent = new Intent(this, MyFirebaseInstanceidService.class);
        Intent intent2 = new Intent(this, MyFirebaseMessagingService.class);
        startService(intent);
        startService(intent2);

        if (auth.getCurrentUser() != null) {
            String token = FirebaseInstanceId.getInstance().getToken();
            api.registerToken(auth.getCurrentUser().getUid(), token);
        }

        try {
            initUi();
        }catch(Exception e){
            Log.e("error handled", e.toString());
            startActivity(new Intent(this, LoginScreenEmail.class));
        }
    }

    private boolean isLoggedIn() {
        return auth != null && auth.getCurrentUser() != null;
    }

    private void initUi() {
        fonts = new Fonts(this);
        if (auth == null || auth.getCurrentUser() == null) {
            startActivity(new Intent(this, LoginScreenEmail.class));
            finish();
        } else {

            api.getUserInfo(new WallaApi.OnDataReceived() {
                @Override
                public void onDataReceived(Object data, int call) {
                    user = (UserInfo) data;
                    name.setText(String.format("%s %s", user.getFirst_name(), user.getLast_name()));

                    if(!user.isIntro_complete()){
                        startActivity(new Intent(MainContainer.this, Onboarding.class));
                    }

                    if (user.getProfile_url() != null && !user.getProfile_url().equals("")) {
                        showWelcomeMessage();
                        Picasso.with(MainContainer.this) //Context
                                .load(user.getProfile_url()) //URL/FILE
                                .into(profile_pic);//an ImageView Object to show the loaded image
                    }

                }
            }, auth.getCurrentUser().getUid());
        }

        //check to see if the user is suspended
        api.isUserSuspended(new WallaApi.OnDataReceived() {
            @Override
            public void onDataReceived(Object data, int call) {
                boolean isSuspended = (boolean) data;
                if (isSuspended) {
                    notifyUserOfSuspension();
                }

            }
        }, auth.getCurrentUser().getUid());

        minVersionListener();


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navHeader = navigationView.getHeaderView(0);
        name = (TextView) navHeader.findViewById(R.id.name);
        name.setOnClickListener(this);
        profile_pic = (CircleImageView) navHeader.findViewById(R.id.profile_image);
        profile_pic.setOnClickListener(this);
        name.setTypeface(fonts.AzoSansRegular);

        tabIcons = new int[]{R.mipmap.ic_home, R.mipmap.ic_notifications, R.mipmap.ic_profile,};
        tabIconsColored = new int[]{R.mipmap.ic_home_c, R.mipmap.ic_notifications_c, R.mipmap.ic_profile_c,};
        tabNames = new String[]{"Activities", "Notifications", "My Profile"};

        getSupportActionBar().setTitle(tabNames[0]);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    public static void refresh() {
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

    private void notifyUserOfSuspension() {
        Intent intent = new Intent(this, AccountSuspension.class);
        startActivity(intent);
        finish();
    }

    private void showWelcomeMessage() {
        Snackbar snack = Snackbar.make(navigationView, String.format("Welcome %s", user.getFirst_name()), Snackbar.LENGTH_LONG);
        View view = snack.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTypeface(fonts.AzoSansRegular);
        tv.setTextColor(getResources().getColor(R.color.colorPrimary));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

        snack.setCallback(new Snackbar.Callback() {

            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                   fab.show(true);
            }

        });

        snack.show();

    }

    private void setupFab() {
        fab = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_add);
        fab.setColorNormal(getResources().getColor(R.color.lightblue));
        fab.setColorPressed(getResources().getColor(R.color.lightblue));
        fab.setImageResource(R.drawable.ic_create);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainContainer.this, Create.class));
            }
        });

        fab.hide(false);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(Home.newInstance("home", ""), "Home");
        adapter.addFragment(Notifications.newInstance("notifications", ""), "Notifications");
        adapter.addFragment(UserProfile.newInstance("profile", ""), "Profile");


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

    private void minVersionListener() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("app_settings/min_version/android").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String minVersion = (String) dataSnapshot.getValue();
                        if (getVersion().compareTo(minVersion) < 0) {
                            startActivity(new Intent(MainContainer.this, OutdatedVersion.class));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("Cancelled", "getUser:onCancelled", databaseError.toException());
                        // ...
                    }
                });
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
                fab.show(true);
                tabLayout.getTabAt(0).setIcon(tabIconsColored[0]);
                tabLayout.getTabAt(1).setIcon(tabIcons[1]);
                tabLayout.getTabAt(2).setIcon(tabIcons[2]);
                break;
            case 1:
                fab.hide(true);
                tabLayout.getTabAt(0).setIcon(tabIcons[0]);
                tabLayout.getTabAt(1).setIcon(tabIconsColored[1]);
                tabLayout.getTabAt(2).setIcon(tabIcons[2]);
                break;
            case 2:
                fab.hide(true);
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
                break;
            case R.id.name:
                startActivity(new Intent(this, EditProfile.class));
                break;
            case R.id.profile_image:
                startActivity(new Intent(this, EditProfile.class));
                break;
            case R.id.all:
                Home.showFilter("");
                break;
            case R.id.today:
                Home.showFilter("Today");
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
            case R.id.nav_share:
                shareWalla();
                break;
            case R.id.nav_contact:
                contactWalla();
                break;
            case R.id.nav_logout:
                logout();
                break;
            case R.id.nav_terms:
                showTerms();
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
        startActivity(new Intent(this, LoginScreenEmail.class));
    }

    private void contactWalla() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:hollawalladuke@gmail.com"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Walla v" + getVersion() + " Android, Report a Problem");

        startActivity(Intent.createChooser(intent, "Send Email"));
    }

    private void showTerms() {
        String url = "https://www.wallasquad.com/terms-and-conditions/";
        Intent in = new Intent(Intent.ACTION_VIEW);
        in.setData(Uri.parse(url));
        startActivity(in);
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
