package genieus.com.walla.v2.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;
import genieus.com.walla.R;
import genieus.com.walla.v2.activity.EditProfile;
import genieus.com.walla.v2.activity.LoginScreenEmail;
import genieus.com.walla.v2.api.WallaApi;
import genieus.com.walla.v2.info.Fonts;
import genieus.com.walla.v2.info.GroupInfo;
import genieus.com.walla.v2.info.UserInfo;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserProfile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserProfile extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private WallaApi api;
    private Fonts fonts;
    private FirebaseAuth auth;
    private UserInfo user;

    private View view;
    private TextView edit, contact, logout, name, major, year, hometown, desc;
    private LinearLayout groupsContainer;
    private CircleImageView image;

    private OnFragmentInteractionListener mListener;

    private void initUi(){
        fonts = new Fonts(getContext());

        edit = (TextView) view.findViewById(R.id.edit_profile);
        edit.setTypeface(fonts.AzoSansRegular);
        edit.setOnClickListener(this);

        contact = (TextView) view.findViewById(R.id.contact);
        contact.setTypeface(fonts.AzoSansRegular);
        contact.setOnClickListener(this);

        logout = (TextView) view.findViewById(R.id.logout);
        logout.setTypeface(fonts.AzoSansRegular);
        logout.setOnClickListener(this);

        image = (CircleImageView) view.findViewById(R.id.profile_picture);
        setImage(image, user.getProfile_url());

        name = (TextView) view.findViewById(R.id.name);
        name.setTypeface(fonts.AzoSansRegular);
        name.setText(String.format("%s %s", user.getFirst_name(), user.getLast_name()));

        major = (TextView) view.findViewById(R.id.major);
        major.setTypeface(fonts.AzoSansRegular);
        major.setText(user.getMajor());

        year = (TextView) view.findViewById(R.id.year);
        year.setTypeface(fonts.AzoSansRegular);
        year.setText(user.getYear());

        hometown = (TextView) view.findViewById(R.id.hometowen);
        hometown.setTypeface(fonts.AzoSansRegular);
        hometown.setText(user.getHometown());

        desc = (TextView) view.findViewById(R.id.details_in);
        desc.setTypeface(fonts.AzoSansRegular);
        desc.setText(user.getDescription());

        groupsContainer = (LinearLayout) view.findViewById(R.id.group_container);
        showUserGroups();
    }

    private void showUserGroups(){
        for(String key : user.getGroups()){
            api.getGroup(new WallaApi.OnDataReceived() {
                @Override
                public void onDataReceived(Object data, int call) {
                    GroupInfo group = (GroupInfo) data;
                    initAndAttachGroup(getGroupView(), group);
                }
            }, key);
        }
    }

    private View getGroupView(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.single_group_profile, null);
        return view;
    }

    private void initAndAttachGroup(View view, GroupInfo group){
        TextView abbr = (TextView) view.findViewById(R.id.abbr);
        abbr.setTypeface(fonts.AzoSansRegular);
        TextView name = (TextView) view.findViewById(R.id.title_group);
        name.setTypeface(fonts.AzoSansRegular);

        RelativeLayout container = (RelativeLayout) view.findViewById(R.id.group_icon_container);
        changeBackgroundColor(container, group.getColor());

        abbr.setText(group.getAbbr());
        name.setText(group.getName());

        groupsContainer.addView(view);
    }

    private void changeBackgroundColor(View view, String color){
        Drawable background = view.getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable)background).getPaint().setColor(Color.parseColor(color));
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable)background).setColor(Color.parseColor(color));
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable)background).setColor(Color.parseColor(color));
        }
    }

    private void setImage(final ImageView imageView, String url){
        if(url != null && !url.equals("")) {
            if(!url.startsWith("gs://walla-launch.appspot.com")) {
                Picasso.with(getContext()) //Context
                        .load(url) //URL/FILE
                        .into(imageView);//an ImageView Object to show the loaded image;
            }else{
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
            }
        }
    }

    private void contactWalla() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:hollawalladuke@gmail.com"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Walla Customer Service");

        startActivity(Intent.createChooser(intent, "Send Email"));
    }

    public UserProfile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static UserProfile newInstance(String param1, String param2) {
        UserProfile fragment = new UserProfile();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        this.view = view;

        auth = FirebaseAuth.getInstance();
        api = WallaApi.getInstance(getContext());
        api.getUserInfo(new WallaApi.OnDataReceived() {
            @Override
            public void onDataReceived(Object data, int call) {
                user = (UserInfo) data;
                initUi();
            }
        }, auth.getCurrentUser().getUid());

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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.edit_profile:
                getActivity().startActivity(new Intent(getActivity(), EditProfile.class));
                break;
            case R.id.contact:
                contactWalla();
                break;
            case R.id.logout:
                auth.signOut();
                getActivity().startActivity(new Intent(getActivity(), LoginScreenEmail.class));
                break;
            default:
                break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
