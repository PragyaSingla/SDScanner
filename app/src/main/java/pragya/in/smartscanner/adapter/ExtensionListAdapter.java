package pragya.in.smartscanner.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import pragya.in.smartscanner.R;
import pragya.in.smartscanner.model.ExtInfo;
import pragya.in.smartscanner.model.FileInfo;

/**
 * Created by Pragya on 02-05-2018.
 */
public class ExtensionListAdapter extends RecyclerView.Adapter<ExtensionListAdapter.ViewHolder> {

    private List<ExtInfo> items;

    public ExtensionListAdapter(List<ExtInfo> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ExtInfo item = items.get(position);
        if (holder != null) {
            holder.itemName.setText(item.getName());
            holder.itemSize.setText("Frequencies: " + item.getFrequence());
        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<ExtInfo> items) {
        this.items = items;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView itemName, itemSize;

        ViewHolder(View view) {
            super(view);
            itemName = view.findViewById(R.id.tv_item_name);
            itemSize = view.findViewById(R.id.tv_item_size);

        }
    }
}