package genieus.com.walla.v2.adapter.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import genieus.com.walla.R;
import genieus.com.walla.v2.activity.Group;
import genieus.com.walla.v2.activity.InterestsView;
import genieus.com.walla.v2.info.Fonts;
import genieus.com.walla.v2.viewholder.TabHolder;

/**
 * Created by Anesu on 12/17/2016.
 */
public class TabRVAdapter extends RecyclerView.Adapter<TabHolder> {
    private Context context;
    private List<String> list;
    private Fonts fonts;

    public TabRVAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;

        fonts = new Fonts(context);
    }

    @Override
    public TabHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(context).
                inflate(R.layout.single_tab, parent, false);

        return new TabHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TabHolder holder, int position) {
        holder.tabName.setText(list.get(position));
        holder.tabName.setTypeface(fonts.AzoSansMedium);
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, InterestsView.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
