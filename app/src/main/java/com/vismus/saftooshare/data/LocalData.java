package com.vismus.saftooshare.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.vismus.saftooshare.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LocalData {

    public static Storage storage() {
        return Storage.instance();
    }

    public static Database database(Context context) {
        return Database.instance(context);
    }

    public void add(){

    }

    /* storage */

    public static class Storage {

        static Storage _instance = null;

        static Storage instance() {
            if (_instance == null) {
                _instance = new Storage();
            }
            return _instance;
        }

        public boolean storeSharedItem(String srcPath, String dstPath) {
            File srcFile = new File(srcPath);
            File dstFile = new File(dstPath);
            try {
                Utils.copy(srcFile, dstFile);
            } catch (IOException e) {
                return false;
            }
            return true;
        }
    }

    /* database */

    @android.arch.persistence.room.Database(entities = {SharedItem.class}, version = 1)
    @TypeConverters({Database.SharingStatusTypeConverter.class, Database.DateTypeConverter.class})
    public abstract static class Database extends RoomDatabase {

        static Database _instance = null;

        static Database instance(Context context) {
            if (_instance == null) {
                _instance = Room.databaseBuilder(context.getApplicationContext(),
                        Database.class, DATABASE_NAME)
                        .allowMainThreadQueries()
                        .build();
            }
            return _instance;
        }

        static final String DATABASE_NAME = "SharedItems";

        Database() {}

        abstract SharedItemDao sharedItemDao();

        public void insertSharedItem(String filePath, Date sharingDate) {
            sharedItemDao().insert(new SharedItem(filePath, sharingDate));
        }

        public List<SharedItem> getSharedItems() {
            validate();
            return sharedItemDao().getAll();
        }

        void validate() {
            List<SharedItem> items = sharedItemDao().getAll();
            List<SharedItem> validItems = new ArrayList<>();
            for (SharedItem item : items) {
                File file = new File(item.filePath);
                if (file.exists()) {
                    validItems.add(item);
                }
            }
            sharedItemDao().deleteAll();
            for (SharedItem item : validItems) {
                sharedItemDao().insertAll(item);
            }
        }

        @Dao
        public interface SharedItemDao {

            @Query("SELECT * FROM SharedItem")
            List<SharedItem> getAll();

            @Query("SELECT * FROM SharedItem WHERE id = :id")
            SharedItem getSharedItemById(int id);

            @Insert(onConflict = OnConflictStrategy.REPLACE)
            void insertAll(SharedItem... sharedItem);

            @Insert(onConflict = OnConflictStrategy.REPLACE)
            void insert(SharedItem sharedItem);

            @Delete
            void delete(SharedItem sharedItem);

            @Query("DELETE FROM SharedItem")
            void deleteAll();
        }

        static class DateTypeConverter {

            @TypeConverter
            public Date toDate(Long value) {
                return value == null ? null : new Date(value);
            }

            @TypeConverter
            public Long toLong(Date date) {
                return date == null ? null : date.getTime();
            }
        }

        public static class SharingStatusTypeConverter {

            @TypeConverter
            public SharedItem.SharingStatus toSharingStatus(Integer value) {
                return SharedItem.SharingStatus.values()[value];
            }

            @TypeConverter
            public Integer toInteger(SharedItem.SharingStatus sharingStatus) {
                return sharingStatus == null ? null : sharingStatus.ordinal();
            }

        }
    }

    @Entity
    public static class SharedItem {

        public enum SharingStatus {
            SENT,
            RECEIVED,
            SEEN
        }

        @PrimaryKey(autoGenerate = true)
        public int id;

        public String filePath;
        public Date sharingDate;
        public SharedItem.SharingStatus sharingStatus;

        public SharedItem(String filePath, Date sharingDate) {
            this.filePath = filePath;
            this.sharingDate = sharingDate;
            this.sharingStatus = SharedItem.SharingStatus.SENT;
        }

    }

}
