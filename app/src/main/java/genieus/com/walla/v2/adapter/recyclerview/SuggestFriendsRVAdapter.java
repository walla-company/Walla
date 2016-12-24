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

import java.util.List;

import genieus.com.walla.R;
import genieus.com.walla.v2.info.Fonts;
import genieus.com.walla.v2.info.MutualFriendInfo;
import genieus.com.walla.v2.viewholder.SuggestFriendsHolder;

/**
 * Created by anesu on 12/21/16.
 */

public class SuggestFriendsRVAdapter extends RecyclerView.Adapter<SuggestFriendsHolder>{
    private Context context;
    private List<MutualFriendInfo> data;
    private String BUTTONBLUE = "#63CAF9";
    private Fonts fonts;


    public SuggestFriendsRVAdapter(Context context, List<MutualFriendInfo> data){
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
        MutualFriendInfo info = data.get(position);

        holder.name.setTypeface(fonts.AzoSansRegular);
        holder.mutualFriends.setTypeface(fonts.AzoSansRegular);

        holder.name.setText(info.getName());
        holder.mutualFriends.setText(info.getMutualFriends() + " mutual friends");
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
