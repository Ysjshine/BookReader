package com.buaa.yushijie.bookreader.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.buaa.yushijie.bookreader.Services.CurrentApplication;
import com.buaa.yushijie.bookreader.Services.SQLUpload;

import java.util.concurrent.TimeoutException;

import bean.BookBean;
import bean.UserBean;
import bean.UserCategory;

/**
 * Created by yushijie on 17-5-18.
 */

public class MyBookShelfDeleteABookDialogFragment extends DialogFragment {
    private BookBean bookBean;
    private UserBean userBean;
    private UserCategory userCategory;
    private Activity currentActivity;

    private int groupPos;
    private int selectedItemsPos;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
             switch (msg.what){
                 case 0:
                     Toast.makeText(currentActivity,"删除失败,请稍后再试",Toast.LENGTH_SHORT).show();
                     break;
                 case 1:
                     Toast.makeText(currentActivity,"删除成功",Toast.LENGTH_SHORT).show();
                     CurrentApplication cu = (CurrentApplication)currentActivity.getApplication();
                     cu.getBookList().get(groupPos).remove(selectedItemsPos);
                     cu.getAdapter().notifyDataSetChanged();
                     break;
                 case 2:
                     Toast.makeText(currentActivity,"网络连接超时",Toast.LENGTH_SHORT).show();
                     break;
             }
        }
    };

    public void setBookBean(BookBean bookBean) {
        this.bookBean = bookBean;
    }

    public void setUserCategory(UserCategory userCategory) {
        this.userCategory = userCategory;
    }

    public void setGroupPos(int groupPos) {
        this.groupPos = groupPos;
    }


    public void setSelectedItemsPos(int selectedItemsPos) {
        this.selectedItemsPos = selectedItemsPos;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        currentActivity = getActivity();
        Dialog dialog = new AlertDialog.Builder(currentActivity)
                .setTitle("确定移除该书籍")
                .setNegativeButton("Cancel",null)
                .setPositiveButton("OK",new ConfirmButtonListener())
                .create();
        return dialog;
    }

    private class ConfirmButtonListener implements DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialog, int which) {
            //send the book info;
            userBean = ((CurrentApplication)currentActivity.getApplication()).getUser();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        SQLUpload.sendDeleteCollectionBookInfo(userBean,bookBean,userCategory,handler);
                    }catch (Exception e){
                        if(e instanceof TimeoutException){
                            //timeout
                            Message msg = new Message();
                            msg.what = 2;
                            handler.sendMessage(msg);
                        }
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
