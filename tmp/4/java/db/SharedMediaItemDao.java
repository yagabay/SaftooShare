package com.vismus.saftooshare.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface SharedMediaItemDao {

    @Query("SELECT * FROM SharedMediaItem")
    List<SharedMediaItem> getAll();

    @Query("SELECT * FROM SharedMediaItem WHERE _id = :id")
    SharedMediaItem getSharedMediaItemById(String path);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(SharedMediaItem... sharedSharedMediaItem);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SharedMediaItem sharedSharedMediaItem);

    @Delete
    void delete(SharedMediaItem sharedSharedMediaItem);

    @Query("DELETE FROM SharedMediaItem")
    void deleteAll();

}
