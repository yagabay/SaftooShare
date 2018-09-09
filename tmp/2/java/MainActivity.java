package com.vismus.saftooshare.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.vismus.saftooshare.MediaItem;
import com.vismus.saftooshare.MediaItemsRecyclerViewAdapter;
import com.vismus.saftooshare.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.rsv_media_items);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MediaItemsRecyclerViewAdapter(createDataSet());
        mRecyclerView.setAdapter(mAdapter);
    }

//    String[] createDataSet(){
//        String[] dataSet = null;
//        return dataSet;
//    }

    List<MediaItem> createDataSet(){
        List<MediaItem> dataSet = new ArrayList<>();
        dataSet.add(new MediaItem("Yaniv"));
        dataSet.add(new MediaItem("Gabay"));
        return dataSet;
    }

}

