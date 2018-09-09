package com.vismus.saftooshare;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        final MediaItem friend = _items.get(position);
        holder.name.setText(friend.getName());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.txv_media_item);
        }
    }
}

//public class MediaItemsRecyclerViewAdapter extends RecyclerView.Adapter<MediaItemsRecyclerViewAdapter.ViewHolder> {
//    private String[] mDataset;
//
//    // Provide a reference to the views for each data item
//    // Complex data items may need more than one view per item, and
//    // you provide access to all the views for a data item in a view holder
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        // each data item is just a string in this case
//        public TextView mTextView;
//        public ViewHolder(TextView v) {
//            super(v);
//            mTextView = v;
//        }
//    }
//
//    // Provide a suitable constructor (depends on the kind of dataset)
//    public MediaItemsRecyclerViewAdapter(String[] myDataset) {
//        mDataset = myDataset;
//    }
//
//    // Create new views (invoked by the layout manager)
//    @Override
//    public MediaItemsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        // create a new view
//        TextView v = (TextView) LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.test_list_item, parent, false);
//        //...
//        ViewHolder vh = new ViewHolder(v);
//        return vh;
//    }
//
//    // Replace the contents of a view (invoked by the layout manager)
//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//        // - get element from your dataset at this position
//        // - replace the contents of the view with that element
//        holder.mTextView.setText(mDataset[position]);
//
//    }
//
//    // Return the size of your dataset (invoked by the layout manager)
//    @Override
//    public int getItemCount() {
//        return mDataset.length;
//    }
//}