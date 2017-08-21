package genieus.com.walla.v2.adapter.listview;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.base.Optional;

import java.util.List;

import genieus.com.walla.R;
import genieus.com.walla.v2.activity.Details;
import genieus.com.walla.v2.api.WallaApi;
import genieus.com.walla.v2.info.Event;
import genieus.com.walla.v2.info.Fonts;
import genieus.com.walla.v2.info.User;
import genieus.com.walla.v2.ui.StateImageView;
import genieus.com.walla.v2.utils.ImageUtils;

/**
 * Created by Anesu on 12/17/2016.
 */
public class EventsLVAdapter extends ArrayAdapter<Event> {
    private int mResource;
    private List<Event> mEvents;
    private Context mContext;


    public EventsLVAdapter(final Context context, final int resource, final List<Event> events) {
        super(context, resource);
        mResource = resource;
        mEvents = events;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mEvents.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View view = inflater.inflate(mResource, null);

        final Event event = getItem(position);

        final TextView title = (TextView) view.findViewById(R.id.title);
        title.setTypeface(Fonts.AzoSansMedium);
        title.setText(event.getTitle());

        final TextView date = (TextView) view.findViewById(R.id.date);
        date.setTypeface(Fonts.AzoSansRegular);
        date.setText(event.getStringDate(event.getStart_time()));

        final TextView time = (TextView) view.findViewById(R.id.time);
        time.setTypeface(Fonts.AzoSansRegular);
        time.setText(event.getStringTime(event.getStart_time(), true));

        final ImageView freeFoodIcon = (ImageView) view.findViewById(R.id.free_food);
        freeFoodIcon.setVisibility(
                isFreeFoodEvent(event) ? View.VISIBLE : View.GONE
        );

        final StateImageView flag = (StateImageView) view.findViewById(R.id.action_flag);
        flag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag.getCurrentState() == StateImageView.State.ACTIVE) {
                    // TODO(anesu): make api
                    flag.setCurrentState(StateImageView.State.DISABLED);
                    ImageUtils.changeImageViewColor(flag,
                            mContext.getResources().getColor(R.color.LightGrey));
                    Snackbar.make(view,
                            "This event has been unflagged ",
                            Snackbar.LENGTH_LONG)
                            .show();

                } else {
                    // TODO(anesu): make api
                    flag.setCurrentState(StateImageView.State.ACTIVE);
                    ImageUtils.changeImageViewColor(flag,
                            mContext.getResources().getColor(R.color.colorPrimary));
                    Snackbar.make(view,
                            "This event has flagged for review",
                            Snackbar.LENGTH_LONG)
                            .setAction("UNDO", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    flag.callOnClick();
                                }
                            }).show();
                }
            }
        });

        WallaApi.getUserInfo(new WallaApi.OnDataReceived() {
            @Override
            public void onDataReceived(Object data, int call) {
                if (data instanceof User) {
                    final User user = (User) data;
                    ImageUtils.loadImageFromUrl(
                            mContext,
                            ((ImageView) view.findViewById(R.id.icon)),
                            Optional.of(user.getProfileUrl())
                    );

                    final TextView name = (TextView) view.findViewById(R.id.name);
                    name.setTypeface(Fonts.AzoSansRegular);
                    name.setText(user.getFirstName());
                }
            }
        }, event.getHost());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(getContext(), Details.class);
                intent.putExtra("auid", event.getAuid());
                mContext.startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public Event getItem(int position) {
        return mEvents.get(position);
    }

    private boolean isFreeFoodEvent(final Event event) {
        for (String interest : event.getInterests()) {
            if (interest.toLowerCase().contains("food")) {
                return true;
            }
        }

        return false;
    }
}