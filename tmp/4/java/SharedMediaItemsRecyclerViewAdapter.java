package com.vismus.saftooshare;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.vismus.saftooshare.data.SharedMediaItem;

import java.util.List;

public class SharedMediaItemsRecyclerViewAdapter extends RecyclerView.Adapter<SharedMediaItemsRecyclerViewAdapter.ViewHolder> {

    private List<SharedMediaItem> _items;

    public SharedMediaItemsRecyclerViewAdapter(List<SharedMediaItem> items) {
        _items = items;
    }

    public void setItems(List<SharedMediaItem> items){
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
        final SharedMediaItem sharedMediaItem = _items.get(position);
        Bitmap bitmap = Utils.readBitmapFromFile(sharedMediaItem.getFilePath());
        Bitmap thumbBitmap = Utils.getThumbBitmap(bitmap);
        holder.imvMediaItem.setImageBitmap(thumbBitmap);
        SharedMediaItem.SharingStatus sharingStatus = sharedMediaItem.getSharingStatus();
        if(sharingStatus == SharedMediaItem.SharingStatus.SENT){
            holder.imvSharingStatus.setImageResource(R.drawable.ic_item_sent);
        }
        else if(sharingStatus == SharedMediaItem.SharingStatus.RECEIVED){
            holder.imvSharingStatus.setImageResource(R.drawable.ic_item_received);
        }
        else{ // == SEEN
            holder.imvSharingStatus.setImageResource(R.drawable.ic_item_seen);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imvMediaItem;
        public ImageView imvSharingStatus;

        public ViewHolder(View view) {
            super(view);
            imvMediaItem = view.findViewById(R.id.imv_media_item);
            imvSharingStatus = view.findViewById(R.id.imv_sharing_status);
        }
    }

}
