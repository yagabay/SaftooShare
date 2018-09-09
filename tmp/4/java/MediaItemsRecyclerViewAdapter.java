package com.vismus.saftooshare;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MediaItemsRecyclerViewAdapter extends RecyclerView.Adapter<MediaItemsRecyclerViewAdapter.ViewHolder> {

    private List<MediaItem> _items;

    public MediaItemsRecyclerViewAdapter(List<MediaItem> items) {
        _items = items;
    }

    @Override
    public int getItemCount() {
        return _items.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.media_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final MediaItem mediaItem = _items.get(position);
        holder.imvMediaItem.setImageBitmap(Utils.readBitmapFromFile(mediaItem.getFilePath()));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imvMediaItem;

        public ViewHolder(View view) {
            super(view);
            imvMediaItem = view.findViewById(R.id.imv_media_item);
        }
    }

}
