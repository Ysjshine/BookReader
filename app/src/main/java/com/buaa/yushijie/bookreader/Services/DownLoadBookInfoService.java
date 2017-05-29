package com.buaa.yushijie.bookreader.Services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import bean.BookBean;
import bean.UserBean;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;

/**
 * Created by yushijie on 17-5-5.
 */

public class DownLoadBookInfoService {
    private static final String TAG = "DownLoadBookInfoService";
    private static final String URL_GET_BOOK = "http://120.25.89.166/BookReaderServer/QueryBook?bookName=";
    private static final String URL_GET_USER_INFO="http://120.25.89.166/BookReaderServer/SendUserInfo?username=";
    private static final String URL_GET_BOOK_BY_CID = "http://120.25.89.166/BookReaderServer/QueryTypeBook?type=";
    private static final String URL_GET_PROCESS_INFO="http://120.25.89.166/BookReaderServer/QueryPos";
    private static final String URL_GET_BOOK_BY_CHPTER="http://120.25.89.166/BookReaderServer/DownloadChapter";

    HttpURLConnection conn = null;
    URL url = null;

    private void connectToServer(String path) throws Exception{
        url = new URL(path);
        conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        conn.setConnectTimeout(5000);
        conn.setDoOutput(true);
        conn.connect();
    }

    public List<BookBean> getBookInfo(String query){
        List<BookBean> res = new ArrayList<>();
        try{
            connectToServer(URL_GET_BOOK+EncodeAndDecode.encodeString(query));

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

    public List<BookBean> getBookInfoByCategroyID(int id){
        List<BookBean> res = new ArrayList<>();
        try{
            connectToServer(URL_GET_BOOK_BY_CID+id);

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
                connectToServer(path);

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


    public UserBean getUserInfo(String username){
        UserBean res = null;
        try{
            connectToServer(URL_GET_USER_INFO+java.net.URLEncoder.encode(username,"UTF-8"));
            ObjectInputStream ois = new ObjectInputStream(conn.getInputStream());
            UserBean ub;
            while((ub=(UserBean) ois.readObject())!=null){
                res = ub;
            }
            ois.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(conn!=null)conn.disconnect();
        }
        return res;
    }

    public Book getEPUBBook(String path, Context context) throws Exception{
        connectToServer(path);
        String name = "book_"+path.substring(path.indexOf("id=")+3);
        BufferedInputStream bin = new BufferedInputStream(conn.getInputStream());
        byte[] buffer = new byte[1024*1024];
        File file = new File(context.getCacheDir(),name);
        if(!file.exists()) {
            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            int realReadNum = 0;
            while ((realReadNum = bin.read(buffer)) >= 0) {
                bos.write(buffer, 0, realReadNum);
                Log.e(TAG, "getEPUBBook: " + "ok");
            }
            bos.close();
        }
        bin.close();
         Book res = new EpubReader().readEpub(new FileInputStream(file));
        Log.e(TAG, "getEPUBBook: "+res.getTitle());
        return res;
    }

    public ArrayList<Integer> getProcess(UserBean userBean,BookBean bookBean) throws Exception{
        connectToServer(URL_GET_PROCESS_INFO);

        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
        String info = "uid="+userBean.UserID+"&"
                +"bid="+bookBean.BookID;
        dos.writeBytes(info);
        dos.flush();dos.close();
        ObjectInputStream ois = new ObjectInputStream(conn.getInputStream());
        Integer ans;
        ArrayList<Integer> res = new ArrayList<>();
        while((ans = (Integer) ois.readObject()) !=null){
            res.add(ans);
        }
        ois.close();
        Log.e(TAG, "getProcess: "+res.size() );
        if(conn!=null)conn.disconnect();
        return res;
    }

    public String getEpubBookByChapter(BookBean book,int chapter) throws Exception{
        connectToServer(URL_GET_BOOK_BY_CHPTER);
        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
        String info = "bid="+book.BookID+"&"
                +"chapter="+chapter;
        dos.writeBytes(info);
        dos.flush();dos.close();
        BufferedReader bf = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String sa = null;
        while((sa  = bf.readLine())!=null){
            sb.append(sa);
        }
        bf.close();
        if(conn!=null)conn.disconnect();
        return sb.toString();
    }

}
