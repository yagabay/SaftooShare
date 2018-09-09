package com.vismus.saftooshare.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vismus.saftooshare.R;
import com.vismus.saftooshare.Utils;
import com.vismus.saftooshare.data.SharedMediaItem;
import com.vismus.saftooshare.data.SharedMediaItemsDatabase;

import java.io.File;
import java.io.IOException;

public class ShareActivity extends AppCompatActivity {

    static final String USERNAME = "yagabay@gmail.com";
    static final String PASSWORD = "1qaz@WSX";

    ImageView _imvMediaItem;
    Button _btnShare;

    Uri _sharedMediaItemFileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        _imvMediaItem = findViewById(R.id.imv_media_item);
        _btnShare = findViewById(R.id.btn_share);
        _btnShare.setOnClickListener(new ShareButtonOnClickListener());
        _sharedMediaItemFileUri = getIntent().getParcelableExtra(Intent.EXTRA_STREAM);
        showSharedMediaItem();
    }

    class ShareButtonOnClickListener implements Button.OnClickListener{

        @Override
        public void onClick(View view){
            authAndUploadFile(_sharedMediaItemFileUri);
            saveFile(_sharedMediaItemFileUri);
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);

        }
    }

    void showSharedMediaItem(){
        String filePath = Utils.getPathFromUri(this, _sharedMediaItemFileUri);
        Bitmap bitmap = Utils.readBitmapFromFile(filePath);
        _imvMediaItem.setImageBitmap(bitmap);
    }

    void authAndUploadFile(final Uri fileUri){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(USERNAME, PASSWORD)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            uploadFile(fileUri);
                            Toast.makeText(ShareActivity.this, "Signed-in successfully", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(ShareActivity.this, "Error: failed to sign-in Share failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    void uploadFile(Uri fileUri){
        String filePath = Utils.getPathFromUri(this, fileUri);
        String targetFileName = Utils.generateRandomIdentifier() + "." + Utils.getFileExtension(filePath);
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference targetFileRef = storageRef.child(targetFileName);
        targetFileRef.putFile(fileUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(ShareActivity.this, "File uploaded successfully", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(ShareActivity.this, "Error: failed to upload file", Toast.LENGTH_LONG).show();
                    }
                });
    }

    void saveFile(Uri fileUri){
        File srcFile = new File(Utils.getPathFromUri(this, fileUri));
        File dstFile = new File(Utils.APP_SHARED_ITEMS_DIR + srcFile.getName());
        try {
            Utils.copy(srcFile, dstFile);
        }
        catch(IOException e){
            Toast.makeText(this, "Error: failed to save file " + dstFile.getPath(), Toast.LENGTH_SHORT).show();
            return;
        }
        SharedMediaItemsDatabase sharedMediaItemsDatabase = SharedMediaItemsDatabase.getInstance(this);
        SharedMediaItem sharedMediaItem = new SharedMediaItem(dstFile.getPath());
        sharedMediaItemsDatabase.sharedMediaItemDao().insert(sharedMediaItem);
    }
}