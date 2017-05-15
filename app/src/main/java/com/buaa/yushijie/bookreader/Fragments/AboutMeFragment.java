package com.buaa.yushijie.bookreader.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.TaskStackBuilder;
import android.system.ErrnoException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.buaa.yushijie.bookreader.Activities.LoginActivity;
import com.buaa.yushijie.bookreader.R;
import com.buaa.yushijie.bookreader.Services.SQLUpload;

import java.util.concurrent.TimeoutException;

import bean.UserBean;

/**
 * Created by yushijie on 17-5-15.
 */

public class AboutMeFragment extends Fragment {
    private UserBean user = new UserBean();
    private TextView usernameTextView;
    private EditText nicknameEditext;
    private Button mModifyNickNameButton;
    private Button mModifyPasswordButton;
    private Button mLoginOutButton;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            mModifyNickNameButton.setText("EDIT");
            nicknameEditext.setFocusable(false);
        }
    };


    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.about_me_fragment,container,false);
        usernameTextView = (TextView)v.findViewById(R.id.about_me_fragment_username_text);
        nicknameEditext = (EditText)v.findViewById(R.id.about_me_fragment_nickname_text);
        mModifyNickNameButton = (Button) v.findViewById(R.id.about_me_fragment_edit_nickname_button);
        mModifyPasswordButton = (Button)v.findViewById(R.id.about_me_change_password_button);
        mLoginOutButton = (Button)v.findViewById(R.id.about_me_login_out_button);
        nicknameEditext.setFocusable(false);

        if(user == null)
            return v;
        user.UserID = 1;
        user.account = "hhhhh";
        user.nickname = "hhh";
        usernameTextView.setText(user.account);
        nicknameEditext.setText(user.nickname);
        mModifyNickNameButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(!nicknameEditext.isFocusable()){
                    nicknameEditext.setFocusable(true);
                    mModifyNickNameButton.setText("save");
                }else{
                    String ans = nicknameEditext.getText().toString();
                    if(!ans.equals("")){
                        //send new nickname string to server
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    SQLUpload.senNewNickNameString(user,ans);
                                }catch (Exception e){
                                    if(e instanceof TimeoutException){
                                        //connecting timeout;
                                    }
                                    e.printStackTrace();
                                }finally {
                                    handler.sendMessage(new Message());
                                }
                            }
                        }).start();
                    }else{
                        nicknameEditext.setText(user.nickname);
                    }
                    mModifyNickNameButton.setText("EDIT");
                    nicknameEditext.setFocusable(false);
                }
            }
        });

        mModifyPasswordButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //modify password
                //show dialog
            }
        });

        mLoginOutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent logoutIntent = new Intent(getActivity(), LoginActivity.class);
                logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(logoutIntent);
            }
        });
        return v;
    }
}
