package genieus.com.walla.v2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.Image;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import genieus.com.walla.R;

/**
 * Created by Anesu on 12/17/2016.
 */
public class EventsLVAdapter extends ArrayAdapter implements Filterable{
    int resource;
    List<Event> events;
    List<String> filtered;
    Filter filter;
    Typeface robotoMedium;
    Typeface robotoBold;

    public EventsLVAdapter(Context context, int resource, List<Event> events) {
        super(context, resource);
        this.resource = resource;
        this.events = events;

        filtered = new ArrayList<>();
        filter = new ItemFilter();

        robotoMedium = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Medium.ttf");
        robotoBold = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Bold.ttf");
    }

    private Event getEvent(List<Event> events, String query){
        for(Event event : events){
            if((event.getTabs().toString() + event.getTitle() + event.getStartTime()).equals(query)){
                return event;
            }
        }

        return null;
    }

    public Filter getFilter() {
        return filter;
    }

    @Override
    public int getCount() {
        return filtered.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
        }

        ImageView categoryIcon = (ImageView) convertView.findViewById(R.id.icon);
        ImageView visibility = (ImageView) convertView.findViewById(R.id.visibility);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView interested = (TextView) convertView.findViewById(R.id.interested_tv);
        TextView going = (TextView) convertView.findViewById(R.id.going_tv);
        TextView duration = (TextView) convertView.findViewById(R.id.duration);
        RecyclerView tabs = (RecyclerView) convertView.findViewById(R.id.tabs_rv);

        Event event = getEvent(events, filtered.get(position));

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

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(new Intent(getContext(), Details.class));
            }
        });

        return convertView;
    }

    private class ItemFilter extends Filter {
        @Override
        protected Filter.FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<Event> list = events;

            int count = list.size();
            final ArrayList<String> nlist = new ArrayList<>(count);

            String filterableString;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i).getTabs().toString() + list.get(i).getTitle() + list.get(i).getStartTime();
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
