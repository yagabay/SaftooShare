package com.vismus.saftooshare;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vismus.saftooshare.data.LocalData;

import java.text.SimpleDateFormat;
import java.util.List;

public class SharedItemsRecyclerViewAdapter extends RecyclerView.Adapter<SharedItemsRecyclerViewAdapter.SharedItemViewHolder> {

    Context _context;
    List<LocalData.SharedItem> _items;

    public SharedItemsRecyclerViewAdapter(Context context, List<LocalData.SharedItem> items) {
        _context = context;
        _items = items;
    }

    @Override
    public int getItemCount() {
        return _items.size();
    }

    @Override
    public SharedItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.shared_item_display, parent, false);
        return new SharedItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SharedItemViewHolder holder, final int position) {
        LocalData.SharedItem item = _items.get(position);

        // thumbnail
        String filePath = item.filePath;
        Utils.MediaType mediaType = Utils.getMediaType(filePath);
        if(mediaType == Utils.MediaType.PICTURE) {
            Bitmap picture = Utils.readBitmapFromFile(filePath);
            DisplayMetrics displayMetrics = _context.getResources().getDisplayMetrics();
            Bitmap pictureThumb = ThumbnailUtils.extractThumbnail(picture, displayMetrics.widthPixels, displayMetrics.widthPixels);
            holder.imvThumbnail.setImageBitmap(pictureThumb);
        }
        else if(mediaType == Utils.MediaType.VIDEO) {
            Bitmap vidThumb =  ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Images.Thumbnails.MINI_KIND);
            holder.imvThumbnail.setImageBitmap(vidThumb);
        }
        else{
            Toast.makeText(_context, "Error: unknown media type", Toast.LENGTH_SHORT).show();
            return;
        }

        // sharingStatus
        LocalData.SharedItem.SharingStatus sharingStatus = LocalData.SharedItem.SharingStatus.SENT; // TMP
        int imgResourceId = 0;
        switch(sharingStatus){
            case SENT: imgResourceId = R.drawable.ic_item_sent;
                break;
            case RECEIVED: imgResourceId = R.drawable.ic_item_received;
                break;
            case SEEN: imgResourceId = R.drawable.ic_item_seen;
            default: break;
        }
        holder.imvStatus.setImageResource(imgResourceId);

        // sharingDate
        String dateStr = new SimpleDateFormat("HH:mm:ss").format(item.sharingDate);
        holder.txvDate.setText(dateStr);
    }

    public class SharedItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView imvThumbnail;
        public ImageView imvStatus;
        public TextView txvDate;

        public SharedItemViewHolder(View view) {
            super(view);
            imvThumbnail = view.findViewById(R.id.imv_thumbnail);
            imvStatus = view.findViewById(R.id.imv_status);
            txvDate = view.findViewById(R.id.txv_date);
        }
    }

    public void setItems(List<LocalData.SharedItem> items){
        _items = items;
        notifyDataSetChanged();
    }

}