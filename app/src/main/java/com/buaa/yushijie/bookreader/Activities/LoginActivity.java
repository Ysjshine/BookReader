package com.buaa.yushijie.bookreader.Activities;

import android.content.Intent;
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
    private String TAG="LoginActivity";
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        mUsernameEditText = (EditText)findViewById(R.id.username_edit_text);
        mPasswordEditText = (EditText)findViewById(R.id.password_edit_text);
        mLoginButtton = (Button)findViewById(R.id.login_button);
        mRegisterButton = (Button)findViewById(R.id.register_button);

        mLoginButtton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mUsernameEditText.getText().toString().equals("") || mPasswordEditText.getText().toString().equals("")){
                        Toast.makeText(LoginActivity.this,"信息不完整",Toast.LENGTH_SHORT).show();
                    return;
                }
                t =  new Thread(new Runnable(){
                    @Override
                    public void run(){
                        httpConnectionPost();
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

    public void httpConnectionPost(){
        HttpURLConnection conn = null;
        try {
            URL url = new URL("http://120.25.89.166/BookReaderServer/login");
            conn =(HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            String content = "username="+mUsernameEditText.getText().toString()+"&"
                    +"password="+mPasswordEditText.getText().toString();

            dos.writeBytes(content);
            dos.flush();
            dos.close();
            InputStream in = conn.getInputStream();
            InputStreamReader inr = new InputStreamReader(in);
           BufferedReader bf = new BufferedReader(inr);
            StringBuilder sb = new StringBuilder();
            String sa = null;
            while((sa  = bf.readLine())!=null){
                sb.append(sa);
            }
            Log.e(TAG, "httpConnectionPost: "+sb.toString() );
            if(sb.toString().equals("1")){
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
            else {
                Message msg = new Message();
                msg.what = 0;
                handler.sendMessage(msg);
            }
            in.close();
            inr.close();
            bf.close();


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(conn != null) conn.disconnect();
        }
    }
}
