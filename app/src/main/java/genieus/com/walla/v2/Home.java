package genieus.com.walla.v2;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import genieus.com.walla.Interests;
import genieus.com.walla.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Home.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView interest_rv;
    private ListView events_lv;
    private InterestsRVAdapter adapter;
    private List<Interests> interests;
    private static Dialog dialog;

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
        initFilter();
        initEvents();
    }

    private void initEvents() {
        List<Event> list = new ArrayList<>();

        list.add(new Event("Dance", 4, "Lorem ipsum dolor sit amet, consectetur adipiscing elit.", Event.Type.LIT, "6:45 PM", "7:55",2, 11, new ArrayList<String>(Arrays.asList("Movies", "Academics"))));
        list.add(new Event("Academics", 6, "Lorem ipsum dolor sit amet, consectetur adipiscing elit.", Event.Type.CHILL, "3:45 PM", "4:30", 4, 2, new ArrayList<String>(Arrays.asList("Sports","CS201"))));
        list.add(new Event("Food", 6, "Lorem ipsum dolor sit amet, consectetur adipiscing elit.", Event.Type.CHILL, "8:45 AM", "10:00", 7, 5, new ArrayList<String>(Arrays.asList("Dance", "Music", "Socialize"))));
        list.add(new Event("Music", 7, "Lorem ipsum dolor sit amet, consectetur adipiscing elit.", Event.Type.LIT, "12:45 PM", "2:30", 3, 13, new ArrayList<String>(Arrays.asList("Free Food"))));
        list.add(new Event("Socialize", 11, "Lorem ipsum dolor sit amet, consectetur adipiscing elit.", Event.Type.CHILL, "11:00 AM", "1:55", 7, 4, new ArrayList<String>(Arrays.asList("Music","Dab Squad"))));

        EventsLVAdapter adapter = new EventsLVAdapter(getContext(), R.layout.single_activity, list);
        events_lv.setAdapter(adapter);
    }

    private void initFilter() {
        interests = new ArrayList<>();
        interests.add(new Interests("All", R.mipmap.all));
        interests.add(new Interests("Movies", R.mipmap.other));
        interests.add(new Interests("Food", R.mipmap.food));
        interests.add(new Interests("Academics", R.mipmap.other));
        interests.add(new Interests("Study", R.mipmap.other));
        interests.add(new Interests("Sports", R.mipmap.other));
        interests.add(new Interests("Exhibition", R.mipmap.other));
        interests.add(new Interests("Music", R.mipmap.other));
        interests.add(new Interests("Games", R.mipmap.games));
        interests.add(new Interests("Dance", R.mipmap.other));
        interests.add(new Interests("Socialize", R.mipmap.other));
        interests.add(new Interests("Other", R.mipmap.other));

        dialog.setTitle("Filter activities");

        //LinearLayoutManager horizontal
        // = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        GridLayoutManager grid
                = new GridLayoutManager(getContext(), 4);

        adapter = new InterestsRVAdapter(interests, new InterestsRVAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(Interests event, View view, List<View> all, int pos) {
                changeColorOfFilters(event, view, all, pos);
            }
        }, getContext());

        interest_rv.setLayoutManager(grid);
        interest_rv.setAdapter(adapter);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(lp);
    }

    private void changeColorOfFilters(Interests event, View view, List<View> all, int pos) {
        Drawable background = view.getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable)background).getPaint().setColor(getResources().getColor(R.color.colorPrimary));
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable)background).setColor(getResources().getColor(R.color.colorPrimary));
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable)background).setColor(getResources().getColor(R.color.colorPrimary));
        }

        //String search = event.getName().equals("All") ? "" : event.getName();
        //filterEvents(search);

        for(int i= 0; i < all.size(); i++){
            if(i != pos){
                Drawable bg = all.get(i).getBackground();
                if (bg instanceof ShapeDrawable) {
                    ((ShapeDrawable)bg).getPaint().setColor(getResources().getColor(R.color.LightGrey));
                } else if (bg instanceof GradientDrawable) {
                    ((GradientDrawable)bg).setColor(getResources().getColor(R.color.LightGrey));
                } else if (bg instanceof ColorDrawable) {
                    ((ColorDrawable)bg).setColor(getResources().getColor(R.color.LightGrey));
                }
            }
        }
    }

    public static void showFilter(){
        dialog.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.filter_popup);
        interest_rv = (RecyclerView) dialog.findViewById(R.id.interests_rv);
        events_lv = (ListView) view.findViewById(R.id.events);

        initUi();
        return view;
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
