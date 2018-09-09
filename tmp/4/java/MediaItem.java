package com.vismus.saftooshare;

public class MediaItem {

    String _filePath;

    public MediaItem(String filePath){
        _filePath = filePath;
    }

    String getFilePath(){
        return _filePath;
    }

}
