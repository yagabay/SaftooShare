//package com.vismus.saftooshare.data;
//
//import android.net.Uri;
//import android.support.annotation.NonNull;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.UploadTask;
//
//public class RemoteStorage {
//
//    static RemoteStorage _instance = null;
//
//    public static RemoteStorage getInstance(){
//        if(_instance == null){
//            _instance = new RemoteStorage();
//        }
//        return _instance;
//    }
//
//    OnPutItemCompleteListener _onPutItemCompleteListener;
//
//    public void setOnPutItemCompleteListener(OnPutItemCompleteListener listener){
//        _onPutItemCompleteListener = listener;
//    }
//
//    public void putSharedItem(String name, Uri uri){
//        FirebaseStorage.getInstance().getReference().child(name).putFile(uri)
//                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                        _onPutItemCompleteListener.onPutItemComplete(task.isSuccessful());
//                    }
//                });
//    }
//
//    public interface OnPutItemCompleteListener {
//        void onPutItemComplete(boolean isSuccessful);
//    }
//}
