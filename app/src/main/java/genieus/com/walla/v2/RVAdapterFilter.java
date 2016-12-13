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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import genieus.com.walla.InterestsViewHolder;
import genieus.com.walla.R;


/**
 * Created by Anesu on 12/13/2016.
 */
public class RVAdapterFilter extends RecyclerView.Adapter<FilterVH> {

    public interface ItemClickListener {
        void onItemClicked(String filter, int pos);
    }

    List<String> filters;
    Context context;
    ItemClickListener listener;

    public RVAdapterFilter(List<String> filters, ItemClickListener listener, Context context) {
        this.filters = filters;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public FilterVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.single_filter, parent, false);

        return new FilterVH(itemView);
    }

    @Override
    public void onBindViewHolder(FilterVH holder, final int position) {
        final String title = filters.get(position);
        holder.text.setText(title);

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClicked(title, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filters.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
