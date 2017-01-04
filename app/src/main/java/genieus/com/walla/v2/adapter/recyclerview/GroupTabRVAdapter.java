package genieus.com.walla.v2.adapter.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import genieus.com.walla.R;
import genieus.com.walla.v2.activity.Group;
import genieus.com.walla.v2.info.Fonts;
import genieus.com.walla.v2.info.GroupInfo;
import genieus.com.walla.v2.viewholder.TabHolder;

/**
 * Created by anesu on 1/3/17.
 */

public class GroupTabRVAdapter extends  RecyclerView.Adapter<TabHolder> {
    private Context context;
    private List<GroupInfo> list;
    private Fonts fonts;

    public GroupTabRVAdapter(Context context, List<GroupInfo> list) {
        this.context = context;
        this.list = list;

        Log.d("groupdata", "sizeL " + list.size());;

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
        final GroupInfo info = list.get(position);
        holder.tabName.setText(info.getAbbr());
        holder.tabName.setTypeface(fonts.AzoSansMedium);
        holder.container.setBackgroundColor(context.getResources().getColor(R.color.tan));
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Group.class);
                intent.putExtra("guid", info.getGuid());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
