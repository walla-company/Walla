package genieus.com.walla.v2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import genieus.com.walla.R;

/**
 * Created by anesu on 12/20/16.
 */

public class FriendsLVAdapter extends ArrayAdapter<Friend> {

    private List<Friend> data;
    private int resource;

    public FriendsLVAdapter(Context context, int resource, List<Friend> data) {
        super(context, resource);
        this.data = data;
        this.resource = resource;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
        }

        Friend friend = data.get(position);

        ImageView image = (ImageView) convertView.findViewById(R.id.profile_picture);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView details = (TextView) convertView.findViewById(R.id.details);

        name.setText(friend.getName());
        details.setText(friend.getYear() + " · " + friend.getMajor());

        return convertView;
    }
}
