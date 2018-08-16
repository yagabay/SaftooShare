package com.vismus.saftooshare;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.vismus.saftooshare.db.local.model.SharedMediaItem;

import java.util.List;

import static java.lang.Math.min;

public class SharedMediaItemsRecyclerViewAdapter extends RecyclerView.Adapter<SharedMediaItemsRecyclerViewAdapter.ViewHolder> {

    Context _context;
    List<SharedMediaItem> _items;

    public SharedMediaItemsRecyclerViewAdapter(Context context, List<SharedMediaItem> items) {
        _context = context;
        _items = items;
    }

    @Override
    public int getItemCount() {
        return _items.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.media_item_display, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        SharedMediaItem sharedMediaItem = _items.get(position);

        // thumb
        String filePath = sharedMediaItem.getFilePath();
        Utils.MediaType mediaType = Utils.getMediaType(filePath);
        if(mediaType == Utils.MediaType.PICTURE) {
            Bitmap picture = Utils.readBitmapFromFile(filePath);
            int picThumbSize = min(picture.getWidth(), picture.getHeight());
            Bitmap pictureThumb = ThumbnailUtils.extractThumbnail(picture, picThumbSize, picThumbSize);
            holder.imvMediaItemDisplay.setImageBitmap(pictureThumb);
        }
        else if(mediaType == Utils.MediaType.VIDEO) {
            Bitmap vidThumb =  ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Images.Thumbnails.MINI_KIND);
            holder.imvMediaItemDisplay.setImageBitmap(vidThumb);
        }
        else{
            Toast.makeText(_context, "Error: unknown media type", Toast.LENGTH_SHORT).show();
            return;
        }

        // sharing status
        SharedMediaItem.SharingStatus sharingStatus = sharedMediaItem.getSharingStatus();
        int imgResourceId = 0;
        switch(sharingStatus){
            case SENT: imgResourceId = R.drawable.ic_item_sent;
                break;
            case RECEIVED: imgResourceId = R.drawable.ic_item_received;
                break;
            case SEEN: imgResourceId = R.drawable.ic_item_seen;
            default: break;
        }
        holder.imvMediaItemSharingStatus.setImageResource(imgResourceId);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imvMediaItemDisplay;
        public ImageView imvMediaItemSharingStatus;

        public ViewHolder(View view) {
            super(view);
            imvMediaItemDisplay = view.findViewById(R.id.imv_media_item_thumbnail);
            imvMediaItemSharingStatus = view.findViewById(R.id.imv_media_item_sharing_status);
        }
    }

    public void setItems(List<SharedMediaItem> items){
        _items = items;
        notifyDataSetChanged();
    }
}
