package com.buaa.yushijie.bookreader.UI;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by yushijie on 17-5-27.
 */

public class MyWebView extends WebView {
    public MyWebView(Context context) {
        super(context);
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public int getContentWidth(){
        return this.computeHorizontalScrollRange();
    }
}
