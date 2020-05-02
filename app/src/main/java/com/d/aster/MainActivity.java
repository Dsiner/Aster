package com.d.aster;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.d.aster.activity.DownloadActivity;
import com.d.aster.activity.GetActivity;
import com.d.aster.activity.PostActivity;
import com.d.aster.activity.UploadActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_get:
                startActivity(new Intent(MainActivity.this, GetActivity.class));
                break;

            case R.id.btn_post:
                startActivity(new Intent(MainActivity.this, PostActivity.class));
                break;

            case R.id.btn_download:
                startActivity(new Intent(MainActivity.this, DownloadActivity.class));
                break;

            case R.id.btn_upload:
                startActivity(new Intent(MainActivity.this, UploadActivity.class));
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindView();
    }

    private void bindView() {
        findViewById(R.id.btn_get).setOnClickListener(this);
        findViewById(R.id.btn_post).setOnClickListener(this);
        findViewById(R.id.btn_download).setOnClickListener(this);
        findViewById(R.id.btn_upload).setOnClickListener(this);
    }
}
