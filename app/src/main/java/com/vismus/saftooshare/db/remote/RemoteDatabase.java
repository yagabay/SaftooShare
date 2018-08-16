package com.vismus.saftooshare.db.remote;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.vismus.saftooshare.activities.MainActivity;
import com.vismus.saftooshare.activities.ShareMediaItemActivity;
import com.vismus.saftooshare.db.remote.model.SharedItem;

public class RemoteDatabase {

    public static final String PATH_SHARED_ITEMS = "shared_items";
    public static final String PATH_USERS = "user";

    static DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

    static final String USERNAME = "yagabay@gmail.com";
    static final String PASSWORD = "1qaz@WSX";

    public static void uploadSharedItem(final ShareMediaItemActivity activity, final SharedItem sharedItem, final Uri uri){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(USERNAME, PASSWORD)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                            firebaseStorage.getReference().child(sharedItem.name).putFile(uri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            DatabaseReference sharedItemRef = databaseRef.child(PATH_SHARED_ITEMS).push();
                                            databaseRef.child(PATH_SHARED_ITEMS).child(sharedItemRef.getKey()).setValue(sharedItem);
                                            activity.saveSharedItemToFile(sharedItem.name); // IMPROVE
                                            activity.startActivity(new Intent(activity, MainActivity.class)); // IMPROVE
                                            Toast.makeText(activity, "Item uploaded successfully!", Toast.LENGTH_LONG).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            Toast.makeText(activity, "Error: failed to upload file " + sharedItem.name, Toast.LENGTH_LONG).show();
                                        }
                                    });

                        }
                        else {
                            Toast.makeText(activity, "Error: failed to sign-in", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
