package genieus.com.walla;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Anesu on 9/10/2016.
 */
public class MyInterestsAdapter extends ArrayAdapter<String> {
    String[] interests;
    int res;
    public MyInterestsAdapter(Context context, int resource, String[] interests) {
        super(context, resource);
        this.interests = interests;
        res = resource;
    }

    @Override
    public int getCount() {
        return interests.length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(res, parent, false);
        }

        TextView tv = (TextView) convertView.findViewById(R.id.interest_name);
        tv.setText(interests[position]);

        return convertView;
    }
}
