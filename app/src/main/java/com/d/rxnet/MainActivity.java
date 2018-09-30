package com.d.rxnet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.d.lib.rxnet.base.ApiManager;
import com.d.lib.rxnet.utils.Util;
import com.d.rxnet.activity.Download;
import com.d.rxnet.activity.Get;
import com.d.rxnet.activity.Post;
import com.d.rxnet.activity.Upload;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindView();
        wipeCache();
    }

    private void bindView() {
        findViewById(R.id.btn_get).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Get.class));
            }
        });
        findViewById(R.id.btn_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Post.class));
            }
        });
        findViewById(R.id.btn_download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Download.class));
            }
        });
        findViewById(R.id.btn_upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Upload.class));
            }
        });
    }

    private void wipeCache() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Util.deleteFile(new File(App.mPath + "1.jpg"));
            }
        }).run();
    }

    @Override
    protected void onDestroy() {
        ApiManager.get().cancelAll();
        super.onDestroy();
    }
}
