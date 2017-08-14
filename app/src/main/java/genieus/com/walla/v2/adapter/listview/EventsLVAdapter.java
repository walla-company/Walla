package genieus.com.walla.v2.adapter.listview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import genieus.com.walla.R;
import genieus.com.walla.v2.activity.Details;
import genieus.com.walla.v2.activity.Group;
import genieus.com.walla.v2.adapter.recyclerview.GroupTabRVAdapter;
import genieus.com.walla.v2.api.WallaApi;
import genieus.com.walla.v2.fragment.Home;
import genieus.com.walla.v2.info.EventInfo;
import genieus.com.walla.v2.adapter.recyclerview.TabRVAdapter;
import genieus.com.walla.v2.info.Fonts;
import genieus.com.walla.v2.info.GroupInfo;
import genieus.com.walla.v2.info.UserInfo;

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
    private FirebaseAuth auth;


    public EventsLVAdapter(Context context, int resource, List<EventInfo> events) {
        super(context, resource);
        this.resource = resource;
        this.events = events;

        filtered = new ArrayList<>();
        filter = new ItemFilter();
        fonts = new Fonts(context);
        api = WallaApi.getInstance(context);
        auth = FirebaseAuth.getInstance();
    }

    private EventInfo getEvent(List<EventInfo> events, String query){
        if(events == null){
            return null;
        }

        for(EventInfo event : events){
            if((event.getInterests().toString() + event.getTitle() + event.getStart_time() + event.getStringDate(event.getStart_time())).equals(query)){
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
        if(filtered == null){
            return 0;
        }
        return filtered.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        LayoutInflater inflater = LayoutInflater.from(getContext());
        view = inflater.inflate(resource, null);

        TextView title = (TextView) view.findViewById(R.id.title);
        final TextView name = (TextView) view.findViewById(R.id.name);
        TextView duration = (TextView) view.findViewById(R.id.duration);
        TextView date = (TextView) view.findViewById(R.id.date);
        //final ImageView more = (ImageView) view.findViewById(R.id.more);
        //final ImageView action = (ImageView) view.findViewById(R.id.user_action);

        final CircleImageView icon = (CircleImageView) view.findViewById(R.id.icon);
        final ImageView food = (ImageView) view.findViewById(R.id.free_food);
        food.setVisibility(View.GONE);

        final EventInfo event = getEvent(events, filtered.get(position));

        /*
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getContext(), more);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.feed_options, popup.getMenu());

                if(!event.getHost().equals(auth.getCurrentUser().getUid())){
                    popup.getMenu().findItem(R.id.delete_post).setVisible(false);
                }

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();

                        switch (id){
                            case R.id.interested:
                                api.interested(auth.getCurrentUser().getUid(), event.getAuid());
                                Toast.makeText(getContext(), "Added to interested list", Toast.LENGTH_LONG).show();
                                break;
                            case R.id.going:
                                api.going(auth.getCurrentUser().getUid(), event.getAuid());
                                Toast.makeText(getContext(), "Added to going list", Toast.LENGTH_LONG).show();
                            case R.id.flag_post:
                                //TODO: add code
                                Toast.makeText(getContext(), "Post has been flagged", Toast.LENGTH_LONG).show();
                                break;
                            case R.id.delete_post:
                                api.deleteActivity(auth.getCurrentUser().getUid(), event.getAuid());
                                Home.refreshPage(false);
                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                });

                popup.show(); //showing popup menu
            }
        });

        */

        api.getUserInfo(new WallaApi.OnDataReceived() {
            @Override
            public void onDataReceived(Object data, int call) {
                UserInfo user = (UserInfo) data;
                name.setText(user.getFirst_name());

                //setImage(icon, user.getProfile_url());
            }
        }, event.getHost());

        for(String interest : event.getInterests()){
            if(interest.toLowerCase().contains("food")){
                food.setVisibility(View.VISIBLE);
                break;
            }
        }
        title.setText(event.getTitle());
        title.setTypeface(fonts.AzoSansMedium);
        date.setTypeface(fonts.AzoSansRegular);
        date.setText(event.getStringDate(event.getStart_time()));
        duration.setText(event.getStringTime(event.getStart_time(), true));
        duration.setTypeface(fonts.AzoSansRegular);

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

    private void setImage(final ImageView imageView, String url){
        if(url != null && !url.equals("")) {
            if(!url.startsWith("gs://walla-launch.appspot.com")) {
                Picasso.with(getContext()) //Context
                        .load(url) //URL/FILE
                        .into(imageView);//an ImageView Object to show the loaded image;
            }else{
                /*
                FirebaseStorage storage = FirebaseStorage.getInstance();
                storage.getReferenceFromUrl(url).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            Picasso.with(getContext()) //Context
                                    .load(task.getResult().toString()) //URL/FILE
                                    .into(imageView);//an ImageView Object to show the loaded image;
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
                */
            }
        }
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
                filterableString = list.get(i).getInterests().toString() + list.get(i).getTitle() + list.get(i).getStart_time() + list.get(i).getStringDate(list.get(i).getStart_time());
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
