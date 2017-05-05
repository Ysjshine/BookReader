package com.buaa.yushijie.bookreader.Services;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedInputStream;
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

    public List<BookBean> getBookInfo(){
        List<BookBean> res = new ArrayList<>();
        HttpURLConnection conn = null;
        try{
            URL url = new URL("http://115.25.144.135:8080/bookBean");
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

            Log.e("sssssssssss", "getBookInfo: "+res.size() );
            in.close();
            ois.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(conn != null) conn.disconnect();
        }
        return res;
    }

    public Bitmap getImageURI(String path,File cache){
        try{

            File file = new File(cache,"111.jpg");
            if(file.exists()){
               // return Uri.fromFile(file);
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
                return bp;
               /* FileOutputStream fos = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = bin.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
                in.close();
                bin.close();
                fos.close();
                if(conn != null) conn.disconnect();
                return Uri.fromFile(file);*/
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
