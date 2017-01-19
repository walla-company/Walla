package genieus.com.walla.v2.adapter.recyclerview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import genieus.com.walla.R;
import genieus.com.walla.v2.info.Fonts;
import genieus.com.walla.v2.info.MutualFriendInfo;
import genieus.com.walla.v2.info.UserInfo;
import genieus.com.walla.v2.viewholder.SuggestFriendsHolder;

/**
 * Created by anesu on 12/21/16.
 */

public class SuggestFriendsRVAdapter extends RecyclerView.Adapter<SuggestFriendsHolder>{
    private Context context;
    private List<UserInfo> data;
    private String BUTTONBLUE = "#63CAF9";
    private Fonts fonts;


    public SuggestFriendsRVAdapter(Context context, List<UserInfo> data){
        this.context = context;
        this.data = data;

        fonts = new Fonts(context);
    }

    @Override
    public SuggestFriendsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(context).inflate(R.layout.single_friend_suggest, parent, false);
        return new SuggestFriendsHolder(view);
    }

    @Override
    public void onBindViewHolder(SuggestFriendsHolder holder, int position) {
        UserInfo info = data.get(position);

        holder.name.setTypeface(fonts.AzoSansRegular);
        holder.mutualFriends.setTypeface(fonts.AzoSansRegular);

        holder.name.setText(String.format("%s %s", info.getFirst_name(), info.getLast_name()));
        holder.mutualFriends.setVisibility(View.GONE);
        if(info.getProfile_url() != null && !info.getProfile_url().equals("")) {
            Picasso.with(context) //Context
                    .load(info.getProfile_url()) //URL/FILE
                    .into(holder.icon);//an ImageView Object to show the loaded image;
        }
        changeBackgroundColor(holder.addBtn, BUTTONBLUE);
    }

    @Override
    public int getItemCount() {
        return data.size();
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
}
