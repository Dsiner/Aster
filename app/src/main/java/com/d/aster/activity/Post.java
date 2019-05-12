package com.d.aster.activity;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.d.aster.Logger;
import com.d.aster.api.API;
import com.d.aster.model.MovieInfo;
import com.d.lib.aster.Aster;
import com.d.lib.aster.base.AsterModule;
import com.d.lib.aster.base.Params;
import com.d.lib.aster.callback.AsyncCallback;
import com.d.lib.aster.scheduler.callback.Observer;
import com.d.lib.aster.scheduler.schedule.Schedulers;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;

/**
 * Request --> Post
 * Created by D on 2017/10/26.
 */
public class Post extends Request {

    @Override
    protected void init() {
        mUrl = API.MovieTop.rtpType;
        etUrl.setText(mUrl);
        etUrl.setSelection(etUrl.getText().toString().length());
    }

    @Override
    protected void request() {
        requestImpl(TYPE_SINGLETON);
    }

    @Override
    protected void requestSingleton() {
        Params params = new Params(mUrl);
        params.addParam(API.MovieTop.start, "0");
        params.addParam(API.MovieTop.count, "10");
        Aster.getDefault().post(API.MovieTop.rtpType, params)
                .request(new AsyncCallback<String, String>() {
                    @Override
                    public String apply(@NonNull String info) throws Exception {
                        Logger.d("dsiner_request--> apply");
                        return "" + info;
                    }

                    @Override
                    public void onSuccess(String response) {
                        Logger.d("dsiner_request--> onSuccess: " + response);
                        formatPrinting(response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.d("dsiner_request--> onError");
                    }
                });
    }

    @Override
    protected void requestNew() {
        Params params = new Params(mUrl);
        params.addParam(API.MovieTop.start, "1");
        params.addParam(API.MovieTop.count, "10");
        Aster.post("top250", params)
                .baseUrl("https://api.douban.com/v2/movie/")
                .connectTimeout(5 * 1000)
                .readTimeout(5 * 1000)
                .writeTimeout(5 * 1000)
                .request(new AsyncCallback<MovieInfo, String>() {
                    @Override
                    public String apply(@NonNull MovieInfo info) throws Exception {
                        Logger.d("dsiner_request--> apply");
                        int size = info.subjects.size();
                        return "" + size;
                    }

                    @Override
                    public void onSuccess(String response) {
                        Logger.d("dsiner_request--> onSuccess: " + response);
                        formatPrinting(response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.d("dsiner_request--> onError");
                    }
                });
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void requestObservable() {
        Aster.getDefault().post(mUrl)
                .observable(ResponseBody.class)
                .observeOn(Schedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onNext(@NonNull ResponseBody response) {
                        try {
                            formatPrinting(response.string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    @Override
    protected void requestRetrofit() {
        AsterModule aster = Aster.getAster();
        if (!(aster instanceof com.d.lib.aster.integration.retrofit.RetrofitModule)) {
            Toast.makeText(getApplicationContext(),
                    "Please initial Aster with the RetrofitModule.",
                    Toast.LENGTH_SHORT);
            return;
        }
        com.d.lib.aster.integration.retrofit.RetrofitModule retrofit
                = (com.d.lib.aster.integration.retrofit.RetrofitModule) aster;
        retrofit.getRetrofit().create(com.d.lib.aster.integration.retrofit.RetrofitAPI.class)
                .post(mUrl)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .map(new io.reactivex.functions.Function<ResponseBody, MovieInfo>() {
                    @Override
                    public MovieInfo apply(ResponseBody responseBody) throws Exception {
                        return new Gson().fromJson(responseBody.string(), MovieInfo.class);
                    }
                })
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<MovieInfo>() {
                    @Override
                    public void onSubscribe(io.reactivex.disposables.Disposable d) {

                    }

                    @Override
                    public void onNext(MovieInfo movieInfo) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
