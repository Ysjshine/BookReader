package com.buaa.yushijie.bookreader.Services;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.net.HttpURLConnection;
import java.net.URL;

import bean.BookBean;
import bean.CommentBean;
import bean.UserBean;
import bean.UserCategory;

/**
 * Created by yushijie on 17-5-11.
 */

public class SQLUpload {

    private static final String URL_SEND_CATEGORY = CurrentApplication.getUrlServer()+"/BookReaderServer/AddCategory";
    private static final String URL_SEND_NEW_NICKNAME=CurrentApplication.getUrlServer()+"/BookReaderServer/UpdateUserInfo";
    private static final String URL_SEND_NEW_PASSWORD=CurrentApplication.getUrlServer()+"/BookReaderServer/UpdateUserInfo";
    private static final String URL_SEND_COMMENT=CurrentApplication.getUrlServer()+"/BookReaderServer/AddComment";
    private static final String URL_SEND_DELETING_BOOK_INFO=CurrentApplication.getUrlServer()+"/BookReaderServer/DelCategoryBook";
    private static final String URL_SEND_DELETING_CATEGORY_INFO=CurrentApplication.getUrlServer()+"/BookReaderServer/DelCategory";
    private static final String URL_SEND_COLLECTION_INFO=CurrentApplication.getUrlServer()+"/BookReaderServer/AddCategoryBook";
    private static final String URL_SEND_DELETE_COMMENT_INFO=CurrentApplication.getUrlServer()+"/BookReaderServer/DelComment";
    private static final String URL_SEND_PROCESS_INFO=CurrentApplication.getUrlServer()+"/BookReaderServer/UpdatePos";

    private static HttpURLConnection conn = null;
    private static URL url = null;

    private static void connectToServer(String path) throws Exception{
        url = new URL(path);
        conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        conn.setConnectTimeout(5000);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.connect();

    }


    //send user category;
    public static void sendCategoryString(UserBean ub,String category) throws Exception{
        connectToServer(URL_SEND_CATEGORY);
        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
        String categorys = "uid="+ub.UserID+"&"
                +"categoryName="+EncodeAndDecode.encodeString(category);
        dos.writeBytes(categorys);
        dos.flush();
        dos.close();
        conn.getResponseCode();
        if(conn!=null)conn.disconnect();
    }

    //send user new nickname;
    public static void senNewNickNameString(UserBean ub, String newNickname) throws Exception{

        connectToServer(URL_SEND_NEW_NICKNAME);
        String info = "uid="+ub.UserID.toString()+"&"
                +"newNickname="+EncodeAndDecode.encodeString(newNickname);
        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
        dos.writeBytes(info);
        dos.flush();
        dos.close();
        conn.getResponseCode();
        if(conn!=null) conn.disconnect();

    }

    //send new password
    public static void sendNewPassword(UserBean ub,String newPassword) throws Exception{
        connectToServer(URL_SEND_NEW_PASSWORD);
        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
        String info = "uid="+ub.UserID+"&"
                +"newPassword="+newPassword;
        dos.writeBytes(info);
        dos.flush();
        dos.close();
        conn.getResponseCode();
        if(conn!=null)conn.disconnect();
    }

    //send comment
    public static boolean sendComment(UserBean ub,BookBean bookBean,String comment,Handler handler) throws Exception{
        connectToServer(URL_SEND_COMMENT);
        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
        String info = "uid="+ub.UserID+"&"
                +"bid="+bookBean.BookID+"&"
                +"contents="+EncodeAndDecode.encodeString(comment);
        dos.writeBytes(info);
        dos.flush();
        dos.close();
        boolean res = getReturnInfo(handler);
        if(conn!=null)conn.disconnect();
        return res;
    }

    private static boolean getReturnInfo(Handler handler) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String stp;
        while((stp = br.readLine())!=null){
            sb.append(stp);
        }
        if(sb.toString().equals("1")){
            Message msg = new Message();
            msg.what = 1;
            handler.sendMessage(msg);
            return true;
        }else{
            Message msg = new Message();
            msg.what = 0;
            handler.sendMessage(msg);
            return false;
        }
    }
    //send deleting book info
    public static void sendDeleteCollectionBookInfo(UserBean ub, BookBean bookBean,UserCategory userCategory,
                                                    Handler handler) throws Exception{
        connectToServer(URL_SEND_DELETING_BOOK_INFO);
        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
        String info = "uid="+ub.UserID+"&"
                +"cid="+userCategory.CategoryID+"&"
                +"bid="+bookBean.BookID;
        dos.writeBytes(info);
        dos.flush();
        dos.close();
        getReturnInfo(handler);
        if(conn!=null) conn.disconnect();
    }

    //send deleting category info
    public static void sendDeleteCollectionCategoryInfo(UserBean ub, UserCategory userCategory,
                                                        Handler handler) throws Exception{
        connectToServer(URL_SEND_DELETING_CATEGORY_INFO);
        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
        String info = "uid="+ub.UserID+"&"
                +"cid="+userCategory.CategoryID;
        dos.writeBytes(info);
        dos.flush();
        dos.close();
        getReturnInfo(handler);
        if(conn!=null) conn.disconnect();
    }

    //send collection info
    public static void sendCollectionInfo(UserBean userBean, UserCategory userCategory,
                                          BookBean bookBean,Handler handler) throws Exception{
        connectToServer(URL_SEND_COLLECTION_INFO);
        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
        String info = "uid="+userBean.UserID+"&"
                +"cid="+userCategory.CategoryID+"&"
                +"bid="+bookBean.BookID;
        dos.writeBytes(info);
        dos.flush();
        dos.close();
        getReturnInfo(handler);
        if(conn!=null)conn.disconnect();
    }

    //delete a comment
    public static boolean sendDeleteCommentInfo(UserBean userBean, CommentBean commentBean,
                                             Handler handler) throws Exception{
        connectToServer(URL_SEND_DELETE_COMMENT_INFO);
        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
        String info = "uid="+userBean.UserID+"&"
                +"coid="+commentBean.CommentID;
        dos.writeBytes(info);
        dos.flush();
        dos.close();
        boolean res = getReturnInfo(handler);
        if(conn!=null)conn.disconnect();
        return res;
    }

    //send the process of book
    public static void sendProcessOfBook(UserBean userBean,BookBean bookBean,
                                         int chapter,int page) throws Exception{
        connectToServer(URL_SEND_PROCESS_INFO);
        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
        String info = "uid="+userBean.UserID+"&"
                +"bid="+bookBean.BookID+"&"
                +"chapter="+chapter+"&"
                +"page="+page;
        dos.writeBytes(info);
        dos.flush();
        dos.close();
        conn.getResponseCode();
        if(conn!=null)conn.disconnect();
    }

}
