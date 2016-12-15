package genieus.com.walla.v2;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import genieus.com.walla.R;

/**
 * Created by Anesu on 12/13/2016.
 */
public class InterestsAdapter extends BaseAdapter {
    List<InterestInfo> list;
    Context context;
    public InterestsAdapter(List<InterestInfo> list, Context context){
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.single_interest, null);
        }

        InterestInfo data = list.get(position);

        TextView title = (TextView) convertView.findViewById(R.id.interest_title);
        ImageView icon  = (ImageView) convertView.findViewById(R.id.interest_icon);
        RelativeLayout container = (RelativeLayout) convertView.findViewById(R.id.interests_container);

        title.setText(data.getName());
        icon.setImageResource(data.getImg());

        if(data.isSelected()){
            title.setTextColor(Color.WHITE);
            container.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        }

        return convertView;
    }
}
