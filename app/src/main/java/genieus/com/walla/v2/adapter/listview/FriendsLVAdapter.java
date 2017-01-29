package genieus.com.walla.v2.adapter.listview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import genieus.com.walla.R;
import genieus.com.walla.v2.activity.Friends;
import genieus.com.walla.v2.activity.ViewProfile;
import genieus.com.walla.v2.info.Fonts;
import genieus.com.walla.v2.info.FriendInfo;

/**
 * Created by anesu on 12/20/16.
 */

public class FriendsLVAdapter extends ArrayAdapter<FriendInfo> {

    public interface OnFriendStateListener{
        public void onFriendStateChanged(String name, boolean checked);
    }


    private List<FriendInfo> data;
    private int resource;
    private Fonts fonts;
    private OnFriendStateListener listener;

    public FriendsLVAdapter(Context context, OnFriendStateListener listener, int resource, List<FriendInfo> data) {
        super(context, resource);
        this.data = data;
        this.resource = resource;
        this.listener = listener;

        fonts = new Fonts(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
        }

        final FriendInfo friend = data.get(position);

        CheckBox check = (CheckBox) convertView.findViewById(R.id.check);
        if(check != null){
            check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    listener.onFriendStateChanged(friend.getName(), isChecked);
                }
            });
        }else{
            RelativeLayout container = (RelativeLayout) convertView.findViewById(R.id.container);
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ViewProfile.class);
                    intent.putExtra("uid", friend.getUid());
                    getContext().startActivity(intent);
                }
            });
        }

        final CircleImageView image = (CircleImageView) convertView.findViewById(R.id.profile_picture);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        name.setTypeface(fonts.AzoSansMedium);
        TextView details = (TextView) convertView.findViewById(R.id.details);
        details.setTypeface(fonts.AzoSansRegular);

        if(friend.getImage_url() != null && !friend.getImage_url().equals("")) {
            if(!friend.getImage_url().startsWith("gs://walla-launch.appspot.com")) {
                Picasso.with(getContext()) //Context
                        .load(friend.getImage_url()) //URL/FILE
                        .into(image);//an ImageView Object to show the loaded image;
            }else{
                FirebaseStorage storage = FirebaseStorage.getInstance();
                storage.getReferenceFromUrl(friend.getImage_url()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            Picasso.with(getContext()) //Context
                                    .load(task.getResult().toString()) //URL/FILE
                                    .into(image);//an ImageView Object to show the loaded image;
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        }

        name.setText(friend.getName());
        details.setText(friend.getYear() + " · " + friend.getMajor());

        return convertView;
    }
}
