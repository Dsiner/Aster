package com.d.rxnet.activity;

import com.d.lib.rxnet.RxNet;
import com.d.lib.rxnet.api.RetrofitAPI;
import com.d.lib.rxnet.base.Params;
import com.d.lib.rxnet.callback.AsyncCallback;
import com.d.lib.rxnet.utils.ULog;
import com.d.lib.rxnet.utils.Util;
import com.d.rxnet.api.API;
import com.d.rxnet.model.MovieInfo;
import com.google.gson.Gson;

import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
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
        requestImp(TYPE_SINGLETON);
    }

    @Override
    protected void requestSingleton() {
        Params params = new Params(mUrl);
        params.addParam(API.MovieTop.start, "0");
        params.addParam(API.MovieTop.count, "10");
        RxNet.getDefault().post(API.MovieTop.rtpType, params)
                .request(new AsyncCallback<String, String>() {
                    @Override
                    public String apply(@NonNull String info) throws Exception {
                        Util.printThread("dsiner_theard apply");
                        ULog.d("dsiner_request--> apply");
                        return "" + info;
                    }

                    @Override
                    public void onSuccess(String response) {
                        Util.printThread("dsiner_theard onSuccess");
                        ULog.d("dsiner_request--> onSuccess: " + response);
                        formatPrinting(response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Util.printThread("dsiner_theard onError");
                        ULog.d("dsiner_request--> onError");
                    }
                });
    }

    @Override
    protected void requestNew() {
        Params params = new Params(mUrl);
        params.addParam(API.MovieTop.start, "1");
        params.addParam(API.MovieTop.count, "10");
        RxNet.post("top250", params)
                .baseUrl("https://api.douban.com/v2/movie/")
                .connectTimeout(5 * 1000)
                .readTimeout(5 * 1000)
                .writeTimeout(5 * 1000)
                .request(new AsyncCallback<MovieInfo, String>() {
                    @Override
                    public String apply(@NonNull MovieInfo info) throws Exception {
                        Util.printThread("dsiner_theard apply");
                        ULog.d("dsiner_request--> apply");
                        int size = info.subjects.size();
                        return "" + size;
                    }

                    @Override
                    public void onSuccess(String response) {
                        Util.printThread("dsiner_theard onSuccess");
                        ULog.d("dsiner_request--> onSuccess: " + response);
                        formatPrinting(response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Util.printThread("dsiner_theard onError");
                        ULog.d("dsiner_request--> onError");
                    }
                });
    }

    @Override
    protected void requestObservable() {
        RxNet.getDefault().post(mUrl)
                .observable(ResponseBody.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResponseBody>() {
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

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void requestRetrofit() {
        RxNet.getRetrofit().create(RetrofitAPI.class)
                .post(mUrl)
                .subscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, MovieInfo>() {
                    @Override
                    public MovieInfo apply(ResponseBody responseBody) throws Exception {
                        return new Gson().fromJson(responseBody.string(), MovieInfo.class);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MovieInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {

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
