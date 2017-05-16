package com.buaa.yushijie.bookreader.Fragments;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.buaa.yushijie.bookreader.R;
import com.buaa.yushijie.bookreader.Services.CurrentUser;
import com.buaa.yushijie.bookreader.Services.SQLUpload;

import java.util.concurrent.TimeoutException;

import bean.UserBean;

/**
 * Created by yushijie on 17-5-16.
 */

public class CommentDialogFragment extends DialogFragment {
    private EditText commentEditText;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.comment_dialog_fragment,null);
        commentEditText = (EditText)v.findViewById(R.id.comment_dialog_fragment_edit_text_box);
        Dialog dialog = new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("写评论")
                .setNegativeButton("Cancel",null)
                .setPositiveButton("OK",null)
                .create();
        return dialog;
    }

    private class confirmContentButtonListener implements DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialog, int which) {
            //send comment to server
            String comment = commentEditText.getText().toString();
            UserBean ub = ((CurrentUser)getActivity().getApplication()).getUser();
            if(!comment.equals("")) {
                try {
                    SQLUpload.sendComment(ub,comment);
                } catch (Exception e) {
                    if (e instanceof TimeoutException) {
                        //timeout
                    }
                    e.printStackTrace();
                }
            }
        }
    }

}
