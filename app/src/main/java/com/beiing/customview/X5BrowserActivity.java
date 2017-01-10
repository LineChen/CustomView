package com.beiing.customview;

import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.BitmapDecoder;
import com.tencent.smtt.sdk.WebView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class X5BrowserActivity extends AppCompatActivity {
    WebView webView;
    ImageView ivAsset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_x5_browser);
        webView = (WebView) findViewById(R.id.forum_context);
        ivAsset = (ImageView) findViewById(R.id.iv_asset);
        //http://m.huodongjia.com/event-1314268412.html
        //https://www.yayi365.cn/
        webView.loadUrl("http://m.huodongjia.com/event-1314268412.html");

        try {
//            InputStream stream = getAssets().open("index0.jpg");
//            ivAsset.setImageBitmap(BitmapFactory.decodeStream(stream));
//           Uri uri =  Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.index0);
//            ivAsset.setImageURI(uri);
//            InputStreamReader inputReader = new InputStreamReader( getResources().getAssets().open("index0.jpg") );

            //android.resource://com.beiing.customview/2131165184
            Uri uri =  Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.index0);
            String file_name = uri.getPath().substring(1, uri.getPath().length());
            Log.e("====", "file_name:" + file_name);
            ContentResolver resolver = getContentResolver();
            AssetFileDescriptor assetFileDescriptor = resolver.openAssetFileDescriptor(uri, "r");
            FileInputStream inputStream = assetFileDescriptor.createInputStream();
            ivAsset.setImageBitmap(BitmapFactory.decodeStream(inputStream));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     *  Android把res/raw的资源转化为Uri形式访问(android.resource://)
     Andorid应用会在打包成Apk时把应用中使用的资源文件都打包进去了，尤其是我们熟悉的assets和res文件夹里面存放的资源文件， 一般情况下我们可以直接使用AssetManager类访问Apk下的assets目录，而对于res目录下的资源，我们很少直接使用他们，基本上都是通过它们的id在代码中使用。那么是否可以直接访问APK压缩包中Res目录下的内容呢? 比如需要访问res/raw这样的文件夹?  如果我们想访问res/raw/sample.png文件，可以使用android.resource://package_name/" + R.raw.sample.png这种格式来获取对应的Uri（其中package_name是应用的包名），有了这个Uri那么一切都好办了。
     完整的处理代码为
     Uri uri = Uri.parse("android.resource://package_name/raw/sample.png");
     这样就可以通过Uri来使用apk中res/raw目录下的文件了。
     */


}
