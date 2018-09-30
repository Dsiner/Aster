package com.d.rxnet.activity;

import com.d.lib.rxnet.RxNet;
import com.d.lib.rxnet.api.RetrofitAPI;
import com.d.lib.rxnet.base.Params;
import com.d.lib.rxnet.callback.AsyncCallback;
import com.d.lib.rxnet.callback.SimpleCallback;
import com.d.lib.rxnet.utils.ULog;
import com.d.lib.rxnet.utils.Util;
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
 * Request --> Get
 * Created by D on 2017/10/26.
 */
public class Get extends Request {

    @Override
    protected void init() {
        mUrl = API.MovieTop.rtpType;
        etUrl.setText(mUrl);
        etUrl.setSelection(etUrl.getText().toString().length());
    }

    @Override
    protected void request() {
        testIns();
        // testNew();
        // testObservable();
        // testRetrofit();
    }

    private void testIns() {
        Params params = new Params(mUrl);
        params.addParam(API.MovieTop.start, "0");
        params.addParam(API.MovieTop.count, "10");
        RxNet.getDefault().get(API.MovieTop.rtpType, params)
                .request(new SimpleCallback<MovieInfo>() {
                    @Override
                    public void onSuccess(MovieInfo response) {
                        Util.printThread("dsiner_theard onSuccess: ");
                        ULog.d("dsiner_request onSuccess: " + response);
                        formatPrinting(response.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Util.printThread("dsiner_theard onError: ");
                        ULog.d("dsiner_request onError");
                    }
                });
    }

    private void testNew() {
        Params params = new Params(mUrl);
        params.addParam(API.MovieTop.start, "1");
        params.addParam(API.MovieTop.count, "10");
        RxNet.get("top250", params)
                .baseUrl("https://api.douban.com/v2/movie/")
                .connectTimeout(5 * 1000)
                .readTimeout(5 * 1000)
                .writeTimeout(5 * 1000)
                .request(new AsyncCallback<MovieInfo, String>() {
                    @Override
                    public String apply(@NonNull MovieInfo info) throws Exception {
                        Util.printThread("dsiner_theard apply: ");
                        ULog.d("dsiner_request apply");
                        int size = info.subjects.size();
                        return "" + size;
                    }

                    @Override
                    public void onSuccess(String response) {
                        Util.printThread("dsiner_theard onSuccess: ");
                        ULog.d("dsiner_request onSuccess: " + response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Util.printThread("dsiner_theard onError: ");
                        ULog.d("dsiner_request onError");
                    }
                });
    }

    private void testObservable() {
        Params params = new Params(mUrl);
        params.addParam(API.MovieTop.start, "1");
        params.addParam(API.MovieTop.count, "10");
        RxNet.getDefault().get(API.MovieTop.rtpType, params)
                .observable(MovieInfo.class)
                .map(new Function<MovieInfo, MovieInfo>() {
                    @Override
                    public MovieInfo apply(@NonNull MovieInfo info) throws Exception {
                        Util.printThread("dsiner_theard apply: ");
                        return info;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<MovieInfo, MovieInfo>() {
                    @Override
                    public MovieInfo apply(@NonNull MovieInfo info) throws Exception {
                        Util.printThread("dsiner_theard apply: ");
                        return info;
                    }
                })
                .observeOn(Schedulers.io())
                .map(new Function<MovieInfo, MovieInfo>() {
                    @Override
                    public MovieInfo apply(@NonNull MovieInfo info) throws Exception {
                        Util.printThread("dsiner_theard apply: ");
                        return info;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<MovieInfo>() {
                    @Override
                    public void onNext(@NonNull MovieInfo info) {
                        Util.printThread("dsiner_theard onNext: ");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Util.printThread("dsiner_theard onError: ");
                    }

                    @Override
                    public void onComplete() {
                        Util.printThread("dsiner_theard onComplete: ");
                    }
                });
    }

    private void testRetrofit() {
        RxNet.getRetrofit().create(RetrofitAPI.class)
                .get(mUrl)
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
