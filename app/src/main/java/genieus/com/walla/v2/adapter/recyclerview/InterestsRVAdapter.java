package genieus.com.walla.v2.adapter.recyclerview;

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

import genieus.com.walla.R;
import genieus.com.walla.v1.Interests;
import genieus.com.walla.v2.viewholder.FilterViewHolder;
import genieus.com.walla.v2.info.Fonts;

/**
 * Created by Anesu on 9/3/2016.
 */
public class InterestsRVAdapter extends RecyclerView.Adapter<FilterViewHolder> {
    private Fonts fonts;

    private List<FilterViewHolder> all;
    private List<Interests> interests;
    private ItemClickListener listener;
    private Context context;

    public InterestsRVAdapter(List<Interests> interests, ItemClickListener listener, Context context) {
        this.interests = interests;
        this.listener = listener;
        this.context = context;

        all = new ArrayList<>();
        fonts = new Fonts(context);
    }

    public interface ItemClickListener {
        void onItemClicked(Interests event, View view, List<FilterViewHolder> all, int pos);
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
    public FilterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.interest_template, parent, false);

        return new FilterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FilterViewHolder holder, final int position) {
        if(!all.contains(holder)){
            all.add(holder);
        }
        final Interests i = interests.get(position);

        holder.icon.setImageResource(i.getImg());
        holder.label.setText(i.getName());
        holder.label.setTypeface(fonts.AzoSansRegular);
        holder.label.setTextColor(context.getResources().getColor(R.color.black));

        if(position == 0){
            Drawable background = holder.container1.getBackground();
            if (background instanceof ShapeDrawable) {
                ((ShapeDrawable)background).getPaint().setColor(Color.parseColor(getColor(i.getName())));
            } else if (background instanceof GradientDrawable) {
                ((GradientDrawable)background).setColor(Color.parseColor(getColor(i.getName())));
            } else if (background instanceof ColorDrawable) {
                ((ColorDrawable)background).setColor(Color.parseColor(getColor(i.getName())));
            }
        }else{
            Drawable background = holder.container1.getBackground();
            if (background instanceof ShapeDrawable) {
                ((ShapeDrawable)background).getPaint().setColor(context.getResources().getColor(R.color.LightGrey));
            } else if (background instanceof GradientDrawable) {
                ((GradientDrawable)background).setColor(context.getResources().getColor(R.color.LightGrey));
            } else if (background instanceof ColorDrawable) {
                ((ColorDrawable)background).setColor(context.getResources().getColor(R.color.LightGrey));
            }
        }

        holder.container2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClicked(i, holder.container1, all, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return interests.size();
    }
}
