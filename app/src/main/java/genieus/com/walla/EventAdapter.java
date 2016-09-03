package genieus.com.walla;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Anesu on 9/3/2016.
 */
public class EventAdapter extends ArrayAdapter<String> {

    List<Event> events;
    int resource;

    public EventAdapter(Context context, int resource, List<Event> events) {
        super(context, resource);
        this.events = events;
        this.resource = resource;
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
        }

        TextView time = (TextView) convertView.findViewById(R.id.event_time);
        TextView category = (TextView) convertView.findViewById(R.id.event_category);
        TextView title = (TextView) convertView.findViewById(R.id.event_title);
        TextView creator =  (TextView) convertView.findViewById(R.id.event_creator);

        Event event = events.get(position);

        time.setText(event.getTimePosted());
        category.setText(event.getEventCateogory());
        title.setText(event.getEventTitle());
        creator.setText("Posted by " + event.getPostedBy());

        return convertView;
    }
}
