package com.vismus.saftooshare.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Database(entities = {SharedMediaItem.class}, version = 1)
@TypeConverters({SharingStatusTypeConverter.class})
public abstract class SharedMediaItemsDatabase extends RoomDatabase{

    public static final String DATABASE_NAME = "SharedMediaItems";

    private static SharedMediaItemsDatabase INSTANCE;

    public abstract SharedMediaItemDao sharedMediaItemDao();

    public static SharedMediaItemsDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    SharedMediaItemsDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }

    public void validate(){
        List<SharedMediaItem> items = sharedMediaItemDao().getAll();
        List<SharedMediaItem> validItems = new ArrayList<>();
        for(SharedMediaItem item : items) {
            File itemFile = new File(item.getFilePath());
            if (itemFile.exists()) {
                validItems.add(item);
            }
        }
        sharedMediaItemDao().deleteAll();
        for(SharedMediaItem item : validItems) {
            sharedMediaItemDao().insertAll(item);
        }
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
