package com.vismus.saftooshare.data;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

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

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
