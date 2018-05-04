package pragya.in.smartscanner.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.text.DecimalFormat;
import java.util.List;

import pragya.in.smartscanner.R;
import pragya.in.smartscanner.model.FileInfo;
import pragya.in.smartscanner.utils.FileUtils;

/**
 * Created by Pragya on 02-05-2018.
 */
public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.ViewHolder> {

    private List<FileInfo> items;


    public FileListAdapter(List<FileInfo> items) {
        this.items = items;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FileInfo item = items.get(position);
        if (holder != null) {
            holder.itemName.setText(item.getName());
            holder.itemSize.setText("Size: " + FileUtils.getFileSize(item.getSize()));
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<FileInfo> items) {
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