package com.buaa.yushijie.bookreader.Services;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import bean.UserBean;

/**
 * Created by yushijie on 17-5-11.
 */

public class SQLUpload {

    private static final String SEND_CATEGORY = "";
    private static final String SEND_NEW_NICKNAME="";
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
        connectToServer(SEND_CATEGORY);
        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
        String categorys = "category="+category;
        dos.writeBytes(categorys);
        dos.flush();
        dos.close();
        if(conn!=null)conn.disconnect();
    }

    //send user new nickname;
    public static void senNewNickNameString(UserBean ub, String newNickname) throws Exception{
        connectToServer(SEND_NEW_NICKNAME);
        conn.setDoInput(true);
        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
        String info = "uid="+ub.UserID+"&"
                +"nickname="+newNickname;
        dos.writeBytes(info);
        dos.flush();
        dos.close();
//
//        ObjectInputStream ois = new ObjectInputStream(conn.getInputStream());
//        UserBean ubs = null;
//        while((ubs = (UserBean)ois.readObject())!=null){
//            ub = ubs;
//        }
//        ois.close();

        if(conn!=null) conn.disconnect();
    }
}
