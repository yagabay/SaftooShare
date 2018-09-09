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
import com.vismus.saftooshare.data.SharedMediaItem;
import com.vismus.saftooshare.data.SharedMediaItemsDatabase;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView _rsvMediaItems;
    private SharedMediaItemsRecyclerViewAdapter _rsvSharedMediaItemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _rsvMediaItems = findViewById(R.id.rsv_media_items);
        _rsvMediaItems.setLayoutManager(new GridLayoutManager(this, 3));
        _rsvMediaItems.setHasFixedSize(true);
        SharedMediaItemsDatabase sharedMediaItemsDatabase = SharedMediaItemsDatabase.getInstance(this);
        List<SharedMediaItem> sharedMediaItems = sharedMediaItemsDatabase.sharedMediaItemDao().getAll();
        _rsvSharedMediaItemsAdapter = new SharedMediaItemsRecyclerViewAdapter(sharedMediaItems);
        _rsvMediaItems.setAdapter(_rsvSharedMediaItemsAdapter);
        requestPermissions();
        createAppDirs();
    }

    @Override
    public void onResume(){
        super.onResume();
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
        if (path.mkdirs()) {
            Toast.makeText(this, "App base path create successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error: failed to create app base path", Toast.LENGTH_SHORT).show();
        }
    }
}

//    void createUser(String username, String password){
//        _firebaseAuth = FirebaseAuth.getInstance();
//        _firebaseAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()) {
//                    Toast.makeText(MainActivity.this, "User created successfully", Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(MainActivity.this, "Error: create user failed", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//    }


//    List<MediaItem> getSharedMediaItems(){
//        List<MediaItem> dataSet = new ArrayList<>();
//        File[] sharedFiles = Utils.getFilesInDir(Utils.getAppBaseDir() + "/Shared");
//        if(sharedFiles != null) {
//            for (int i = 0; i < sharedFiles.length; ++i) {
//                dataSet.add(new MediaItem(sharedFiles[i].getPath()));
//            }
//        }
//        return dataSet;
//    }
