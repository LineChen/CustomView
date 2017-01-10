package com.beiing.customview;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by chenliu on 2017/1/10.<br/>
 * 描述：
 * </br>
 */

public class AssetsProvider extends ContentProvider {


//    @Nullable
//    @Override
//    public AssetFileDescriptor openAssetFile(Uri uri, String mode) throws FileNotFoundException {
//        AssetManager am = getContext( ).getAssets( );
//
//        String file_name = uri.getPath().substring(1, uri.getPath().length());
//        Log.e("====", "file_name:" + file_name);
//        //String file_name = uri.getLastPathSegment();
//        // Neither of the two lines above work for me
//
//        AssetFileDescriptor afd = null;
//
//        try {
//            afd = am.openFd( file_name );
//        }
//        catch(IOException e)
//        {
//            e.printStackTrace( );
//        }
//
//        return afd;//super.openAssetFile(uri, mode);
//    }

    @Override
    public boolean onCreate() {
        return false;
    }


    private final static int FILE_NAME = 1;
    private final static String AUTHORITY = "com.beiing.customview.assetsprovider";
    private static final UriMatcher sMatcher;
    static {
        sMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sMatcher.addURI(AUTHORITY, "file/*", FILE_NAME);
    }

    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode)
            throws FileNotFoundException {
        // 另一个程序使用如下代码获得流InputStream is = resolver.openInputStream(uri);
        ParcelFileDescriptor pfd = null;
        switch (sMatcher.match(uri)) {
            case FILE_NAME:
                String filename = uri.getPathSegments().get(1);
                File file = new File(getContext().getFilesDir(), filename);
                if (!file.exists()) {
                    try {
                        InputStream in = getContext().getAssets().open(filename);
                        BufferedInputStream bis = new BufferedInputStream(in);
                        FileOutputStream fos = new FileOutputStream(file);
                        int len = 0;
                        byte[] b = new byte[1024];
                        while ((len = bis.read(b)) != -1) {
                            fos.write(b, 0, len);
                        }
                        fos.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                pfd = ParcelFileDescriptor.open(file,
                        ParcelFileDescriptor.MODE_READ_ONLY);
                file.delete();
                break;
            default:
                break;
        }
        return pfd;
    }

    @Override
    public Bundle call(String method, String arg, Bundle extras) {
        Bundle ret_extras = new Bundle();
        if ("getIn".equals(method)) {
            try {
                ret_extras = getIn(extras);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret_extras;
    }

    public Bundle getIn(Bundle extras) throws IOException {
        Bundle ret_extras = new Bundle();
        String filename = extras.getString("filename", "");
        try {
            InputStream in = getContext().getAssets().open(filename);
            BufferedInputStream bis = new BufferedInputStream(in);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buff = new byte[1024];
            int len = 0;
            while ((len = bis.read(buff)) != -1) {
                baos.write(buff, 0, len);
            }
            byte[] b = baos.toByteArray();
            ret_extras.putByteArray("myBundle", b);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret_extras;
    }


    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
