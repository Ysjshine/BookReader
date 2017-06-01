package com.buaa.yushijie.bookreader.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.LayoutInflaterCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.buaa.yushijie.bookreader.R;
import com.buaa.yushijie.bookreader.Services.AsyncTaskLoadImg;
import com.buaa.yushijie.bookreader.Services.DownLoadBookInfoService;

import java.io.File;

import bean.BookBean;

/**
 * Created by yushijie on 17-6-1.
 */

public class BookDetailCoverDialogFragment extends DialogFragment {
    private ImageView coverImageView;
    private BookBean currentBook;
    private Activity currentActivity;
    private File cache;

    public void setCache(File cache) {
        this.cache = cache;
    }

    public void setCurrentBook(BookBean currentBook) {
        this.currentBook = currentBook;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        currentActivity = getActivity();
        Dialog dialog = new Dialog(currentActivity, R.style.FullScreen);
        View v = LayoutInflater.from(currentActivity)
                .inflate(R.layout.book_detail_cover_picture_dialog_fragment,null);
        coverImageView = (ImageView)v.findViewById(R.id.book_detail_cover_image_big);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.dialogStyle);
        window.getDecorView().setPadding(0, 0, 0, 0);
        android.view.WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        dialog.setContentView(v);
        AsyncTaskLoadImg task = new AsyncTaskLoadImg(new DownLoadBookInfoService(),coverImageView,cache);
        task.execute(currentBook.imgSource);
        return dialog;
    }
}
