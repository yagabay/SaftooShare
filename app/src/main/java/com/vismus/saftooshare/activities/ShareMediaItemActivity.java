package com.vismus.saftooshare.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.vismus.saftooshare.R;
import com.vismus.saftooshare.Utils;
import com.vismus.saftooshare.db.local.SharedMediaItemsDatabase;
import com.vismus.saftooshare.db.local.model.SharedMediaItem;
import com.vismus.saftooshare.db.remote.RemoteDatabase;
import com.vismus.saftooshare.db.remote.model.SharedItem;

import java.io.File;
import java.io.IOException;

public class ShareMediaItemActivity extends AppCompatActivity {

    ImageView _imvSharedItem;
    Button _btnShare;

    Uri _sharedItemUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        _imvSharedItem = findViewById(R.id.imv_shared_item);
        _btnShare = findViewById(R.id.btn_share);
        _btnShare.setOnClickListener(new ShareButtonOnClickListener());
        _sharedItemUri = getIntent().getParcelableExtra(Intent.EXTRA_STREAM);
        String sharedItemPath = Utils.getFilePathFromUri(this, _sharedItemUri);
        Utils.MediaType mediaType = Utils.getMediaType(sharedItemPath);
        if(mediaType == Utils.MediaType.PICTURE){
            _imvSharedItem.setImageBitmap(Utils.readBitmapFromFile(sharedItemPath));
        }
        else{
            Bitmap vidThumb =  ThumbnailUtils.createVideoThumbnail(sharedItemPath, MediaStore.Images.Thumbnails.MINI_KIND);
            _imvSharedItem.setImageBitmap(vidThumb);
        }
        _btnShare.callOnClick();
    }

    class ShareButtonOnClickListener implements Button.OnClickListener{

        @Override
        public void onClick(View view){
            view.setEnabled(false);
            final SharedItem sharedItem = new SharedItem(ShareMediaItemActivity.this, _sharedItemUri);
            RemoteDatabase.uploadSharedItem(ShareMediaItemActivity.this, sharedItem, _sharedItemUri);
        }
    }

    public void saveSharedItemToFile(String itemNewName){
           File itemSrc = new File(Utils.getFilePathFromUri(this, _sharedItemUri));
        File itemDst = new File(Utils.APP_SHARED_ITEMS_DIR + itemNewName);
        try {
            Utils.copy(itemSrc, itemDst);
        }
        catch(IOException e){
            Toast.makeText(this, "Error: failed to save file " + itemDst.getPath(), Toast.LENGTH_SHORT).show();
            return;
        }
        SharedMediaItemsDatabase sharedMediaItemsDatabase = SharedMediaItemsDatabase.getInstance(this);
        SharedMediaItem sharedMediaItem = new SharedMediaItem(itemDst.getPath());
        sharedMediaItemsDatabase.sharedMediaItemDao().insert(sharedMediaItem);
    }
}
