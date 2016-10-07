package genieus.com.walla;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Anesu on 9/3/2016.
 */
public class EventAdapter extends ArrayAdapter<String> implements Filterable {

    private DatabaseReference mDatabase;
    List<Event> events;
    List<String> filtered;
    int resource;
    Filter mFilter;

    public EventAdapter(Context context, int resource, List<Event> events) {
        super(context, resource);
        this.events = events;
        this.resource = resource;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        filtered = new ArrayList<>();
        mFilter = new ItemFilter();
    }

    public Filter getFilter() {
        return mFilter;
    }

    @Override
    public int getCount() {
        return filtered.size();
    }

    private Event getEventFromCategory(List<Event> events, String str){
        for(Event ev : events){
            if((ev.getEventCateogory() + ev.getTimePosted()).equals(str)){
                return ev;
            }
        }

        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
        }

        LinearLayout ec = (LinearLayout) convertView.findViewById(R.id.event_container);
        TextView category = (TextView) convertView.findViewById(R.id.category);
        TextView time = (TextView) convertView.findViewById(R.id.event_time);
        TextView title = (TextView) convertView.findViewById(R.id.event_title);
        TextView date = (TextView) convertView.findViewById(R.id.event_date);
        TextView uid = (TextView) convertView.findViewById(R.id.uid);
        final TextView creator =  (TextView) convertView.findViewById(R.id.event_creator);
        RelativeLayout container = (RelativeLayout) convertView.findViewById(R.id.category_container);

        final Event event = getEventFromCategory(events, filtered.get(position));

        if(!event.isExpired()) {
            Drawable background = container.getBackground();
            if (background instanceof ShapeDrawable) {
                ((ShapeDrawable) background).getPaint().setColor(Color.parseColor(event.getColor()));
            } else if (background instanceof GradientDrawable) {
                ((GradientDrawable) background).setColor(Color.parseColor(event.getColor()));
            } else if (background instanceof ColorDrawable) {
                ((ColorDrawable) background).setColor(Color.parseColor(event.getColor()));
            }
        }else{
            Drawable bg = container.getBackground();
            if (bg instanceof ShapeDrawable) {
                ((ShapeDrawable)bg).getPaint().setColor(getContext().getResources().getColor(R.color.LightGrey));
            } else if (bg instanceof GradientDrawable) {
                ((GradientDrawable)bg).setColor(getContext().getResources().getColor(R.color.LightGrey));
            } else if (bg instanceof ColorDrawable) {
                ((ColorDrawable)bg).setColor(getContext().getResources().getColor(R.color.LightGrey));
            }
        }

        category.setText(event.getEventCateogory());
        date.setText(event.getDatePosted());
        time.setText(event.getTimePosted());
        title.setText(event.getEventTitle());
        creator.setVisibility(View.GONE);
        creator.setText(event.getPosterUid());

        creator.setVisibility(View.VISIBLE);
        creator.setText(event.getPostedBy() +  " invites you");

            ec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        Intent intent = new Intent(getContext(), ActivityDetails.class);
                        intent.putExtra("description", event.getEventTitle());
                        intent.putExtra("time", event.getTimePosted());
                        intent.putExtra("location", event.getLocation());
                        intent.putExtra("people", event.getPeople());
                        intent.putExtra("category", event.getEventCateogory());
                        intent.putExtra("color", event.getColor());
                        intent.putExtra("key", event.getKey());
                        intent.putExtra("poster", event.getPostedBy());
                        intent.putExtra("uid", event.getPosterUid());
                        intent.putExtra("expired", event.isExpired());

                        getContext().startActivity(intent);


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
            final ArrayList<String> nlist = new ArrayList<String>(count);

            String filterableString;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i).getEventCateogory() + list.get(i).getTimePosted();
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
