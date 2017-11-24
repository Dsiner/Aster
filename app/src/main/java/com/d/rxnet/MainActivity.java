package com.d.rxnet;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.d.lib.rxnet.util.RxUtil;
import com.d.rxnet.request.Download;
import com.d.rxnet.request.Get;
import com.d.rxnet.request.Post;
import com.d.rxnet.request.Upload;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        RxUtil.printThread("dsiner_theard Main: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wipeCache();
        initView();
    }

    private void wipeCache() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RxUtil.deleteFile(new File(Environment.getExternalStorageDirectory().getPath() + "/test/1.jpg"));
            }
        }).run();
    }

    private void initView() {
        findViewById(R.id.btn_get).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Get(MainActivity.this).testAll();
            }
        });
        findViewById(R.id.btn_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Post(MainActivity.this).testAll();
            }
        });
        findViewById(R.id.btn_download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Download(MainActivity.this).testAll();
            }
        });
        findViewById(R.id.btn_upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Upload(MainActivity.this).testAll();
            }
        });
    }
}
