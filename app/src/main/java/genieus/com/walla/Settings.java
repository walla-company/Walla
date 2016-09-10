package genieus.com.walla;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Settings extends AppCompatActivity {

    final String[] settings = new String[]{"Share Walla!",
        "Review Walla on Google Play",
        "Visit the Walla Website",
        "Report a problem"};

    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       initUi();

    }

    private void initUi(){
        lv = (ListView) findViewById(R.id.settings);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, settings);
        lv.setAdapter(adapter);
    }

}
