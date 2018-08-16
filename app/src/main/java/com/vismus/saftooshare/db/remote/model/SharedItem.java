package com.vismus.saftooshare.db.remote.model;

import android.content.Context;
import android.net.Uri;

import com.vismus.saftooshare.Utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SharedItem {

    public String name;
    public long size;
    public String sharingDate;
    public boolean seenByAddresee;

    public SharedItem(Context context, Uri uri){
        String filePath = Utils.getFilePathFromUri(context, uri);
        String fileExt = Utils.getFileExtension(filePath);
        File file = new File(filePath);
        this.name = Utils.generateRandomIdentifier() + "." + Utils.getFileExtension(fileExt);
        this.size = file.length();
        this.sharingDate = new SimpleDateFormat("ddMMyy_HHmmss").format(new Date());
        this.seenByAddresee = false;
    }

}
