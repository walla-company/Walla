package genieus.com.walla.v2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * Created by Anesu on 12/17/2016.
 */
public class EventsLVAdapter extends ArrayAdapter {
    int resource;
    public EventsLVAdapter(Context context, int resource) {
        super(context, resource);
        this.resource = resource;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
        }

        return convertView;
    }
}
