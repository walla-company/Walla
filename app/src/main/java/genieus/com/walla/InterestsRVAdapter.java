package genieus.com.walla;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Anesu on 9/3/2016.
 */
public class InterestsRVAdapter extends RecyclerView.Adapter<InterestsViewHolder> {

    List<Interests> interests;

    public InterestsRVAdapter(List<Interests> interests) {
        this.interests = interests;
    }

    @Override
    public InterestsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.interest_template, parent, false);

        return new InterestsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(InterestsViewHolder holder, int position) {
        Interests i = interests.get(position);

        holder.civ.setImageResource(i.getImg());
        holder.tv.setText(i.getName());
    }

    @Override
    public int getItemCount() {
        return interests.size();
    }
}
