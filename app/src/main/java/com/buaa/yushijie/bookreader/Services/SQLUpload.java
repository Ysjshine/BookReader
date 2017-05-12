package com.buaa.yushijie.bookreader.Services;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by yushijie on 17-5-11.
 */

public class SQLUpload {

    private static final String SEND_CATEGORY = "";
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

    public static void sendCategoryString(String category){
        try{
            connectToServer(SEND_CATEGORY);

            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            String categorys = "category="+category;
            dos.writeBytes(categorys);
            dos.flush();
            dos.close();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(conn != null) conn.disconnect();
        }
    }
}
