package com.buaa.yushijie.bookreader.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.buaa.yushijie.bookreader.R;

/**
 * Created by yushijie on 17-5-28.
 */

public class BookReadSettingFragmentDialog extends DialogFragment {
    private Activity currentActivity;
    private Context currentContext;
    private EditText fontSizeEditText;
    private RadioGroup fontFamilyRadioGroup;
    private RadioGroup backgroundColorGroup;
    private WebView mWebView;

    public void setmWebView(WebView mWebView) {
        this.mWebView = mWebView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        currentActivity = getActivity();
        Dialog dialog = new Dialog(currentActivity, R.style.Theme_Light_Dialog);
        View v = LayoutInflater.from(currentActivity).inflate(R.layout.book_read_settings_dialog,null);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.dialogStyle);
        window.getDecorView().setPadding(0, 0, 0, 0);
        android.view.WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        dialog.setContentView(v);

        fontSizeEditText = (EditText)v.findViewById(R.id.book_read_settings_font_size_edit_text);
        fontFamilyRadioGroup = (RadioGroup)v.findViewById(R.id.book_read_settings_font_family);
        backgroundColorGroup = (RadioGroup)v.findViewById(R.id.book_read_settings_background_color);

        fontSizeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")) return;
                int size = Integer.parseInt(s.toString());
                if(size>10&&size<30) {
                    mWebView.getSettings().setDefaultFontSize(size);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        fontFamilyRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                String text = ((RadioButton)v.findViewById(group.getCheckedRadioButtonId())).getText().toString();
                mWebView.getSettings().setSerifFontFamily(text);
            }
        });

        backgroundColorGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                String text =  ((RadioButton)v.findViewById(group.getCheckedRadioButtonId())).getText().toString();
                if(text.equals("米黄")){
                    mWebView.setBackgroundColor(Color.rgb(255,250,205));
                }else if(text.equals("泛黄")){
                    mWebView.setBackgroundColor(Color.rgb(220,200,150));
                }else {
                    mWebView.setBackgroundColor(Color.rgb(232,232,232));
                }
            }
        });
        return dialog;
    }
}
