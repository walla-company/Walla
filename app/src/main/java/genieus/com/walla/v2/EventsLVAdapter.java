package genieus.com.walla.v2;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import genieus.com.walla.R;

/**
 * Created by Anesu on 12/17/2016.
 */
public class EventsLVAdapter extends ArrayAdapter {
    int resource;
    List<Event> events;
    public EventsLVAdapter(Context context, int resource, List<Event> events) {
        super(context, resource);
        this.resource = resource;
        this.events = events;
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

        ImageView categoryIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView categoryTime = (TextView) convertView.findViewById(R.id.category_time);
        ImageView visibility = (ImageView) convertView.findViewById(R.id.visibility);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView interested = (TextView) convertView.findViewById(R.id.interested_tv);
        TextView going = (TextView) convertView.findViewById(R.id.going_tv);
        TextView time = (TextView) convertView.findViewById(R.id.time);

        Event event = events.get(position);


        categoryTime.setText(event.getCategory() + " · In " + event.getHoursDiff() + " hours");
        visibility.setImageResource(event.getType() == Event.Type.CHILL ? R.drawable.ic_chill : R.drawable.ic_lit);
        title.setText(event.getTitle());
        //interested.setText(event.getInterested());
        //going.setText(event.getGoing());
        time.setText(event.getStartTime() + " - " + event.getEndTime());

        return convertView;
    }
}
