package genieus.com.walla.v2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import genieus.com.walla.R;

/**
 * Created by anesu on 12/20/16.
 */

public class InterestsViewRVAdapter extends RecyclerView.Adapter<InterestsViewHolder> {
    private Context context;
    List<InterestInfo> data;

    public InterestsViewRVAdapter(Context context, List<InterestInfo> data){
        this.context = context;
        this.data = data;
    }

    @Override
    public InterestsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.single_interest, parent, false);

        return new InterestsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(InterestsViewHolder holder, int position) {
        InterestInfo info  = data.get(position);
        holder.icon.setImageResource(info.getImg());
        holder.label.setText(info.getName());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
