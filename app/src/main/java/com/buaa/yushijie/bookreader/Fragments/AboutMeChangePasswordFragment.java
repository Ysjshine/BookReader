package com.buaa.yushijie.bookreader.Fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.buaa.yushijie.bookreader.R;
import com.buaa.yushijie.bookreader.Services.SQLUpload;

import java.util.concurrent.TimeoutException;

import bean.UserBean;

/**
 * Created by yushijie on 17-5-16.
 */

public class AboutMeChangePasswordFragment extends DialogFragment {
    private EditText oldPasswordEditText;
    private EditText newPasswordEditText;
    private UserBean ub;

    public void setUb(UserBean ub) {
        this.ub = ub;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.about_me_change_password_dialog_fragment,null);
        oldPasswordEditText = (EditText)v.findViewById(R.id.about_me_change_password_dialog_old_password);
        newPasswordEditText = (EditText)v.findViewById(R.id.about_me_change_password_dialog_new_password);

        Dialog dialog = new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("密码修改")
                .setPositiveButton("OK",new ConfirmButtonListener())
                .setNegativeButton("Cancel",null)
                .create();

        return dialog;
    }

    private class ConfirmButtonListener implements DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialog, int which) {
            String oldPassword = oldPasswordEditText.getText().toString();
            String newPassword = newPasswordEditText.getText().toString();
            if(oldPassword.equals(ub.password)&&newPassword.length()>=6){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            SQLUpload.sendNewPassword(ub,newPassword);
                        }catch (Exception e) {
                            if(e instanceof TimeoutException){
                                //timeout
                            }
                            e.printStackTrace();
                        }
                    }
                }).start();
            }else if(oldPassword.equals(ub.password)&&newPassword.length()<6){
                Toast.makeText(getActivity(),"重置密码失败,密码长度应大于等于6",Toast.LENGTH_SHORT).show();
            }else if(!oldPassword.equals(ub.password)){
                Toast.makeText(getActivity(),"重置密码失败,原密码错误",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
