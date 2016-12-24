package genieus.com.walla.v2.adapter.listview;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import genieus.com.walla.R;
import genieus.com.walla.v2.info.Fonts;
import genieus.com.walla.v2.info.NotificationInfo;

/**
 * Created by anesu on 12/20/16.
 */

public class NotificationInfoLVAdapter extends ArrayAdapter<NotificationInfo> {
    private List<NotificationInfo> data;
    private int resource;
    private Fonts fonts;
    public NotificationInfoLVAdapter(Context context, int resource, List<NotificationInfo> data) {
        super(context, resource);
        this.data = data;
        this.resource = resource;

        fonts = new Fonts(context);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
        }

        NotificationInfo notification = data.get(position);

        ImageView typeIcon = (ImageView) convertView.findViewById(R.id.type_icon);
        TextView info = (TextView) convertView.findViewById(R.id.info);

        info.setText(notification.getMessage());
        info.setTypeface(fonts.AzoSansRegular);

        return convertView;
    }

    @Override
    public int getCount() {
        return data.size();
    }
}
