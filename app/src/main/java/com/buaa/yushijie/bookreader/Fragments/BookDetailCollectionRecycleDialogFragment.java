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
import android.util.Log;
import android.widget.Toast;

import com.buaa.yushijie.bookreader.Services.CurrentUser;
import com.buaa.yushijie.bookreader.Services.DownLoadMyBookShelfService;
import com.buaa.yushijie.bookreader.Services.SQLUpload;

import java.net.ConnectException;
import java.util.ArrayList;

import bean.BookBean;
import bean.UserBean;
import bean.UserCategory;

/**
 * Created by yushijie on 17-5-19.
 */

public class BookDetailCollectionRecycleDialogFragment extends DialogFragment {

    private int selectedIndex = 0;
    private Activity currentActivity;
    private CurrentUser cu ;
    private BookBean bookBean;
    UserCategory userCategory;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(currentActivity != null){
                if(msg.what == 0){
                    Toast.makeText(currentActivity,"书籍已存在",Toast.LENGTH_SHORT).show();
                }else if(msg.what == 1){
                    Toast.makeText(currentActivity,"添加成功",Toast.LENGTH_SHORT).show();
                    CurrentUser cu = (CurrentUser)currentActivity.getApplication();
                    ArrayList<ArrayList<BookBean>> b =  cu.getBookList();

                    b.get(selectedIndex).add(bookBean);
                    cu.getAdapter().notifyDataSetChanged();
                }else if(msg.what == 2){
                    Toast.makeText(currentActivity,"网络连接超时",Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    public void setBookBean(BookBean bookBean) {
        this.bookBean = bookBean;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentActivity = getActivity();
        cu =  (CurrentUser)currentActivity.getApplication();

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        ArrayList<String> items = new ArrayList<>();
        int size = cu.getUserCategories().size();
        for(int i=0;i<size;i++){
            items.add(cu.getUserCategories().get(i).CategoryName);
        }
        String[] a = new String[size];
        AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);

        builder.setTitle("选择分类");
        builder.setSingleChoiceItems(items.toArray(a),0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedIndex = which;
                Log.e("ssss", "onClick: "+selectedIndex );
            }
        });
        return  builder.setPositiveButton("OK",new ConfirmButtonListener())
                .setNegativeButton("Cancel",null)
                .create();
    }

    private class ConfirmButtonListener implements DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialog, int which) {
             userCategory = cu.getUserCategories().get(selectedIndex);
            UserBean userBean = cu.getUser();
            Message msg = new Message();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        SQLUpload.sendCollectionInfo(userBean,userCategory,bookBean,handler);


                    }catch (Exception e){
                        if(e instanceof ConnectException){
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
