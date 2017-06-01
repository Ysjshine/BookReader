package com.buaa.yushijie.bookreader.Services;

import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by yushijie on 17-5-6.
 */

public class AsyncTaskLoadImg extends AsyncTask<String,Integer,Uri> {
    private DownLoadBookInfoService service = new DownLoadBookInfoService();
    private ImageView coverImage;
    private File caches;

    public AsyncTaskLoadImg(DownLoadBookInfoService service, ImageView coverImage, File caches) {
        super();
        this.caches = caches;
        this.service = service;
        this.coverImage = coverImage;
    }

    @Override
    protected Uri doInBackground(String... params) {
        try{
            return service.getImageURI(params[0],caches);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Uri uri) {
        super.onPostExecute(uri);
        coverImage.setImageURI(uri);
    }
}
