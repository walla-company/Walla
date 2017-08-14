package genieus.com.walla.v2.adapter.listview;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import genieus.com.walla.R;
import genieus.com.walla.v2.activity.Details;
import genieus.com.walla.v2.api.WallaApi;
import genieus.com.walla.v2.fragment.Notifications;
import genieus.com.walla.v2.info.Fonts;
import genieus.com.walla.v2.info.NotificationInfo;
import genieus.com.walla.v2.info.User;

/**
 * Created by anesu on 12/20/16.
 */

public class NotificationInfoLVAdapter extends ArrayAdapter<NotificationInfo> {
    private List<NotificationInfo> data;
    private int resource;
    private Fonts fonts;
    private WallaApi api;
    private FirebaseAuth auth;
    public NotificationInfoLVAdapter(Context context, int resource, List<NotificationInfo> data) {
        super(context, resource);
        this.data = data;
        this.resource = resource;

        fonts = new Fonts(context);
        api = WallaApi.getInstance(context);
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);

        final NotificationInfo notification = data.get(position);

        final RelativeLayout container = (RelativeLayout) convertView.findViewById(R.id.container);
        final CircleImageView pic = (CircleImageView) convertView.findViewById(R.id.type_icon);
        final TextView info = (TextView) convertView.findViewById(R.id.message);
        info.setTypeface(fonts.AzoSansRegular);
        info.clearComposingText();

        final Button accept = (Button) convertView.findViewById(R.id.accept);
        accept.setTypeface(fonts.AzoSansRegular);
        Button ignore = (Button) convertView.findViewById(R.id.ignore);
        ignore.setTypeface(fonts.AzoSansRegular);

        setImage(pic, notification.getImage_url());

        if(!notification.isRead()){
            changeBackGroundColor(container, getContext().getResources().getColor(R.color.notifblue));
        }

        switch(notification.getType()){
            case Notifications.FRIEND_REQUEST:
                changeBackGroundColor(accept, getContext().getResources().getColor(R.color.lightblue));
                changeBackGroundColor(ignore, getContext().getResources().getColor(R.color.LightGrey));

                accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        api.approveFriendRequest(auth.getCurrentUser().getUid(), notification.getSenderUId());
                        data.remove(notification);
                        notifyDataSetChanged();
                    }
                });

                ignore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        api.ignoreFriendRequest(auth.getCurrentUser().getUid(), notification.getSenderUId());
                        data.remove(notification);
                        notifyDataSetChanged();
                    }
                });

                api.getUserInfo(new WallaApi.OnDataReceived() {
                    @Override
                    public void onDataReceived(Object data, int call) {
                        User user = (User) data;
                        info.setText(String.format("%s %s sent you a friend request", user.getFirst_name(), user.getLast_name()));

                        setImage(pic, user.getProfile_url());
                    }
                }, notification.getSenderUId());
                break;
            case Notifications.GROUP_INVITED:
                info.setText(notification.getMessage());
                accept.setVisibility(View.GONE);
                ignore.setVisibility(View.GONE);

                container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), Details.class);
                        intent.putExtra("auid", notification.getActivityUid());
                        getContext().startActivity(intent);
                    }
                });
                break;
            case Notifications.DISCUSSION_POSTED:
                info.setText(notification.getMessage());
                accept.setVisibility(View.GONE);
                ignore.setVisibility(View.GONE);
                pic.setVisibility(View.INVISIBLE);

                ImageView noneCircleImage = (ImageView) convertView.findViewById(R.id.type_icon2);
                noneCircleImage.setVisibility(View.VISIBLE);

                container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), Details.class);
                        intent.putExtra("auid", notification.getActivityUid());
                        getContext().startActivity(intent);
                    }
                });
                break;
            case Notifications.USER_INVITED:
                info.setText(notification.getMessage());
                accept.setVisibility(View.GONE);
                ignore.setVisibility(View.GONE);

                container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), Details.class);
                        intent.putExtra("auid", notification.getActivityUid());
                        getContext().startActivity(intent);
                    }
                });
            default:
                //container.setVisibility(View.GONE);
                break;
        }


        return convertView;
    }

    private void setImage(final ImageView image, String url){
        if(url != null && !url.equals("")) {
            if(!url.startsWith("gs://walla-launch.appspot.com")) {
                Picasso.with(getContext()) //Context
                        .load(url) //URL/FILE
                        .into(image);//an ImageView Object to show the loaded image;
            }else{
                FirebaseStorage storage = FirebaseStorage.getInstance();
                storage.getReferenceFromUrl(url).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
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
    }

    @Override
    public int getCount() {
        return data.size();
    }

    private void changeBackGroundColor(View view, int color){
        Drawable background = view.getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable)background).getPaint().setColor(color);
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable)background).setColor(color);
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable)background).setColor(color);
        }
    }
}
