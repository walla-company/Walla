package genieus.com.walla.v2.adapter.listview;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import genieus.com.walla.R;
import genieus.com.walla.v2.activity.Details;
import genieus.com.walla.v2.activity.Group;
import genieus.com.walla.v2.adapter.recyclerview.GroupTabRVAdapter;
import genieus.com.walla.v2.api.WallaApi;
import genieus.com.walla.v2.info.EventInfo;
import genieus.com.walla.v2.adapter.recyclerview.TabRVAdapter;
import genieus.com.walla.v2.info.Fonts;
import genieus.com.walla.v2.info.GroupInfo;

/**
 * Created by Anesu on 12/17/2016.
 */
public class EventsLVAdapter extends ArrayAdapter implements Filterable{
    private int resource;
    private List<EventInfo> events;
    private List<String> filtered;
    private Filter filter;
    private Fonts fonts;
    private WallaApi api;


    public EventsLVAdapter(Context context, int resource, List<EventInfo> events) {
        super(context, resource);
        this.resource = resource;
        this.events = events;

        filtered = new ArrayList<>();
        filter = new ItemFilter();
        fonts = new Fonts(context);
        api = new WallaApi(context);
    }

    private EventInfo getEvent(List<EventInfo> events, String query){
        for(EventInfo event : events){
            if((event.getInterests().toString() + event.getTitle() + event.getStart_time()).equals(query)){
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
        View view = convertView;

        LayoutInflater inflater = LayoutInflater.from(getContext());
        view = inflater.inflate(resource, null);

        ImageView visibility = (ImageView) view.findViewById(R.id.visibility);
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView interested = (TextView) view.findViewById(R.id.interested);
        TextView going = (TextView) view.findViewById(R.id.going);
        TextView duration = (TextView) view.findViewById(R.id.duration);
        TextView date = (TextView) view.findViewById(R.id.date);
        TextView attendees_description = (TextView) view.findViewById(R.id.attendees_description);
        RecyclerView tabs = (RecyclerView) view.findViewById(R.id.tabs_rv);
        final RecyclerView groupsTabs = (RecyclerView) view.findViewById(R.id.groups_rv);

        final EventInfo event = getEvent(events, filtered.get(position));

        LinearLayoutManager horizontal
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        LinearLayoutManager horizontal2
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        groupsTabs.setLayoutManager(horizontal2);
        tabs.setLayoutManager(horizontal);

        if(event.getGroup_name() != null && !event.getGroup_name().isEmpty()){
            GroupInfo groupInfo = new GroupInfo(event.getGroup_name(), event.getGroup_abbr(), "");
            groupInfo.setGuid(event.getHost_group());
            GroupTabRVAdapter groupTabAdapter = new GroupTabRVAdapter(getContext(), new ArrayList<GroupInfo>(Arrays.asList(groupInfo)));
            groupsTabs.setAdapter(groupTabAdapter);
        }

        TabRVAdapter tabAdapter = new TabRVAdapter(getContext(), event.getInterests());
        tabs.setAdapter(tabAdapter);

        visibility.setImageResource(event.is_public() ? R.drawable.ic_lit_gray : R.drawable.ic_chill_gray);
        title.setText(event.getTitle());
        title.setTypeface(fonts.AzoSansRegular);
        interested.setText(event.getInterested_list().size() + "");
        interested.setTypeface(fonts.AzoSansRegular);
        going.setText(event.getGoing_list().size() + "");
        going.setTypeface(fonts.AzoSansRegular);
        date.setTypeface(fonts.AzoSansRegular);
        date.setText(event.getStringDate(event.getStart_time()));
        duration.setText(event.getStringTime(event.getStart_time(), true)+ "\nto " + event.getStringTime(event.getEnd_time(), false));
        duration.setTypeface(fonts.AzoSansRegular);
        attendees_description.setTypeface(fonts.AzoSansRegular);
        attendees_description.setText("");

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Details.class);
                intent.putExtra("auid", event.getAuid());
                getContext().startActivity(intent);
            }
        });

        return view;
    }

    private class ItemFilter extends Filter {
        @Override
        protected Filter.FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<EventInfo> list = events;

            int count = list.size();
            final ArrayList<String> nlist = new ArrayList<>(count);

            String filterableString;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i).getInterests().toString() + list.get(i).getTitle() + list.get(i).getStart_time();
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
