package genieus.com.walla.v1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import genieus.com.walla.R;

/**
 * Created by Anesu on 9/3/2016.
 */
public class SettingsAdapter extends ArrayAdapter<String> {
    String[] settings;
    int resource;

    public SettingsAdapter(Context context, int resource, String[] settings) {
        super(context, resource);
        this.settings = settings;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(resource, parent, false);
        }

        final TextView setting_name = (TextView) convertView.findViewById(R.id.setting_name);

        if(settings[position].equals("Logout")){
            setting_name.setTextColor(Color.RED);
            setting_name.setText("Logout");
        }else{
            setting_name.setText(settings[position]);
            setting_name.setTextColor(Color.parseColor("#6F6F6F"));
        }

        /*
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (setting_name.getText().toString()){
                    case "Share Walla!":
                        break;
                    case "Review Walla on Google Play":
                        Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                        i.setData(Uri.parse("https://play.google.com/store/apps/details?id=genieus.com.walla"));
                        getContext().startActivity(i);
                        break;
                    case "Visit the Walla Website":
                        String url = "http://www.wallasquad.com";
                        Intent in = new Intent(Intent.ACTION_VIEW);
                        in.setData(Uri.parse(url));
                        getContext().startActivity(in);
                        break;
                    case "Report a problem":
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/html");
                        intent.putExtra(Intent.EXTRA_EMAIL, "hollawalladuke@gmail.com");
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Report a problem");
                        getContext().startActivity(Intent.createChooser(intent, "Send Email"));
                        break;

                }
            }
        });
        */

        return convertView;
}

    @Override
    public int getCount() {
        return settings.length;
    }
}
