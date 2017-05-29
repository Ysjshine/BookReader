package com.buaa.yushijie.bookreader.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.buaa.yushijie.bookreader.R;

/**
 * Created by yushijie on 17-5-29.
 */

public class AboutTeamInfoActivity extends AppCompatActivity {
    private android.support.design.widget.FloatingActionButton backButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_team_info_layout);
        backButton = (android.support.design.widget.FloatingActionButton)findViewById(R.id.floatingActionButton2);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
