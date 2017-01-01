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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import genieus.com.walla.R;
import genieus.com.walla.v2.info.Fonts;
import genieus.com.walla.v2.info.GroupInfo;

/**
 * Created by anesu on 12/23/16.
 */

public class MyGroupsLVAdapter extends ArrayAdapter<GroupInfo> {
    public interface OnGroupStateChangeListener{
        public boolean onGroupStateChange(int pos, boolean checked);
    }

    private int resource;
    private List<GroupInfo> data;
    private Fonts fonts;
    private OnGroupStateChangeListener listener;

    public MyGroupsLVAdapter(Context context, OnGroupStateChangeListener listener, int resource, List<GroupInfo> data) {
        super(context, resource);
        this.resource = resource;
        this.data = data;
        this.listener = listener;

        fonts = new Fonts(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
        }

        final GroupInfo info = data.get(position);

        final CheckBox check = (CheckBox) convertView.findViewById(R.id.check);
        if(check != null){
            check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(!listener.onGroupStateChange(position, isChecked))
                        check.setChecked(false);
                }
            });
        }

        RelativeLayout container = (RelativeLayout) convertView.findViewById(R.id.group_icon_container);
         changeBackgroundColor(container, info.getColor());
        TextView title = (TextView) convertView.findViewById(R.id.title);
        title.setTypeface(fonts.AzoSansRegular);
        title.setText(info.getName());
        TextView memberInfo = (TextView) convertView.findViewById(R.id.members_info);
        memberInfo.setTypeface(fonts.AzoSansRegular);
        TextView abbr = (TextView) convertView.findViewById(R.id.abbr);
        abbr.setTypeface(fonts.AzoSansRegular);
        abbr.setText(info.getAbbr());

        return convertView;
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
