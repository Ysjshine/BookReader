package com.buaa.yushijie.bookreader.Services;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import bean.UserBean;

/**
 * Created by yushijie on 17-5-11.
 */

public class SQLUpload {

    private static final String URL_SEND_CATEGORY = "";
    private static final String URL_SEND_NEW_NICKNAME="http://120.25.89.166/BookReaderServer/UserInfo";
    private static final String URL_SEND_NEW_PASSWORD="http://120.25.89.166/BookReaderServer/UserInfo";
    private static final String URL_SEND_COMMENT="";

    private static HttpURLConnection conn = null;
    private static URL url = null;

    private static void connectToServer(String path) throws Exception{
        url = new URL(path);
        conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        conn.setConnectTimeout(5000);
        conn.setDoOutput(true);
        conn.connect();

    }


    //send user category;
    public static void sendCategoryString(String category) throws Exception{
        connectToServer(URL_SEND_CATEGORY);
        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
        String categorys = "category="+category;
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
                +"newNickname="+newNickname;
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

    public static void sendComment(UserBean ub,String comment) throws Exception{
        connectToServer(URL_SEND_COMMENT);
        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
        String info = "username="+ub.account+"&"
                +"comment="+comment;
        dos.writeBytes(info);
        dos.flush();
        dos.close();
        conn.getResponseCode();
        if(conn!=null)conn.disconnect();
    }
}
