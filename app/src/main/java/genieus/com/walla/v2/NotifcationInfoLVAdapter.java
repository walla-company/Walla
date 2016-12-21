package genieus.com.walla.v2;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import genieus.com.walla.R;

/**
 * Created by anesu on 12/20/16.
 */

public class NotifcationInfoLVAdapter extends ArrayAdapter<InfoNotification> {
    private List<InfoNotification> data;
    private int resource;
    private Typeface robotoMedium;
    public NotifcationInfoLVAdapter(Context context, int resource, List<InfoNotification> data) {
        super(context, resource);
        this.data = data;
        this.resource = resource;

        robotoMedium = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Medium.ttf");
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
        }

        InfoNotification notification = data.get(position);

        ImageView typeIcon = (ImageView) convertView.findViewById(R.id.type_icon);
        TextView info = (TextView) convertView.findViewById(R.id.info);

        info.setText(notification.getMessage());
        info.setTypeface(robotoMedium);

        return convertView;
    }

    @Override
    public int getCount() {
        return data.size();
    }
}
