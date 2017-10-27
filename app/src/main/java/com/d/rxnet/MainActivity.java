package com.d.rxnet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.d.lib.rxnet.util.RxLog;
import com.d.rxnet.request.Down;
import com.d.rxnet.request.Get;
import com.d.rxnet.request.Post;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxLog.d("dsiner_th_Main: " + Thread.currentThread().getId() + "--NAME--" + Thread.currentThread().getName());
        setContentView(R.layout.activity_main);
        initView();
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
        findViewById(R.id.btn_downFile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Down(MainActivity.this).testAll();
            }
        });
    }
}
