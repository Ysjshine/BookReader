package com.buaa.yushijie.bookreader.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.buaa.yushijie.bookreader.Services.CurrentUser;
import com.buaa.yushijie.bookreader.Services.SQLUpload;

import java.net.ConnectException;

import bean.CommentBean;
import bean.UserBean;

/**
 * Created by yushijie on 17-5-21.
 */

public class DeleteACommentDialogFragment extends DialogFragment {

    private Activity currentActivity;
    private CommentBean commentBean;
    private UserBean userBean;


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    Toast.makeText(currentActivity,"删除失败,请稍后再试",Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(currentActivity,"删除成功",Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(currentActivity,"网络连接超时",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public void setCommentBean(CommentBean commentBean) {
        this.commentBean = commentBean;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        currentActivity = getActivity();
        Dialog dialog = new AlertDialog.Builder(currentActivity)
                .setTitle("确定删除？")
                .setNegativeButton("Cancel",null)
                .setPositiveButton("OK",new ConfirmButtonListener())
                .create();
        return dialog;
    }

    private class ConfirmButtonListener implements DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialog, int which) {
            CurrentUser cu = (CurrentUser)getActivity().getApplication();
            userBean = cu.getUser();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        SQLUpload.sendDeleteCommentInfo(userBean,commentBean,handler);
                    }catch (Exception e){
                        if(e instanceof ConnectException){
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
