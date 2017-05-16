package com.buaa.yushijie.bookreader.Services;

/**
 * Created by yushijie on 17-5-16.
 */

public class EncodeAndDecode {

    public static String encodeString(String s){
        String res = null;
        try {
            res = java.net.URLEncoder.encode(s, "UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

    public static String decodeString(String s){
        String res = null;
        try {
            res = java.net.URLDecoder.decode(s, "UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }
}
