package com.vismus.saftooshare.activities;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.vismus.saftooshare.R;
import com.vismus.saftooshare.Utils;
import com.vismus.saftooshare.data.LocalData;
import com.vismus.saftooshare.data.RemoteData;

import java.io.File;
import java.util.Date;

public class ShareItemActivity extends AppCompatActivity{

    static final String USERNAME = "yagabay@gmail.com";
    static final String PASSWORD = "1qaz@WSX";

    SharedItem _item;

    LocalData.Storage _localStorage;
    LocalData.Database _localDatabase;

    RemoteData.Auth _remoteAuth;
    RemoteData.Storage _remoteStorage;
    RemoteData.Database _remoteDatabase;

    ImageView _imvSharedItem;
    Button _btnShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        _imvSharedItem = findViewById(R.id.imv_shared_item);
        _btnShare = findViewById(R.id.btn_share);
        _btnShare.setOnClickListener(new OnShareButtonClickListener());
        _item = new SharedItem((Uri) getIntent().getParcelableExtra(Intent.EXTRA_STREAM));
        requestPermissions();
    }

    void setDatabaseAndStorageCallbacks(){
        _remoteAuth = RemoteData.auth();
        _remoteAuth.setOnAuthCompleteListener(new RemoteData.Auth.OnAuthCompleteListener() {
            @Override
            public void onAuthComplete(boolean isSuccessful) {
                if(isSuccessful) {
                    _btnShare.callOnClick(); // tmp
                }
                else{
                    Toast.makeText(ShareItemActivity.this, "Error: failed to authenticate user '" + USERNAME + "'", Toast.LENGTH_LONG).show();
                    // exit app
                }
            }
        });
        _remoteStorage = RemoteData.storage();
        _remoteStorage.setOnPutItemCompleteListener(new RemoteData.Storage.OnPutItemCompleteListener() {
            @Override
            public void onPutItemComplete(boolean isSuccessful) {
                if(isSuccessful) {
                    _remoteDatabase.addSharedItem(_item.dstName, _item.size, _item.sharingDate);
                }
                else {
                    Toast.makeText(ShareItemActivity.this, "Error: failed to upload item '" + _item.dstName + "' to remote storage", Toast.LENGTH_LONG).show();
                }
            }
            });
        _remoteDatabase = RemoteData.database();
        _remoteDatabase.setOnAddItemCompleteListener(new RemoteData.Database.OnAddItemCompleteListener() {
            @Override
            public void onAddItemComplete(boolean isSuccessful) {
                if(isSuccessful) {
                    if(_localStorage.storeSharedItem(_item.srcPath, _item.dstPath)){
                        _localDatabase.insertSharedItem(_item.dstPath, _item.sharingDate);
                        Toast.makeText(ShareItemActivity.this, "Item shared successfully!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(ShareItemActivity.this, MainActivity.class));
                    }
                    else{
                        Toast.makeText(ShareItemActivity.this, "Error: failed to save file '" + _item.dstName + "' to local storage", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(ShareItemActivity.this, "Error: failed to add item '" + _item.dstName + "' to remote database", Toast.LENGTH_LONG).show();
                }
            }
        });
        _localStorage = LocalData.storage();
        _localDatabase = LocalData.database(this);
    }

    class OnShareButtonClickListener implements Button.OnClickListener{

        @Override
        public void onClick(View view){
            view.setEnabled(false);
            _remoteStorage.putSharedItem(_item.dstName, _item.uri);
        }
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {
        for(int res : grantResults){
            if(res != 0){
                Toast.makeText(ShareItemActivity.this, "Error: necessary permission(s) is missing", Toast.LENGTH_LONG).show();
                // exit app
            }
        }
        if(!Utils.createAppFolder()){
            Toast.makeText(this, "Error: failed to create app folder", Toast.LENGTH_SHORT).show();
            // exit app
        }
        setDatabaseAndStorageCallbacks();
        displayMediaItem();
        _remoteAuth.authenticate(USERNAME, PASSWORD);
    }

    void requestPermissions(){
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,  Manifest.permission.READ_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(this, permissions, 0);
    }

    void displayMediaItem(){
        Utils.MediaType mediaType = Utils.getMediaType(_item.srcPath);
        if(mediaType == Utils.MediaType.PICTURE){
            _imvSharedItem.setImageBitmap(Utils.readBitmapFromFile(_item.srcPath));
        }
        else{
            Bitmap vidThumb =  ThumbnailUtils.createVideoThumbnail(_item.srcPath, MediaStore.Images.Thumbnails.MINI_KIND);
            _imvSharedItem.setImageBitmap(vidThumb);
        }
    }

    class SharedItem{

        public Uri uri;
        public String srcPath;
        public long size;
        public String dstName;
        public String dstPath;
        public Date sharingDate;

        SharedItem(Uri uri){
            this.uri = uri;
            srcPath = Utils.getFilePathFromUri(ShareItemActivity.this, uri);
            size = new File(srcPath).length();
            dstName = Utils.generateRandomIdentifier() + "." + Utils.getFileExtension(srcPath);
            dstPath = Utils.getBasePath() + "/" + dstName;
            sharingDate = new Date();
        }

    }
}
