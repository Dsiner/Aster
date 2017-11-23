package com.d.rxnet.request;

import android.app.Activity;
import android.content.Context;

import com.d.lib.rxnet.RxNet;
import com.d.lib.rxnet.api.RetrofitAPI;
import com.d.lib.rxnet.base.Params;
import com.d.lib.rxnet.exception.ApiException;
import com.d.lib.rxnet.listener.AsyncCallBack;
import com.d.lib.rxnet.listener.SimpleCallBack;
import com.d.lib.rxnet.util.RxLog;
import com.d.rxnet.api.API;
import com.d.rxnet.model.MovieInfo;

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

    private void testIns() {
        Params params = new Params(API.MovieTop.rtpType);
        params.addParam(API.MovieTop.start, "0");
        params.addParam(API.MovieTop.count, "10");
        RxNet.getInstance(appContext).get(API.MovieTop.rtpType, params)
                .request(new SimpleCallBack<MovieInfo>() {
                    @Override
                    public void onSuccess(MovieInfo response) {
                        RxLog.d("dsiner_th onSuccess: " + Thread.currentThread().getId() + "--NAME--" + Thread.currentThread().getName());
                        RxLog.d("dsiner_request onSuccess: " + response);
                    }

                    @Override
                    public void onError(ApiException e) {
                        RxLog.d("dsiner_th onError: " + Thread.currentThread().getId() + "--NAME--" + Thread.currentThread().getName());
                        RxLog.d("dsiner_request onError");
                    }
                });

        RxNet.getInstance(appContext).get("https://www.baidu.com", params)
                .request(new AsyncCallBack<String, String>() {
                    @Override
                    public String apply(@NonNull String info) throws Exception {
                        RxLog.d("dsiner_th apply: " + Thread.currentThread().getId() + "--NAME--" + Thread.currentThread().getName());
                        RxLog.d("dsiner_request apply");
                        return "" + info;
                    }

                    @Override
                    public void onSuccess(String response) {
                        RxLog.d("dsiner_th onSuccess: " + Thread.currentThread().getId() + "--NAME--" + Thread.currentThread().getName());
                        RxLog.d("dsiner_request onSuccess: " + response);
                    }

                    @Override
                    public void onError(ApiException e) {
                        RxLog.d("dsiner_th onError: " + Thread.currentThread().getId() + "--NAME--" + Thread.currentThread().getName());
                        RxLog.d("dsiner_request onError");
                    }
                });
    }

    private void testNew() {
        Params params = new Params(API.MovieTop.rtpType);
        params.addParam(API.MovieTop.start, "1");
        params.addParam(API.MovieTop.count, "10");
        new RxNet(appContext).get("top250", params)
                .baseUrl("https://api.douban.com/v2/movie/")
                .connectTimeout(5 * 1000)
                .readTimeout(5 * 1000)
                .writeTimeout(5 * 1000)
                .request(new AsyncCallBack<MovieInfo, String>() {
                    @Override
                    public String apply(@NonNull MovieInfo movieTopModelInfo) throws Exception {
                        RxLog.d("dsiner_th apply: " + Thread.currentThread().getId() + "--NAME--" + Thread.currentThread().getName());
                        RxLog.d("dsiner_request apply");
                        int size = movieTopModelInfo.subjects.size();
                        return "" + size;
                    }

                    @Override
                    public void onSuccess(String response) {
                        RxLog.d("dsiner_th onSuccess: " + Thread.currentThread().getId() + "--NAME--" + Thread.currentThread().getName());
                        RxLog.d("dsiner_request onSuccess: " + response);
                    }

                    @Override
                    public void onError(ApiException e) {
                        RxLog.d("dsiner_th onError: " + Thread.currentThread().getId() + "--NAME--" + Thread.currentThread().getName());
                        RxLog.d("dsiner_request onError");
                    }
                });
    }

    private void testObservable() {
        Params params = new Params(API.MovieTop.rtpType);
        params.addParam(API.MovieTop.start, "1");
        params.addParam(API.MovieTop.count, "10");
        RxNet.getInstance(appContext).get(API.MovieTop.rtpType, params)
                .observable(MovieInfo.class)
                .map(new Function<MovieInfo, MovieInfo>() {
                    @Override
                    public MovieInfo apply(@NonNull MovieInfo info) throws Exception {
                        RxLog.d("dsiner_th Observable apply: " + Thread.currentThread().getId() + "--NAME--" + Thread.currentThread().getName());
                        return info;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<MovieInfo, MovieInfo>() {
                    @Override
                    public MovieInfo apply(@NonNull MovieInfo info) throws Exception {
                        RxLog.d("dsiner_th apply: " + Thread.currentThread().getId() + "--NAME--" + Thread.currentThread().getName());
                        return info;
                    }
                })
                .observeOn(Schedulers.io())
                .map(new Function<MovieInfo, MovieInfo>() {
                    @Override
                    public MovieInfo apply(@NonNull MovieInfo info) throws Exception {
                        RxLog.d("dsiner_th apply: " + Thread.currentThread().getId() + "--NAME--" + Thread.currentThread().getName());
                        return info;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<MovieInfo>() {
                    @Override
                    public void onNext(@NonNull MovieInfo info) {
                        RxLog.d("dsiner_th onNext: " + Thread.currentThread().getId() + "--NAME--" + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        RxLog.d("dsiner_th onError: " + Thread.currentThread().getId() + "--NAME--" + Thread.currentThread().getName());
                    }

                    @Override
                    public void onComplete() {
                        RxLog.d("dsiner_th onComplete: " + Thread.currentThread().getId() + "--NAME--" + Thread.currentThread().getName());
                    }
                });
    }

    private void testRetrofit() {
        RxNet.getRetrofit(appContext).create(RetrofitAPI.class)
                .get("")
                .subscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, ArrayList<Boolean>>() {
                    @Override
                    public ArrayList<Boolean> apply(@NonNull ResponseBody info) throws Exception {
                        return new ArrayList<>();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ArrayList<Boolean>>() {
                    @Override
                    public void onNext(ArrayList<Boolean> booleans) {

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
