package genieus.com.walla.v2;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import genieus.com.walla.Interests;
import genieus.com.walla.v2.InterestsViewHolder;
import genieus.com.walla.R;

/**
 * Created by Anesu on 9/3/2016.
 */
public class InterestsRVAdapter extends RecyclerView.Adapter<InterestsViewHolder> {

    List<View> all;
    List<Interests> interests;
    ItemClickListener listener;
    Context context;

    public InterestsRVAdapter(List<Interests> interests, ItemClickListener listener, Context context) {
        this.interests = interests;
        this.listener = listener;
        this.context = context;

        all = new ArrayList<>();
    }

    public interface ItemClickListener {
        void onItemClicked(Interests event, View view, List<View> all, int pos);
    }

    private String getColor(String category){
        switch(category){
            case "All":
                return "#FFA160";
            case "Art":
                return "#E47E30";
            case "School":
                return "#F0C330";
            case "Sports":
                return "#3A99D8";
            case "Rides":
                return "#39CA74";
            case "Games":
                return "#FFBB9C";
            case "Food":
                return "#E54D42";
            case "Other":
                return "#9A5CB4";
            default:
                return null;

        }
    }

    @Override
    public InterestsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.interest_template, parent, false);

        return new InterestsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final InterestsViewHolder holder, final int position) {
        if(!all.contains(holder.rl)){
            all.add(holder.rl);
        }
        final Interests i = interests.get(position);

        holder.iv.setImageResource(i.getImg());
        holder.tv.setText(i.getName());

        Log.d("color", i.getName());

        if(position == 0){
            Drawable background = holder.rl.getBackground();
            if (background instanceof ShapeDrawable) {
                ((ShapeDrawable)background).getPaint().setColor(Color.parseColor(getColor(i.getName())));
            } else if (background instanceof GradientDrawable) {
                ((GradientDrawable)background).setColor(Color.parseColor(getColor(i.getName())));
            } else if (background instanceof ColorDrawable) {
                ((ColorDrawable)background).setColor(Color.parseColor(getColor(i.getName())));
            }
        }else{
            Drawable background = holder.rl.getBackground();
            if (background instanceof ShapeDrawable) {
                ((ShapeDrawable)background).getPaint().setColor(context.getResources().getColor(R.color.LightGrey));
            } else if (background instanceof GradientDrawable) {
                ((GradientDrawable)background).setColor(context.getResources().getColor(R.color.LightGrey));
            } else if (background instanceof ColorDrawable) {
                ((ColorDrawable)background).setColor(context.getResources().getColor(R.color.LightGrey));
            }
        }

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClicked(i, holder.rl, all, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return interests.size();
    }
}
