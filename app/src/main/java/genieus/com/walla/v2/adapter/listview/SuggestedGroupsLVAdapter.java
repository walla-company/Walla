package genieus.com.walla.v2.adapter.listview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import genieus.com.walla.R;
import genieus.com.walla.v2.info.Fonts;
import genieus.com.walla.v2.info.GroupInfo;

/**
 * Created by anesu on 12/21/16.
 */

public class SuggestedGroupsLVAdapter extends ArrayAdapter<GroupInfo> {
    private List<GroupInfo> data;
    private int resource;
    private Fonts fonts;

    public SuggestedGroupsLVAdapter(Context context, int resource, List<GroupInfo> data) {
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

        GroupInfo info = data.get(position);

        RelativeLayout container = (RelativeLayout) convertView.findViewById(R.id.group_icon_container);
        TextView abbr = (TextView) convertView.findViewById(R.id.group_abbr);
        TextView groupName = (TextView) convertView.findViewById(R.id.group_name);
        TextView members = (TextView) convertView.findViewById(R.id.members_info);

        groupName.setTypeface(fonts.AzoSansMedium);
        members.setTypeface(fonts.AzoSansRegular);
        abbr.setTypeface(fonts.AzoSansRegular);

        abbr.setText(info.getAbbr());
        groupName.setText(info.getName());
        changeBackgroundColor(container, info.getColor());

        return convertView;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    private void changeBackgroundColor(View view, String color){
        Drawable background = view.getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable)background).getPaint().setColor(Color.parseColor(color));
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable)background).setColor(Color.parseColor(color));
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable)background).setColor(Color.parseColor(color));
        }
    }
}