package com.buaa.yushijie.bookreader.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.buaa.yushijie.bookreader.MainActivity;
import com.buaa.yushijie.bookreader.R;
import com.buaa.yushijie.bookreader.Services.EncodeAndDecode;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by yushijie on 17-5-1.
 */

public class RegisterActivity extends AppCompatActivity {

    private EditText mUsername;
    private EditText mPassword;
    private EditText mPasswordAgain;
    private RadioButton mMale;
    private RadioButton mFemale;
    private Button mRegisterButton;


    private String gender;
    private static final String TAG="RegisterActivity";
    private static final String URL_REGISTER="http://120.25.89.166/BookReaderServer/Register";

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 1){
                Toast.makeText(RegisterActivity.this, "用户名已存在", Toast.LENGTH_SHORT).show();
            }else if(msg.what == 2){
                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        mUsername = (EditText)findViewById(R.id.username_edit_text_pwd);
        mPassword = (EditText)findViewById(R.id.password_edit_text_pwd);
        mPasswordAgain = (EditText)findViewById(R.id.password_again_edit_text_pwd);
        mMale = (RadioButton)findViewById(R.id.male_radio_button);
        mFemale = (RadioButton)findViewById(R.id.female_radio_button);
        mRegisterButton = (Button)findViewById(R.id.register_button_pwd);

        mRegisterButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(mUsername.getText().toString().equals("")
                        || mPassword.getText().toString().equals("")
                        || mPasswordAgain.getText().toString().equals("")){
                    Toast.makeText(RegisterActivity.this,"信息不完整",Toast.LENGTH_SHORT).show();
                }else if(!mPassword.getText().toString().equals(mPasswordAgain.getText().toString())){
                    Toast.makeText(RegisterActivity.this,"密码不一致",Toast.LENGTH_SHORT).show();
                    mPasswordAgain.setText("");
                }else if(mPassword.getText().toString().length()<6){
                    Toast.makeText(RegisterActivity.this,"密码长度不得少于六位",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(mMale.isChecked()){
                        gender = "0";
                    }else if(mFemale.isChecked()){
                        gender = "1";
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            httpConnectionPostRegisterInfo();
                        }
                    }).start();
                }
            }
        });


    }

    public void httpConnectionPostRegisterInfo(){
        HttpURLConnection conn = null;
        try {
            URL url = new URL(URL_REGISTER);
            conn =(HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            String content = "username="+ mUsername.getText().toString()+"&"
                    +"password="+mPassword.getText().toString()+"&"
                    +"gender="+gender;

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
            Message msg = new Message();
            if(sb.toString().equals("1")){
                msg.what = 2;
                handler.sendMessage(msg);
            }
            else {
                msg.what = 1;
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
