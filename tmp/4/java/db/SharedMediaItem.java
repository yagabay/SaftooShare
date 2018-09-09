package com.vismus.saftooshare.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class SharedMediaItem {

    public enum SharingStatus{
        SENT,
        RECEIVED,
        SEEN
    }

    @PrimaryKey(autoGenerate = true)
    int _id;

    String _filePath;

    SharingStatus _sharingStatus;

    public SharedMediaItem(String filePath){
        _filePath = filePath;
        _sharingStatus = SharingStatus.SENT;
    }

    public int getId() { return _id; }

    public void setId(int id) {
        _id = id;
    }

    public void setFilePath(String filePath){
        _filePath = filePath;
    }

    public String getFilePath(){
        return _filePath;
    }

    public void setSharingStatus(SharingStatus status){
        _sharingStatus = status;
    }

    public SharingStatus getSharingStatus(){
        return _sharingStatus;
    }


}
