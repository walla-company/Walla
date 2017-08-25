package genieus.com.walla.v2.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import genieus.com.walla.v2.adapters.NavigationTabsAdapter;
import genieus.com.walla.v2.api.WallaApi;
import genieus.com.walla.v2.fragments.Calendar;
import genieus.com.walla.v2.fragments.Home;
import genieus.com.walla.v2.fragments.Notifications;
import genieus.com.walla.v2.fragments.UserProfile;
import genieus.com.walla.v2.utils.Fonts;
import genieus.com.walla.v2.datatypes.MyFirebaseInstanceidService;
import genieus.com.walla.v2.datatypes.MyFirebaseMessagingService;
import genieus.com.walla.v2.datatypes.User;
import genieus.com.walla.v2.utils.ImageUtils;

public class MainContainer extends AppCompatActivity
        implements Home.OnFragmentInteractionListener,
        Calendar.OnFragmentInteractionListener,
        Notifications.OnFragmentInteractionListener {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private com.github.clans.fab.FloatingActionButton fab;
    private static CircleImageView profile_pic;
    private static TextView name;
    private static User user;

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
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

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
            e.printStackTrace();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    private boolean isLoggedIn() {
        return auth != null && auth.getCurrentUser() != null;
    }

    private void initUi() {
        fonts = new Fonts(this);
        if (auth == null || auth.getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        api.isUserSuspended(new WallaApi.OnDataReceived() {
            @Override
            public void onDataReceived(Object data, int call) {
                boolean isSuspended = (boolean) data;
                if (isSuspended) {
                    notifyUserOfSuspension();
                }

            }
        }, auth.getCurrentUser().getUid());

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
                user = (User) data;
                name.setText(String.format("%s %s", user.getFirstName(), user.getLastName()));

                if (user.getProfileUrl() != null && !user.getProfileUrl().equals("")) {
                    Picasso.with(context) //Context
                            .load(user.getProfileUrl()) //URL/FILE
                            .into(profile_pic);//an ImageView Object to show the loaded image
                }
            }
        }, auth.getCurrentUser().getUid());
    }

    private void notifyUserOfSuspension() {
        Intent intent = new Intent(this, AccountSuspensionAlert.class);
        startActivity(intent);
        finish();
    }

    private void showWelcomeMessage() {
        Snackbar snack = Snackbar.make(findViewById(R.id.coordinator_layout),
                String.format("Welcome back %s", user.getFirstName()), Snackbar.LENGTH_LONG);
        final View view = snack.getView();
        final TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
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
        fab.setColorNormal(getResources().getColor(R.color.lightBlue));
        fab.setColorPressed(getResources().getColor(R.color.lightBlue));
        fab.setImageResource(R.drawable.ic_create);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainContainer.this, CreateActivity.class));
            }
        });

        fab.hide(false);
    }

    private void setupViewPager(ViewPager viewPager) {
        NavigationTabsAdapter adapter = new NavigationTabsAdapter(getSupportFragmentManager());
        adapter.addFragment(Home.newInstance("home", ""), "Home");
        adapter.addFragment(Notifications.newInstance("notifications", ""), "Notifications");
        adapter.addFragment(UserProfile.newInstance(), "Profile");


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switchTabIcons(position);
                changeActionBarTitle(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        viewPager.setAdapter(adapter);
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.action_settings)
                .getIcon()
                .setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
               startActivity(new Intent(this, Settings.class));
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
