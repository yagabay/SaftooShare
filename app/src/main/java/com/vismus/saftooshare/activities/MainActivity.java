package com.vismus.saftooshare.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.vismus.saftooshare.SharedMediaItemsRecyclerViewAdapter;
import com.vismus.saftooshare.R;
import com.vismus.saftooshare.Utils;
import com.vismus.saftooshare.db.local.SharedMediaItemsDatabase;
import com.vismus.saftooshare.db.local.model.SharedMediaItem;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView _rsvMediaItems;
    SharedMediaItemsRecyclerViewAdapter _rsvSharedMediaItemsAdapter;

    SharedMediaItemsDatabase sharedMediaItemsDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _rsvMediaItems = findViewById(R.id.rsv_media_items);
        _rsvMediaItems.setLayoutManager(new GridLayoutManager(this, 3));
        _rsvMediaItems.setHasFixedSize(true);
        sharedMediaItemsDatabase = SharedMediaItemsDatabase.getInstance(this);
        sharedMediaItemsDatabase.validate();
        List<SharedMediaItem> sharedMediaItems = sharedMediaItemsDatabase.sharedMediaItemDao().getAll();
        _rsvSharedMediaItemsAdapter = new SharedMediaItemsRecyclerViewAdapter(this, sharedMediaItems);
        _rsvMediaItems.setAdapter(_rsvSharedMediaItemsAdapter);
        requestPermissions();
        createAppDirs();
    }

    @Override
    public void onResume(){
        super.onResume();
        sharedMediaItemsDatabase.validate();
        List<SharedMediaItem> items = sharedMediaItemsDatabase.sharedMediaItemDao().getAll();
        _rsvSharedMediaItemsAdapter.setItems(items);
    }

    void requestPermissions(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }
    }

    void createAppDirs(){
        File path = new File(Utils.APP_SHARED_ITEMS_DIR);
        if (path.exists()) {
            return;
        }
        if (!path.mkdirs()) {
            Toast.makeText(this, "Error: failed to create application dirs", Toast.LENGTH_SHORT).show();
        }
    }

}