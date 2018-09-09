package com.vismus.saftooshare.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.vismus.saftooshare.R;
import com.vismus.saftooshare.Utils;
import com.vismus.saftooshare.data.SharedMediaItem;
import com.vismus.saftooshare.data.SharedMediaItemsDatabase;

import java.io.File;
import java.io.IOException;

public class ShareMediaItemActivity extends AppCompatActivity {

    static final String USERNAME = "yagabay@gmail.com";
    static final String PASSWORD = "1qaz@WSX";

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
        String sharedItemPath = Utils.getPathFromUri(this, _sharedItemUri);
        Utils.MediaType mediaType = Utils.getMediaType(sharedItemPath);
        if(mediaType == Utils.MediaType.PICTURE){
            _imvSharedItem.setImageBitmap(Utils.readBitmapFromFile(sharedItemPath));
        }
        else{
            Bitmap vidThumb =  ThumbnailUtils.createVideoThumbnail(sharedItemPath, MediaStore.Images.Thumbnails.MINI_KIND);
            _imvSharedItem.setImageBitmap(vidThumb);
        }

    }

    class ShareButtonOnClickListener implements Button.OnClickListener{

        @Override
        public void onClick(View view){
            view.setEnabled(false);
            uploadSharedItem();
        }
    }

    void uploadSharedItem(){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(USERNAME, PASSWORD)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            final String itemPath = Utils.getPathFromUri(getApplicationContext(), _sharedItemUri);
                            final String itemNewName = Utils.generateRandomIdentifier() + "." + Utils.getFileExtension(itemPath);
                            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                            firebaseStorage.getReference().child(itemNewName).putFile(_sharedItemUri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            saveSharedItemToFile(itemNewName);
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(intent);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            Toast.makeText(ShareMediaItemActivity.this, "Error: failed to upload file " + itemNewName, Toast.LENGTH_LONG).show();
                                        }
                                    });

                        }
                        else {
                            Toast.makeText(ShareMediaItemActivity.this, "Error: failed to sign-in", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    void saveSharedItemToFile(String itemNewName){
        File itemSrcPath = new File(Utils.getPathFromUri(this, _sharedItemUri));
        File itemDstPath = new File(Utils.APP_SHARED_ITEMS_DIR + itemNewName);
        try {
            Utils.copy(itemSrcPath, itemDstPath);
        }
        catch(IOException e){
            Toast.makeText(this, "Error: failed to save file " + itemDstPath.getPath(), Toast.LENGTH_SHORT).show();
            return;
        }
        SharedMediaItemsDatabase sharedMediaItemsDatabase = SharedMediaItemsDatabase.getInstance(this);
        SharedMediaItem sharedMediaItem = new SharedMediaItem(itemDstPath.getPath());
        sharedMediaItemsDatabase.sharedMediaItemDao().insert(sharedMediaItem);
    }
}