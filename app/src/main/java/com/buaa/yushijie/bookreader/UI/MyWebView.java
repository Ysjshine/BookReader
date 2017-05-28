package com.buaa.yushijie.bookreader.UI;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.buaa.yushijie.bookreader.R;

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

    @Override
    public void setOnScrollChangeListener(OnScrollChangeListener l) {
        super.setOnScrollChangeListener(l);
        this.addView(new WebView(super.getContext()));
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
    }

}
