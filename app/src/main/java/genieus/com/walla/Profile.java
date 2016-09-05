package genieus.com.walla;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class Profile extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener{

    ListView lv;
    RelativeLayout activities;
    final String[] settings = new String[]{"My Calendar", "Account Settings", "Logout"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initUi();

    }

    private void initUi(){
        lv = (ListView) findViewById(R.id.profile_options);
        SettingsAdapter adapter = new SettingsAdapter(getBaseContext(), R.layout.settings_template, settings);
        lv.setAdapter(adapter);

        activities = (RelativeLayout) findViewById(R.id.activities_btn);
        activities.setOnClickListener(this);

        lv.setOnItemClickListener(this);
    }

    private void logout(){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.activities_btn){
            Intent intent = new Intent(this, Activities.class);
            startActivity(intent);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch(settings[position]){
            case "Logout":
                logout();
        }
    }
}
