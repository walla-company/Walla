package genieus.com.walla.v2;

import android.content.Context;
import android.graphics.Typeface;
import android.media.Image;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

import genieus.com.walla.R;

/**
 * Created by Anesu on 12/17/2016.
 */
public class EventsLVAdapter extends ArrayAdapter {
    enum Type{
        GROUP,
        MAIN_VIEW
    }

    int resource;
    List<Event> events;
    Type type;
    public EventsLVAdapter(Context context, int resource, List<Event> events) {
        super(context, resource);
        this.resource = resource;
        this.events = events;
        this.type = type;
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
        }

        Typeface robotoMedium = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Medium.ttf");
        Typeface robotoBold = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Bold.ttf");

        ImageView categoryIcon = (ImageView) convertView.findViewById(R.id.icon);
        ImageView visibility = (ImageView) convertView.findViewById(R.id.visibility);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView interested = (TextView) convertView.findViewById(R.id.interested_tv);
        TextView going = (TextView) convertView.findViewById(R.id.going_tv);
        TextView duration = (TextView) convertView.findViewById(R.id.duration);
        RecyclerView tabs = (RecyclerView) convertView.findViewById(R.id.tabs_rv);

        Event event = events.get(position);

        LinearLayoutManager horizontal
            = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        tabs.setLayoutManager(horizontal);

        TabRVAdapter tabAdapter = new TabRVAdapter(getContext(), event.getTabs());
        tabs.setAdapter(tabAdapter);


        visibility.setImageResource(event.getType() == Event.Type.CHILL ? R.drawable.ic_chill_gray : R.drawable.ic_lit_gray);
        title.setText(event.getTitle());
        title.setTypeface(robotoBold);
        //interested.setText(event.getInterested());
        //going.setText(event.getGoing());
        duration.setText(event.getStartTime() + "\nto " + event.getEndTime());
        duration.setTypeface(robotoMedium);

        return convertView;
    }
}
