package com.d.aster.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.d.aster.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public abstract class Request extends AppCompatActivity {
    public final static int TYPE_SINGLETON = 0;
    public final static int TYPE_NEW = 1;
    public final static int TYPE_OBSERVABLE = 2;
    public final static int TYPE_RETROFIT = 3;

    @IntDef({TYPE_SINGLETON, TYPE_NEW, TYPE_OBSERVABLE, TYPE_RETROFIT})
    @Target({ElementType.PARAMETER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {

    }

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

    protected final void requestImp(@State int type) {
        switch (type) {
            case TYPE_SINGLETON:
                requestSingleton();
                break;
            case TYPE_NEW:
                requestNew();
                break;
            case TYPE_OBSERVABLE:
                requestObservable();
                break;
            case TYPE_RETROFIT:
                requestRetrofit();
                break;
        }
    }

    protected abstract void init();

    protected abstract void request();

    protected void requestSingleton() {
    }

    protected void requestNew() {
    }

    protected void requestObservable() {
    }

    protected void requestRetrofit() {
    }
}
