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

    private static final String urlAddress = "";
    public static void sendQueryString(String query){
        HttpURLConnection conn = null;
        try{
            URL url = new URL(urlAddress);
            conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(5000);
            conn.setDoOutput(true);
            conn.connect();

            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            String querys = "qurey="+query;
            dos.writeBytes(querys);
            dos.flush();
            dos.close();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(conn != null) conn.disconnect();
        }
    }
}
