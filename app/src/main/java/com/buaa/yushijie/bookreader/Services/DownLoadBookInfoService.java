package com.buaa.yushijie.bookreader.Services;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import bean.BookBean;

/**
 * Created by yushijie on 17-5-5.
 */

public class DownLoadBookInfoService {
    private static final String TAG = "DownLoadBookInfoService";
    private static final String GETBOOKURL = "http://120.25.89.166/BookReaderServer/QueryBook?bookName=";
    public List<BookBean> getBookInfo(String query){
        List<BookBean> res = new ArrayList<>();
        HttpURLConnection conn = null;
        try{
            URL url = new URL(GETBOOKURL+java.net.URLEncoder.encode(query,"UTF-8"));
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(3000);
            conn.setRequestMethod("POST");
            conn.connect();

            InputStream in = conn.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(in);
            BookBean b = null;
            while((b = (BookBean) ois.readObject()) != null){
                res.add(b);
            }
            Log.e("TAG", "getBookInfo: "+res.size() );
            in.close();
            ois.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(conn != null) conn.disconnect();
        }
        return res;
    }

    public Uri getImageURI(String path,File cache){
        try{
            String name = "cover_"+path.substring(path.indexOf("id=")+3);
            File file = new File(cache,name);
            if(file.exists()){
               return Uri.fromFile(file);
            }else{
                URL url = new URL(path);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");
                conn.connect();

                InputStream in = conn.getInputStream();
                BufferedInputStream bin = new BufferedInputStream(in);

                Bitmap bp = BitmapFactory.decodeStream(bin);
                FileOutputStream fos = new FileOutputStream(file);
                BufferedOutputStream bitBins = new BufferedOutputStream(fos);
                bp.compress(Bitmap.CompressFormat.JPEG,100,bitBins);
                bitBins.flush();
                fos.flush();


                in.close();
                bin.close();
                fos.close();
                if(conn != null) conn.disconnect();
                return Uri.fromFile(file);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
