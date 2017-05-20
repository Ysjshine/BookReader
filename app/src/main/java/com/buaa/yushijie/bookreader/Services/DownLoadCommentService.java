package com.buaa.yushijie.bookreader.Services;

import android.app.Service;

import com.buaa.yushijie.bookreader.JavaBean.Comment;

import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import bean.BookBean;
import bean.CommentBean;
import bean.UserBean;

/**
 * Created by yushijie on 17-5-19.
 */

public class DownLoadCommentService {
    private static final String URL_DOWNLOAD_COMMENT_INFO="";
    private static final String URL_GET_USER_INFO_BY_ID="";

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
        ois.close();
        if(conn!=null) conn.disconnect();
        return commentBeanArrayList;
    }

    public UserBean getUserInfoById(int userId) throws Exception{
        UserBean res = null;
        connectToServer(URL_GET_USER_INFO_BY_ID);
        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
        String info = "uid="+userId;
        dos.writeBytes(info);
        dos.flush();
        dos.close();

        ObjectInputStream ois = new ObjectInputStream(conn.getInputStream());
        UserBean ub;
        while((ub = (UserBean)ois.readObject())!= null){
            res = ub;
        }
        ois.close();
        if(conn!=null) conn.disconnect();
        return res;
    }

}
