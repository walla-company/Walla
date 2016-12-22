package genieus.com.walla.v1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import genieus.com.walla.R;

/**
 * Created by Anesu on 10/1/2016.
 */
public class CalendarAdapter extends ArrayAdapter {
    private int res;
    private List<Event> events = new ArrayList<Event>();

    public CalendarAdapter(Context context, int resource, List<Event> events) {
        super(context, resource);
        res = resource;
        this.events = events;
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(res, parent, false);
        }

        LinearLayout ec = (LinearLayout) convertView.findViewById(R.id.event_container);
        TextView category = (TextView) convertView.findViewById(R.id.category);
        TextView time = (TextView) convertView.findViewById(R.id.event_time);
        TextView title = (TextView) convertView.findViewById(R.id.event_title);
        TextView date = (TextView) convertView.findViewById(R.id.event_date);
        TextView uid = (TextView) convertView.findViewById(R.id.uid);
        final TextView creator =  (TextView) convertView.findViewById(R.id.event_creator);
        RelativeLayout container = (RelativeLayout) convertView.findViewById(R.id.category_container);

        final Event event = events.get(position);

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
                if(event.isExpired()){
                    Toast.makeText(getContext(), "This event has expired", Toast.LENGTH_SHORT).show();
                }
                else {
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

            }
        });



        return convertView;
    }
}
