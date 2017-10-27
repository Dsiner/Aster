package com.d.rxnet.request;

import android.app.Activity;
import android.content.Context;

import com.d.lib.rxnet.RxNet;
import com.d.lib.rxnet.api.API;
import com.d.lib.rxnet.api.RetrofitAPI;
import com.d.lib.rxnet.base.Params;
import com.d.lib.rxnet.exception.ApiException;
import com.d.lib.rxnet.listener.AsyncCallBack;
import com.d.lib.rxnet.listener.SimpleCallBack;
import com.d.lib.rxnet.util.RxLog;
import com.d.rxnet.model.MovieTopModelInfo;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Request Test --> Get
 * Created by D on 2017/10/26.
 */
public class Get {
    private Context appContext;

    public Get(Activity activity) {
        appContext = activity.getApplicationContext();
    }

    public void testAll() {
        testIns();
        testNew();
        testObservable();
        testRetrofit();
    }

    public void testIns() {
        Params params = new Params(API.MovieTop.rtpType);
        params.addParam(API.MovieTop.start, "0");
        params.addParam(API.MovieTop.count, "10");
        RxNet.getInstance(appContext).get(API.MovieTop.rtpType, params)
                .request(new SimpleCallBack<MovieTopModelInfo>() {
                    @Override
                    public void onSuccess(MovieTopModelInfo response) {
                        RxLog.d("dsiner_th_onSuccess: " + Thread.currentThread().getId() + "--NAME--" + Thread.currentThread().getName());
                        RxLog.d("dsiner_onSuccess: " + response);
                    }

                    @Override
                    public void onError(ApiException e) {
                        RxLog.d("dsiner_onError: ");
                    }
                });

        RxNet.getInstance(appContext).get("https://www.baidu.com", params)
                .request(new AsyncCallBack<String, String>() {
                    @Override
                    public String apply(@NonNull String info) throws Exception {
                        RxLog.d("dsiner_th_apply: " + Thread.currentThread().getId() + "--NAME--" + Thread.currentThread().getName());
                        return "" + info;
                    }

                    @Override
                    public void onSuccess(String response) {
                        RxLog.d("dsiner_th_onSuccess: " + Thread.currentThread().getId() + "--NAME--" + Thread.currentThread().getName());
                        RxLog.d("dsiner_onSuccess: " + response);
                    }

                    @Override
                    public void onError(ApiException e) {
                        RxLog.d("dsiner_onError: ");
                    }
                });
    }

    public void testNew() {
        Params params = new Params(API.MovieTop.rtpType);
        params.addParam(API.MovieTop.start, "1");
        params.addParam(API.MovieTop.count, "10");
        new RxNet(appContext).get("top250", params)
                .baseUrl("https://api.douban.com/v2/movie/")
                .connectTimeout(5 * 1000)
                .readTimeout(5 * 1000)
                .writeTimeout(5 * 1000)
                .request(new AsyncCallBack<MovieTopModelInfo, String>() {
                    @Override
                    public String apply(@NonNull MovieTopModelInfo movieTopModelInfo) throws Exception {
                        RxLog.d("dsiner_th_apply: " + Thread.currentThread().getId() + "--NAME--" + Thread.currentThread().getName());
                        int size = movieTopModelInfo.subjects.size();
                        return "" + size;
                    }

                    @Override
                    public void onSuccess(String response) {
                        RxLog.d("dsiner_th_onSuccess: " + Thread.currentThread().getId() + "--NAME--" + Thread.currentThread().getName());
                        RxLog.d("dsiner_onSuccess: " + response);
                    }

                    @Override
                    public void onError(ApiException e) {
                        RxLog.d("dsiner_onError: ");
                    }
                });
    }

    public void testObservable() {
        RxNet.getInstance(appContext).get("")
                .observable(Boolean.class)
                .subscribe(new DisposableObserver<Boolean>() {
                    @Override
                    public void onNext(@NonNull Boolean aBoolean) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void testRetrofit() {
        RxNet.getRetrofit(appContext).create(RetrofitAPI.class)
                .get("")
                .subscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, ArrayList<Boolean>>() {
                    @Override
                    public ArrayList<Boolean> apply(@NonNull ResponseBody info) throws Exception {
                        return new ArrayList<Boolean>();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<ArrayList<Boolean>>() {
                    @Override
                    protected void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onNext(@NonNull ArrayList<Boolean> list) {
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
