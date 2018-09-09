package com.vismus.saftooshare.activities;

import android.Manifest;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.vismus.saftooshare.SharedItemsRecyclerViewAdapter;
import com.vismus.saftooshare.R;
import com.vismus.saftooshare.Utils;
import com.vismus.saftooshare.data.LocalData;

public class MainActivity extends AppCompatActivity {

    RecyclerView _rsvSharedItems;
    SharedItemsRecyclerViewAdapter _rsvSharedItemsAdapter;

    LocalData.Storage _localStorage;
    LocalData.Database _localDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _localStorage = LocalData.storage();
        _localDatabase = LocalData.database(this);
        _rsvSharedItems = findViewById(R.id.rsv_shared_items);
        _rsvSharedItems.setLayoutManager(new LinearLayoutManager(this));
        _rsvSharedItemsAdapter = new SharedItemsRecyclerViewAdapter(this, _localDatabase.getSharedItems());
        _rsvSharedItems.setAdapter(_rsvSharedItemsAdapter);
        requestPermissions();
    }

    @Override
    public void onResume(){
        super.onResume();
        _rsvSharedItemsAdapter.setItems(_localDatabase.getSharedItems());
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {
        for(int res : grantResults){
            if(res != 0){
                MainActivity.this.finish();
            }
        }
        if(!Utils.createAppFolder()){
            Toast.makeText(this, "Error: failed to create app folder", Toast.LENGTH_SHORT).show();
        }
    }

    void requestPermissions(){
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,  Manifest.permission.READ_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(this, permissions, 0);
    }

}
