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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.buaa.yushijie.bookreader.R;
import com.buaa.yushijie.bookreader.Services.CurrentUser;
import com.buaa.yushijie.bookreader.Services.SQLUpload;

import java.util.concurrent.TimeoutException;

import bean.BookBean;
import bean.UserBean;

/**
 * Created by yushijie on 17-5-16.
 */

public class CommentDialogFragment extends DialogFragment {
    private EditText commentEditText;
    private BookBean currentBook;
    private Activity currentActivity;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    Toast.makeText(currentActivity,"发表失败",Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(currentActivity,"发表成功",Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(currentActivity,"网络连接超时",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    public void setCurrentBook(BookBean currentBook) {
        this.currentBook = currentBook;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        currentActivity = getActivity();
        View v = LayoutInflater.from(currentActivity).inflate(R.layout.comment_dialog_fragment,null);
        commentEditText = (EditText)v.findViewById(R.id.comment_dialog_fragment_edit_text_box);
        Dialog dialog = new AlertDialog.Builder(currentActivity)
                .setView(v)
                .setTitle("写评论")
                .setNegativeButton("Cancel",null)
                .setPositiveButton("OK",new ConfirmButtonListener())
                .create();
        return dialog;
    }

    private class ConfirmButtonListener implements DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialog, int which) {
            //send comment to server
            String comment = commentEditText.getText().toString();
            UserBean ub = ((CurrentUser) currentActivity.getApplication()).getUser();
            if(!comment.equals("")) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            SQLUpload.sendComment(ub,currentBook,comment,handler);
                        } catch (Exception e) {
                            if (e instanceof TimeoutException) {
                                //timeout
                                Message msg = new Message();
                                msg.what = 2;
                                handler.sendMessage(msg);
                            }
                            e.printStackTrace();
                        }
                    }
                }).start();

            }else{
                Toast.makeText(currentActivity,"不可为空",Toast.LENGTH_SHORT).show();
            }
        }
    }

}
