package com.vismus.saftooshare.data;


import android.arch.persistence.room.TypeConverter;
import com.vismus.saftooshare.data.SharedMediaItem.SharingStatus;

public class SharingStatusTypeConverter {

    @TypeConverter
    public static SharingStatus toSharingStatus(Integer value) {
        return SharingStatus.values()[value];
    }

    @TypeConverter
    public static Integer toInteger(SharingStatus sharingStatus) {
        return sharingStatus == null ? null : sharingStatus.ordinal();
    }

}
