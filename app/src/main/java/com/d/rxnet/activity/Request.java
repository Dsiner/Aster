package com.d.rxnet.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.d.rxnet.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public abstract class Request extends AppCompatActivity {
    protected Context mContext;
    protected String mUrl, mParams;

    protected EditText etUrl, etParams;
    protected Button btnRequest;
    protected TextView tvContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        setTitle(getClass().getSimpleName());
        setContentView(R.layout.activity_request);
        bindView();
        init();
    }

    protected void bindView() {
        etUrl = (EditText) findViewById(R.id.et_url);
        etParams = (EditText) findViewById(R.id.et_params);
        btnRequest = (Button) findViewById(R.id.btn_request);
        tvContent = (TextView) findViewById(R.id.tv_content);
        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request();
            }
        });
    }

    protected void formatPrinting(String json) {
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        tvContent.setText(gson.toJson(jsonObject));
    }

    protected abstract void init();

    protected abstract void request();
}
