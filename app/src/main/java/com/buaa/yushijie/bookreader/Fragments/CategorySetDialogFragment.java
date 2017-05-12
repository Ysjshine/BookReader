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
import com.buaa.yushijie.bookreader.Services.SQLUpload;

import java.util.zip.Inflater;

/**
 * Created by yushijie on 17-5-12.
 */

public class CategorySetDialogFragment extends DialogFragment {
    private EditText categoryEditText;
    private String categoryRes = null;

    public String getCategoryRes() {
        return categoryRes;
    }

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
            if (!category.equals("")) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SQLUpload.sendCategoryString(category);
                    }
                }).start();
            }
        }

    }

}
