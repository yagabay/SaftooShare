//package com.vismus.saftooshare.data;
//
//import android.support.annotation.NonNull;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.vismus.saftooshare.Utils;
//
//import java.io.File;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//public class RemoteDatabase {
//
//    static RemoteDatabase _instance = null;
//
//    public static RemoteDatabase getInstance(){
//        if(_instance == null){
//            _instance = new RemoteDatabase();
//        }
//        return _instance;
//    }
//
//    static final String PATH_SHARED_ITEMS = "shared_items";
//    static FirebaseDatabase _database = FirebaseDatabase.getInstance();
//
//    OnAddItemCompleteListener _onAddItemCompleteListener;
//
//    public void setOnAddItemCompleteListener(OnAddItemCompleteListener listener){
//        _onAddItemCompleteListener = listener;
//    }
//
//    public void addSharedItem(Remote.SharedItem item){
//        DatabaseReference dbRef = _database.getReference();
//        DatabaseReference itemRef = dbRef.child(PATH_SHARED_ITEMS).push();
//        dbRef.child(PATH_SHARED_ITEMS).child(itemRef.getKey()).setValue(item)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        _onAddItemCompleteListener.onAddItemComplete(task.isSuccessful());
//                    }
//                });
//    }
//
//    public interface OnAddItemCompleteListener {
//        void onAddItemComplete(boolean isSuccessful);
//    }
//
//    public static class SharedItem {
//
//        static public String name;
//        public long size;
//        public String date;
//
//        public SharedItem(String filePath){
//            this.name = Utils.generateRandomIdentifier() + "." + Utils.getFileExtension(filePath);
//            this.size = new File(filePath).length();
//            this.date = new SimpleDateFormat("ddMMyy_HHmmss").format(new Date());
//        }
//
//    }
//
//}
