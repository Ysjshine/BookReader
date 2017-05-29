package com.buaa.yushijie.bookreader.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.buaa.yushijie.bookreader.MainActivity;
import com.buaa.yushijie.bookreader.R;
import com.buaa.yushijie.bookreader.Services.EncodeAndDecode;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import bean.UserBean;

/**
 * Created by yushijie on 17-5-1.
 */

public class LoginActivity extends AppCompatActivity {
    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private Button mLoginButtton;
    private Button mRegisterButton;

    private SharedPreferences loginInfo;
    private static final String PASSWORD_SAVE="Password";
    private static final String USERNAME_SAVE="Username";

    private static final String URL_LOGIN = "http://120.25.89.166/BookReaderServer/Login";
    private static final String TAG="LoginActivity";
    private static final String USERNAME="username";


    private Thread t = null;
    private Handler handler  =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 0){
                Toast.makeText(LoginActivity.this,"登录失败，请检查用户名和密码是否正确",Toast.LENGTH_SHORT).show();
                mPasswordEditText.setText("");
                try{
                    t.interrupt();
                    t = null;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        autoLogin();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        mUsernameEditText = (EditText)findViewById(R.id.username_edit_text);
        mPasswordEditText = (EditText)findViewById(R.id.password_edit_text);
        mLoginButtton = (Button)findViewById(R.id.login_button);
        mRegisterButton = (Button)findViewById(R.id.register_button);


        mLoginButtton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String username = mUsernameEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();
                if(username.equals("") || password.equals("")){
                        Toast.makeText(LoginActivity.this,"信息不完整",Toast.LENGTH_SHORT).show();
                    return;
                }
                t =  new Thread(new Runnable(){
                    @Override
                    public void run(){
                        httpConnectionPost(username,password);
                    }
                });
                t.start();
            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
    }

    //login automatically
    private void autoLogin(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                loginInfo = getSharedPreferences("loginInfo",0);
                String username = loginInfo.getString(USERNAME_SAVE,"");
                String password = loginInfo.getString(PASSWORD_SAVE,"");
                if(!(username.equals("")||password.equals(""))){
                    httpConnectionPost(username,password);
                }
            }
        }).start();
    }
    public void httpConnectionPost(String username,String password){
        HttpURLConnection conn = null;
        try {
            URL url = new URL(URL_LOGIN);
            conn =(HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            String content = "username="+EncodeAndDecode.encodeString(username)+"&"
                    +"password="+password;
            dos.writeBytes(content);
            dos.flush();dos.close();

            BufferedReader bf = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String sa = null;
            while((sa  = bf.readLine())!=null){
                sb.append(sa);
            }
            Log.e(TAG, "httpConnectionPost: "+sb.toString() );
            if(sb.toString().equals("1")){
                //save username and password
                loginInfo = getSharedPreferences("loginInfo",0);
                SharedPreferences.Editor editor = loginInfo.edit();
                editor.putString(USERNAME_SAVE,username);
                editor.putString(PASSWORD_SAVE,password);
                editor.commit();
                //jump to index
                Intent login = new Intent(LoginActivity.this,MainActivity.class);
                login.putExtra(USERNAME,username);
                Log.e(TAG, "httpConnectionPost: "+username );
                login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(login);
            } else {
                Message msg = new Message();
                msg.what = 0;
                handler.sendMessage(msg);
            }
            bf.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(conn != null) conn.disconnect();
        }
    }
}
