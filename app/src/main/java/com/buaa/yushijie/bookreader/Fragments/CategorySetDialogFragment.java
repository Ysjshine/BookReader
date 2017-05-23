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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.buaa.yushijie.bookreader.R;
import com.buaa.yushijie.bookreader.Services.CurrentApplication;
import com.buaa.yushijie.bookreader.Services.DownLoadMyBookShelfService;
import com.buaa.yushijie.bookreader.Services.SQLUpload;
import java.net.ConnectException;
import java.util.ArrayList;

import bean.BookBean;
import bean.UserBean;
import bean.UserCategory;


/**
 * Created by yushijie on 17-5-12.
 */

public class CategorySetDialogFragment extends DialogFragment {
    private EditText categoryEditText;
    private Activity currentActivity;
    private static final String TAG="CategorySet";
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 0) {
                if(currentActivity == null) {
                    Log.e(TAG, "handleMessage: NULLLLLL" );
                } else {
                    Toast.makeText(currentActivity, "网络连接超时", Toast.LENGTH_SHORT).show();
                }
            }else if(msg.what == 1){
                if(currentActivity == null) {
                    Log.e(TAG, "handleMessage: NULLLLLL" );
                } else {
                    Toast.makeText(currentActivity, "添加成功", Toast.LENGTH_SHORT).show();

                    //Synchronized the data;
                    CurrentApplication cu = (CurrentApplication) currentActivity.getApplication();
                    cu.getUserCategories().clear();
                    cu.getUserCategories().addAll((ArrayList<UserCategory>)msg.obj);
                    ArrayList<BookBean> b = new ArrayList<>();
                    cu.getBookList().add(b);
                    cu.getAdapter().notifyDataSetChanged();
                }

            }
        }

    };

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        currentActivity = getActivity();
        View v = LayoutInflater.from(currentActivity).inflate(R.layout.my_book_shelf_set_category_dialog_fragment,null);

        categoryEditText = (EditText)v.findViewById(R.id.my_book_shelf_add_category_edit_text);
        Dialog res = new AlertDialog.Builder(currentActivity).setView(v).setTitle("请输入分类名")
                .setNegativeButton("cancel",null)
                .setPositiveButton("ok",new ConfirmButtonListener()).create();
        return res;
    }


    private class ConfirmButtonListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            String category = categoryEditText.getText().toString();
            CurrentApplication cu = (CurrentApplication)currentActivity.getApplication();
            UserBean ub = cu.getUser();

            if (!category.equals("")) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            SQLUpload.sendCategoryString(ub,category);

                            //get category again
                            DownLoadMyBookShelfService services = new DownLoadMyBookShelfService();
                            Message msg = new Message();
                            msg.what = 1;
                            msg.obj = services.getCategoryNameList(ub.account);

                            handler.sendMessage(msg);
                        } catch (Exception e) {
                            if(e instanceof ConnectException){
                                Message msg = new Message();
                                msg.what = 0;
                                handler.sendMessage(msg);
                            }
                            e.printStackTrace();
                        }

                    }
                }).start();

            }
        }

    }

}
