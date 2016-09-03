package genieus.com.walla;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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

        TextView setting_name = (TextView) convertView.findViewById(R.id.setting_name);

        if(settings[position].equals("Logout")){
            setting_name.setTextColor(Color.RED);
            setting_name.setText("Logout");
        }else{
            setting_name.setText(settings[position]);
            setting_name.setTextColor(Color.parseColor("#6F6F6F"));
        }

        return convertView;
}

    @Override
    public int getCount() {
        return settings.length;
    }
}
