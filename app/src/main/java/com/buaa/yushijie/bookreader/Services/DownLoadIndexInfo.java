package com.buaa.yushijie.bookreader.Services;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import bean.BookBean;

/**
 * Created by yushijie on 17-5-25.
 */

public class DownLoadIndexInfo {
    private static final String URL_GET_INDEX_INFO=CurrentApplication.getUrlServer()+"/BookReaderServer/QueryTopBook";
    private static final String URL_GET_RES_INFO=CurrentApplication.getUrlServer()+"/BookReaderServer/GetTopImageSrc";
    private static final String URL_GET_NEWS_PIC_INFO=CurrentApplication.getUrlServer()+"/BookReaderServer/GetNewsImageSrc";
    private static final String TAG="DownLoadIndexInfo";
    HttpURLConnection conn = null;
    URL url = null;
    public ArrayList<BookBean> getTop3Book() throws Exception{
        url = new URL(URL_GET_INDEX_INFO);
        conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setConnectTimeout(5000);
        conn.connect();

        ObjectInputStream ois = new ObjectInputStream(conn.getInputStream());
        ArrayList<BookBean> res = new ArrayList<>();
        BookBean book;
        while((book=(BookBean)ois.readObject()) != null){
            res.add(book);
        }
        ois.close();
        Log.e(TAG, "getTop3Book: "+res.size());
        if(conn!=null)conn.disconnect();
        return res;
    }

    public ArrayList<String> getURL(boolean isNews) throws Exception{
        if(!isNews) {
            url = new URL(URL_GET_RES_INFO);
        }else {
            url = new URL(URL_GET_NEWS_PIC_INFO);
        }
        conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setConnectTimeout(5000);
        conn.connect();

        ObjectInputStream ois = new ObjectInputStream(conn.getInputStream());
        ArrayList<String> res = new ArrayList<>();
        String s;
        while((s=(String)ois.readObject()) != null){
            res.add(s);
        }
        Log.e(TAG, "getURL: "+res.size() );
        ois.close();
        if(conn!=null)conn.disconnect();
        return res;
    }
}
