//package com.vismus.saftooshare.data;
//
//import android.arch.persistence.room.TypeConverter;
//
//public class SharingStatusTypeConverter {
//
//    @TypeConverter
//    public static Local.Database.SharedItem.SharingStatus toSharingStatus(Integer value) {
//        return Local.Database.SharedItem.SharingStatus.values()[value];
//    }
//
//    @TypeConverter
//    public static Integer toInteger(Local.Database.SharedItem.SharingStatus sharingStatus) {
//        return sharingStatus == null ? null : sharingStatus.ordinal();
//    }
//
//}
