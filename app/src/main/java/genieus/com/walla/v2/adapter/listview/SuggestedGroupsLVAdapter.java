package genieus.com.walla.v2.adapter.listview;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Filter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import genieus.com.walla.R;
import genieus.com.walla.v2.activity.Group;
import genieus.com.walla.v2.info.EventInfo;
import genieus.com.walla.v2.info.Fonts;
import genieus.com.walla.v2.info.GroupInfo;

/**
 * Created by anesu on 12/21/16.
 */

public class SuggestedGroupsLVAdapter extends ArrayAdapter<GroupInfo> {
    private List<GroupInfo> data;
    private int resource;
    private Fonts fonts;
    private List<String> filtered;
    private Filter filter;

    public SuggestedGroupsLVAdapter(Context context, int resource, List<GroupInfo> data) {
        super(context, resource);
        this.data = data;
        this.resource = resource;

        filtered = new ArrayList<>();
        filter = new SuggestedGroupsLVAdapter.ItemFilter();
        fonts = new Fonts(context);
    }

    public Filter getFilter() {
        return filter;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
        }

        final GroupInfo info = getGroup(data, filtered.get(position));

        RelativeLayout container = (RelativeLayout) convertView.findViewById(R.id.group_icon_container);
        RelativeLayout entire_container = (RelativeLayout) convertView.findViewById(R.id.entire_container);
        entire_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Group.class);
                intent.putExtra("guid", info.getGuid());
                getContext().startActivity(intent);
            }
        });
        TextView abbr = (TextView) convertView.findViewById(R.id.group_abbr);
        TextView groupName = (TextView) convertView.findViewById(R.id.group_name);
        TextView details = (TextView) convertView.findViewById(R.id.details);
        details.setTypeface(fonts.AzoSansRegular);
        details.setText(info.getDescription());

        groupName.setTypeface(fonts.AzoSansMedium);
        abbr.setTypeface(fonts.AzoSansRegular);

        abbr.setText(info.getAbbr());
        groupName.setText(info.getName());
        changeBackgroundColor(container, info.getColor());

        return convertView;
    }

    private GroupInfo getGroup(List<GroupInfo> list, String query){
        for(GroupInfo group : list){
            if((group.getAbbr() + group.getName()).equals(query)){
                return group;
            }
        }

        return null;
    }

    @Override
    public int getCount() {
        return filtered.size();
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

    private class ItemFilter extends Filter {
        @Override
        protected Filter.FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<GroupInfo> list = data;

            int count = list.size();
            final ArrayList<String> nlist = new ArrayList<>(count);

            String filterableString;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i).getAbbr() + list.get(i).getName();
                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(filterableString);
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filtered = (ArrayList<String>) results.values;
            notifyDataSetChanged();
        }
    }
}
