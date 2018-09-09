//package com.vismus.saftooshare.data;
//
//import android.arch.persistence.room.Dao;
//import android.arch.persistence.room.Delete;
//import android.arch.persistence.room.Insert;
//import android.arch.persistence.room.OnConflictStrategy;
//import android.arch.persistence.room.Query;
//
//import java.util.List;
//
//@Dao
//public interface SharedItemDao {
//
//    @Query("SELECT * FROM SharedItem")
//    List<Local.SharedItem> getAll();
//
//    @Query("SELECT * FROM SharedItem WHERE id = :id")
//    Local.SharedItem getSharedItemById(int id);
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    void insertAll(Local.SharedItem... sharedItem);
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    void insert(Local.SharedItem sharedItem);
//
//    @Delete
//    void delete(Local.SharedItem sharedItem);
//
//    @Query("DELETE FROM SharedItem")
//    void deleteAll();
//
//}
