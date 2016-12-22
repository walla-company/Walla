package genieus.com.walla.v2.adapter.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import genieus.com.walla.R;
import genieus.com.walla.v2.viewholder.FilterHolder;


/**
 * Created by Anesu on 12/13/2016.
 */
public class FilterRVAdapter extends RecyclerView.Adapter<FilterHolder> {

    public interface ItemClickListener {
        void onItemClicked(String filter, int pos);
    }

    List<String> filters;
    Context context;
    ItemClickListener listener;

    public FilterRVAdapter(List<String> filters, ItemClickListener listener, Context context) {
        this.filters = filters;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public FilterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.single_filter, parent, false);

        return new FilterHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FilterHolder holder, final int position) {
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
