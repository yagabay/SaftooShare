package com.vismus.saftooshare.data;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RemoteData {

    public static Auth auth() {
        return Auth.instance();
    }

    public static Storage storage() {
        return Storage.instance();
    }

    public static Database database() {
        return Database.instance();
    }

    /* authentication */

    public static class Auth {

        static Auth _instance = null;

        public static Auth instance() {
            if (_instance == null) {
                _instance = new Auth();
            }
            return _instance;
        }

        OnAuthCompleteListener _onAuthCompleteListener;

        public void setOnAuthCompleteListener(OnAuthCompleteListener listener) {
            _onAuthCompleteListener = listener;
        }

        public void authenticate(String username, String password) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (_onAuthCompleteListener != null) {
                                _onAuthCompleteListener.onAuthComplete(task.isSuccessful());
                            }
                        }
                    });
        }

        public interface OnAuthCompleteListener {
            void onAuthComplete(boolean isSuccessful);
        }

    }

    /* storage */

    public static class Storage {

        static Storage _instance = null;

        public static Storage instance() {
            if (_instance == null) {
                _instance = new Storage();
            }
            return _instance;
        }

        OnPutItemCompleteListener _onPutItemCompleteListener;

        public void setOnPutItemCompleteListener(OnPutItemCompleteListener listener) {
            _onPutItemCompleteListener = listener;
        }

        public void putSharedItem(String name, Uri uri) {
            FirebaseStorage.getInstance().getReference().child(name).putFile(uri)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            _onPutItemCompleteListener.onPutItemComplete(task.isSuccessful());
                        }
                    });
        }

        public interface OnPutItemCompleteListener {
            void onPutItemComplete(boolean isSuccessful);
        }

    }

    /* database */

    public static class Database {

        static Database _instance = null;

        public static Database instance() {
            if (_instance == null) {
                _instance = new Database();
            }
            return _instance;
        }

        static final String PATH_SHARED_ITEMS = "shared_items";

        OnAddItemCompleteListener _onAddItemCompleteListener;

        public void setOnAddItemCompleteListener(OnAddItemCompleteListener listener) {
            _onAddItemCompleteListener = listener;
        }

        public void addSharedItem(String name, long size, Date sharingDate) {
            SharedItem item = new SharedItem(name, size, sharingDate);
            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference itemRef = dbRef.child(PATH_SHARED_ITEMS).push();
            dbRef.child(PATH_SHARED_ITEMS).child(itemRef.getKey()).setValue(item)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            _onAddItemCompleteListener.onAddItemComplete(task.isSuccessful());
                        }
                    });
        }

        public interface OnAddItemCompleteListener {
            void onAddItemComplete(boolean isSuccessful);
        }

    }

    /* model */

    public static class SharedItem {

        public String name;
        public long size;
        public String sharingDate;

        public SharedItem(String name, long size, Date sharingDate) {
            this.name = name;
            this.size = size;
            this.sharingDate = new SimpleDateFormat("ddMMyy_HHmmss").format(sharingDate);
        }

    }

}
