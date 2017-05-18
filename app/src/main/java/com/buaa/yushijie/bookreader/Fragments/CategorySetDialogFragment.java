package com.buaa.yushijie.bookreader.Fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.buaa.yushijie.bookreader.R;
import com.buaa.yushijie.bookreader.Services.CurrentUser;
import com.buaa.yushijie.bookreader.Services.DownLoadMyBookShelfService;
import com.buaa.yushijie.bookreader.Services.SQLUpload;

import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import bean.UserBean;
import bean.UserCategory;

/**
 * Created by yushijie on 17-5-12.
 */

public class CategorySetDialogFragment extends DialogFragment {
    private EditText categoryEditText;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.my_book_shelf_set_category_dialog_fragment,null);

        categoryEditText = (EditText)v.findViewById(R.id.my_book_shelf_add_category_edit_text);
        Dialog res = new AlertDialog.Builder(getActivity()).setView(v).setTitle("请输入分类名")
                .setNegativeButton("cancel",null)
                .setPositiveButton("ok",new CertainListener()).create();
        return res;
    }


    private class CertainListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            String category = categoryEditText.getText().toString();
            CurrentUser cu = (CurrentUser)getActivity().getApplication();
            UserBean ub = cu.getUser();

            if (!category.equals("")) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            SQLUpload.sendCategoryString(ub,category);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            DownLoadMyBookShelfService services = new DownLoadMyBookShelfService();
                            ArrayList<UserCategory> auc = services.getCategoryNameList(ub.account);
                            cu.setUserCategories(auc);

                        }catch (Exception e){
                            if(e instanceof TimeoutException){
                                //timeout
                            }
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }

    }

}
