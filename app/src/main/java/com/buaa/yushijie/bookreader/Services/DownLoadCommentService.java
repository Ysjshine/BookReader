package com.buaa.yushijie.bookreader.Services;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import bean.BookBean;
import bean.CommentBean;

/**
 * Created by yushijie on 17-5-19.
 */

public class DownLoadCommentService {
    private static final String URL_DOWNLOAD_COMMENT_INFO="http://120.25.89.166/BookReaderServer/QueryComment";

    private BookBean currentBook;
    private HttpURLConnection conn = null;
    private URL url = null;

    public DownLoadCommentService(BookBean currentBook){
        this.currentBook = currentBook;
    }

    private void connectToServer(String path) throws Exception{
        url = new URL(path);
        conn = (HttpURLConnection)url.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.connect();
    }

    public ArrayList<CommentBean> getCommentInfo() throws Exception{
        ArrayList<CommentBean> commentBeanArrayList = new ArrayList<>();
        connectToServer(URL_DOWNLOAD_COMMENT_INFO);
        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
        String info = "bid="+currentBook.BookID;
        dos.writeBytes(info);
        dos.flush();
        dos.close();
        ObjectInputStream ois = new ObjectInputStream(conn.getInputStream());
        CommentBean cb;
        while((cb=(CommentBean)ois.readObject())!=null){
            commentBeanArrayList.add(cb);
        }

        Log.e("ww", "getCommentInfo: "+commentBeanArrayList.size() );
        ois.close();
        if(conn!=null) conn.disconnect();
        return commentBeanArrayList;
    }


}
