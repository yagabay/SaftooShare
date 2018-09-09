//package com.vismus.saftooshare.data;
//
//import android.content.Context;
//import android.net.Uri;
//
//import com.vismus.saftooshare.Utils;
//
//import java.io.File;
//import java.io.IOException;
//
//public class LocalStorage {
//
//    static LocalStorage _instance = null;
//
//    public static LocalStorage getInstance(Context context) {
//        if (_instance == null) {
//            _instance = new LocalStorage(context);
//        }
//        return _instance;
//    }
//
//    Context _context;
//
//    LocalStorage(Context context) {
//        _context = context;
//    }
//
//    public String storeSharedItem(String name, Uri uri) {
//        File srcFile = new File(Utils.getFilePathFromUri(_context, uri));
//        File dstFile = new File(Utils.getBasePath() + "/" + name);
//        try {
//            Utils.copy(srcFile, dstFile);
//        } catch (IOException e) {
//            return null;
//        }
//        return dstFile.getPath();
//    }
//}