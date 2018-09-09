//package com.vismus.saftooshare.data;
//
//import android.arch.persistence.room.Entity;
//import android.arch.persistence.room.PrimaryKey;
//import android.arch.persistence.room.Room;
//import android.arch.persistence.room.RoomDatabase;
//import android.arch.persistence.room.TypeConverters;
//import android.content.Context;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//@android.arch.persistence.room.Database(entities = {Local.SharedItem.class}, version = 1)
//@TypeConverters({SharingStatusTypeConverter.class, DateTypeConverter.class})
//public abstract class LocalDatabase extends RoomDatabase{
//
//    static LocalDatabase _instance;
//
//    public static LocalDatabase getInstance(Context context) {
//        if (_instance == null) {
//            _instance = Room.databaseBuilder(context.getApplicationContext(),
//                    LocalDatabase.class, DATABASE_NAME)
//                    .allowMainThreadQueries()
//                    .build();
//        }
//        return _instance;
//    }
//
//    static final String DATABASE_NAME = "SharedItems";
//    public abstract SharedItemDao sharedItemDao();
//
//    public void insertSharedItem(Local.SharedItem item){
//        sharedItemDao().insert(item);
//    }
//
//    public List<Local.SharedItem> getSharedItems(){
//        validate();
//        return sharedItemDao().getAll();
//    }
//
//    void validate(){
//        List<Local.SharedItem> items = sharedItemDao().getAll();
//        List<Local.SharedItem> validItems = new ArrayList<>();
//        for(Local.SharedItem item : items) {
//            File file = new File(item.filePath);
//            if (file.exists()) {
//                validItems.add(item);
//            }
//        }
//        sharedItemDao().deleteAll();
//        for(Local.SharedItem item : validItems) {
//            sharedItemDao().insertAll(item);
//        }
//    }
//
//    @Entity
//    public static class SharedItem {
//
//        public enum SharingStatus {
//            SENT,
//            RECEIVED,
//            SEEN
//        }
//
//        @PrimaryKey(autoGenerate = true)
//        public int id;
//
//        public String filePath;
//        public Date sharingDate;
//        public SharingStatus sharingStatus;
//
//        public SharedItem(String filePath){
//            this.filePath = filePath;
//            this.sharingDate = new Date();
//            this.sharingStatus = SharingStatus.SENT;
//        }
//
//    }
//
//}
