package genieus.com.walla.v1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import genieus.com.walla.R;

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

       initUi();

    }

    private void initUi(){
        lv = (ListView) findViewById(R.id.settings);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, settings);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0: //share walla
                        shareWalla();
                        break;
                    case 1: //review walla
                        review();
                        break;
                    case 2: //visit website
                        visitSite();
                        break;
                    case 3: //report problem
                        sendEmail();
                        break;
                }
            }
        });
    }

    private void sendEmail(){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:hollawalladuke@gmail.com"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Walla Android, Report a Problem");

        startActivity(Intent.createChooser(intent, "Send Email"));
    }

    private void visitSite(){
        String url = "http://www.wallasquad.com";
        Intent in = new Intent(Intent.ACTION_VIEW);
        in.setData(Uri.parse(url));
        startActivity(in);
    }

    private void shareWalla(){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "Hey check out the Walla app at: https://play.google.com/store/apps/details?id=genieus.com.walla");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    private void review(){
        Intent i = new Intent(android.content.Intent.ACTION_VIEW);
        i.setData(Uri.parse("https://play.google.com/store/apps/details?id=genieus.com.walla"));
        startActivity(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}