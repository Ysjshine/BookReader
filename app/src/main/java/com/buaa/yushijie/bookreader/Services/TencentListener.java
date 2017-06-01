package com.buaa.yushijie.bookreader.Services;

import android.app.Activity;
import android.widget.Toast;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

/**
 * Created by yushijie on 17-6-1.
 */

public class TencentListener implements IUiListener{
    private Activity currentActivity;
    public TencentListener(Activity currentActivity){
        this.currentActivity = currentActivity;
    }
    @Override
    public void onComplete(Object o) {
        Toast.makeText(currentActivity,"分享成功",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(UiError uiError) {
        Toast.makeText(currentActivity,"系统繁忙，请稍后再试",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancel() {
    }
}
