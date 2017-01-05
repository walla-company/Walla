package genieus.com.walla.v2.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import genieus.com.walla.v1.Interests;
import genieus.com.walla.v2.api.WallaApi;
import genieus.com.walla.v2.viewholder.FilterViewHolder;
import genieus.com.walla.v2.adapter.recyclerview.InterestsRVAdapter;
import genieus.com.walla.R;
import genieus.com.walla.v2.adapter.listview.EventsLVAdapter;
import genieus.com.walla.v2.info.EventInfo;
import genieus.com.walla.v2.info.Fonts;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Home.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView interest_rv;
    private static ListView events_lv;
    private InterestsRVAdapter adapter;
    private static EventsLVAdapter adapterEvents;
    private List<Interests> interests;
    private static TextView filter_tv;
    private static SwipeRefreshLayout swipeRefreshLayout;

    private static AlertDialog alert;
    private Fonts fonts;
    private String currentFilter = "";

    private static Context context;

    private static WallaApi api;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void initUi() {
        context = getContext();
        api = new WallaApi(getContext());
        fonts = new Fonts(getContext());
        initFilter();
        initEvents();
    }

    private void initEvents() {

        /*
        List<EventInfo> list = new ArrayList<>();

        EventInfo event1 = new EventInfo();
        event1.setTitle("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
        event1.setInterests(Arrays.asList("Movies", "Academics"));
        event1.setInterested(7);
        event1.setGoing(5);
        event1.setStart_time(new Long("1451829458000"));
        event1.setEnd_time(new Long("1451833058000"));
        event1.setIs_public(true);

        EventInfo event2 = new EventInfo();
        event2.setTitle("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
        event2.setInterests(Arrays.asList("Movies", "Academics"));
        event2.setInterested(7);
        event2.setGoing(5);
        event2.setStart_time(new Long("1483196258000"));
        event2.setEnd_time(new Long("1483199858000"));
        event2.setIs_public(true);

        EventInfo event3 = new EventInfo();
        event3.setTitle("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
        event3.setInterests(Arrays.asList("Movies", "Academics"));
        event3.setInterested(7);
        event3.setGoing(5);
        event3.setStart_time(new Long("1451628000000"));
        event3.setEnd_time(new Long("1451800800000"));
        event3.setIs_public(false);

        List<EventInfo> data = new ArrayList<>();
        data.add(event1);
        data.add(event2);
        data.add(event3);

        */

        final Context context = getContext();

        api.getActivities(new WallaApi.OnDataReceived() {
            @Override
            public void onDataReceived(Object data, int call) {
                adapterEvents = new EventsLVAdapter(context, R.layout.single_activity, (List<EventInfo>) data);
                events_lv.setAdapter(adapterEvents);
                adapterEvents.getFilter().filter("");
            }
        });

    }

    private void initFilter() {
        filter_tv.setTypeface(fonts.AzoSansRegular);
        interests = new ArrayList<>();
        interests.add(new Interests("All", R.mipmap.all));
        interests.add(new Interests("Movies", R.drawable.ic_movieicon));
        interests.add(new Interests("Food", R.drawable.ic_foodicon));
        interests.add(new Interests("Academics", R.drawable.ic_academicsicon));
        interests.add(new Interests("Study", R.drawable.ic_studyicon));
        interests.add(new Interests("Sports", R.drawable.ic_sportsicon));
        interests.add(new Interests("Exhibition", R.drawable.ic_exhibitionicon));
        interests.add(new Interests("Music", R.drawable.ic_musicicon));
        interests.add(new Interests("Games", R.drawable.ic_gamesicon));
        interests.add(new Interests("Dance", R.drawable.ic_danceicon));
        interests.add(new Interests("Socialize", R.drawable.ic_socialize));
        interests.add(new Interests("Volunteer", R.drawable.ic_volunteericon));

        //LinearLayoutManager horizontal
        // = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        GridLayoutManager grid
                = new GridLayoutManager(getContext(), 4);

        adapter = new InterestsRVAdapter(interests, new InterestsRVAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(Interests event, View view, List<FilterViewHolder> all, int pos) {
                String query = event.getName().equals("All") ? "" : event.getName();
                currentFilter = query;
                changeColorOfFilters(view, all, pos);
            }
        }, getContext());

        interest_rv.setLayoutManager(grid);
        interest_rv.setAdapter(adapter);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alert.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        alert.getWindow().setAttributes(lp);
        alert.setCanceledOnTouchOutside(true);

    }

    public static void refreshPage() {
        swipeRefreshLayout.setRefreshing(false);
        api.getActivities(new WallaApi.OnDataReceived() {
            @Override
            public void onDataReceived(Object data, int call) {
                adapterEvents = new EventsLVAdapter(context, R.layout.single_activity, (List<EventInfo>) data);
                events_lv.setAdapter(adapterEvents);
                adapterEvents.getFilter().filter("");
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void changeColorOfFilters(View view, List<FilterViewHolder> all, int pos) {
        Drawable background = view.getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable) background).getPaint().setColor(getResources().getColor(R.color.colorPrimary));
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable) background).setColor(getResources().getColor(R.color.colorPrimary));
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable) background).setColor(getResources().getColor(R.color.colorPrimary));
        }

        all.get(pos).label.setTextColor(getResources().getColor(R.color.colorPrimary));

        //String search = event.getName().equals("All") ? "" : event.getName();
        //filterEvents(search);

        for (int i = 0; i < all.size(); i++) {
            if (i != pos) {
                Drawable bg = all.get(i).container1.getBackground();
                if (bg instanceof ShapeDrawable) {
                    ((ShapeDrawable) bg).getPaint().setColor(getResources().getColor(R.color.LightGrey));
                } else if (bg instanceof GradientDrawable) {
                    ((GradientDrawable) bg).setColor(getResources().getColor(R.color.LightGrey));
                } else if (bg instanceof ColorDrawable) {
                    ((ColorDrawable) bg).setColor(getResources().getColor(R.color.LightGrey));
                }

                all.get(i).label.setTextColor(getResources().getColor(R.color.black));
            }
        }
    }

    public static void showFilter() {
        alert.show();
    }

    private void filterEvents(String query) {
        if (query.equals("")) filter_tv.setVisibility(View.GONE);
        else {
            filter_tv.setVisibility(View.VISIBLE);
            filter_tv.setText(String.format("Showing %s events", query.toLowerCase()));
        }

        adapterEvents.getFilter().filter(query);

        /*
        if(adapterEvents.getCount() == 0){
            filter_tv.setVisibility(View.VISIBLE);
            if (query.equals(""))
                filter_tv.setText("There are currently no events");
            else
                filter_tv.setText("There are currently no " + query + " events");
        }
        */

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        createDialogFilter();

        events_lv = (ListView) view.findViewById(R.id.events);
        filter_tv = (TextView) view.findViewById(R.id.filter_label);
        filter_tv.setVisibility(View.GONE);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_container);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPage();
            }
        });

        initUi();
        return view;
    }

    private void createDialogFilter() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false)
                .setPositiveButton("Select Filter", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        filterEvents(currentFilter);
                    }
                });

        View view = LayoutInflater.from(getContext()).inflate(R.layout.filter_popup, null);
        interest_rv = (RecyclerView) view.findViewById(R.id.interests_rv);
        builder.setView(view);
        alert = builder.create();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
