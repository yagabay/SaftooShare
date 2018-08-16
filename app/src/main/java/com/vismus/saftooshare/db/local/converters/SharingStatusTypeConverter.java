package com.vismus.saftooshare.db.local.converters;

import android.arch.persistence.room.TypeConverter;

import com.vismus.saftooshare.db.local.model.SharedMediaItem;

public class SharingStatusTypeConverter {

    @TypeConverter
    public static SharedMediaItem.SharingStatus toSharingStatus(Integer value) {
        return SharedMediaItem.SharingStatus.values()[value];
    }

    @TypeConverter
    public static Integer toInteger(SharedMediaItem.SharingStatus sharingStatus) {
        return sharingStatus == null ? null : sharingStatus.ordinal();
    }

}
