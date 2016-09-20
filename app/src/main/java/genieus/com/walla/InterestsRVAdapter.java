package genieus.com.walla;

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

/**
 * Created by Anesu on 9/3/2016.
 */
public class InterestsRVAdapter extends RecyclerView.Adapter<InterestsViewHolder> {

    List<View> all;
    List<Interests> interests;
    ItemClickListener listener;

    public InterestsRVAdapter(List<Interests> interests, ItemClickListener listener) {
        this.interests = interests;
        this.listener = listener;

        all = new ArrayList<>();
    }

    public interface ItemClickListener {
        void onItemClicked(Interests event, View view, List<View> all);
    }

    @Override
    public InterestsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.interest_template, parent, false);

        return new InterestsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final InterestsViewHolder holder, int position) {
        if(!all.contains(holder.rl)){
            all.add(holder.rl);
        }
        final Interests i = interests.get(position);

        holder.iv.setImageResource(i.getImg());
        holder.tv.setText(i.getName());

        Log.d("color", i.getName());

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClicked(i, holder.rl, all);
            }
        });
    }

    @Override
    public int getItemCount() {
        return interests.size();
    }
}
