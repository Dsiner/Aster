package com.d.rxnet.request;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;

import com.d.lib.rxnet.RxNet;
import com.d.lib.rxnet.exception.ApiException;
import com.d.lib.rxnet.listener.DownloadCallBack;
import com.d.lib.rxnet.util.RxLog;

/**
 * Request Test --> Down
 * Created by D on 2017/10/26.
 */
public class Down {
    private String fileUrl = "http://m10.music.126.net/20171027150016/9b65067112ac9ab0df434b955d2aadd4/ymusic/9e23/460b/3237/53cd858401aff035a9b3139d78d78b82.mp3";
    private Context appContext;

    private ProgressDialog dialog;

    public Down(Activity activity) {
        appContext = activity.getApplicationContext();
        dialog = new ProgressDialog(activity);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置进度条的形式为圆形转动的进度条
        dialog.setMessage("正在下载...");
        dialog.setTitle("下载文件");
        dialog.setMax(100);
    }

    public void testAll() {
        testIns();
        testNew();
    }

    public void testIns() {
        RxNet.getInstance(appContext).download(fileUrl)
                .request(Environment.getExternalStorageDirectory().getPath() + "/test/", "" + System.currentTimeMillis() + ".mp3", new DownloadCallBack() {
                    @Override
                    public void onStart(long total) {
                        RxLog.d("dsiner_th_onStart: " + Thread.currentThread().getId() + "--NAME--" + Thread.currentThread().getName());
                        RxLog.d("dsiner_down onStart: -->total: " + total);
//                        dialog.show();
                    }

                    @Override
                    public void onProgresss(long download, long total) {
                        RxLog.d("dsiner_th_onProgresss: " + Thread.currentThread().getId() + "--NAME--" + Thread.currentThread().getName());
                        RxLog.d("dsiner_down onProgresss: -->download: " + download + " total: " + total);
//                        dialog.setProgress((int) (download * 100 / total));
                    }

                    @Override
                    public void onComplete() {
                        RxLog.d("dsiner_th_onComplete: " + Thread.currentThread().getId() + "--NAME--" + Thread.currentThread().getName());
                        RxLog.d("dsiner_down onComplete:");
//                        dialog.setMessage("下载完成");
                    }

                    @Override
                    public void onError(ApiException e) {
                        RxLog.d("dsiner_th_onError: " + Thread.currentThread().getId() + "--NAME--" + Thread.currentThread().getName());
                        RxLog.d("dsiner_down onError " + e.getMessage());
//                        dialog.dismiss();
                    }
                });
    }

    public void testNew() {
        new RxNet(appContext).download(fileUrl)
                .connectTimeout(60 * 1000)
                .readTimeout(60 * 1000)
                .writeTimeout(60 * 1000)
                .retryCount(3)
                .retryDelayMillis(300)
                .request(Environment.getExternalStorageDirectory().getPath() + "/test/", "" + System.currentTimeMillis() + ".mp3", new DownloadCallBack() {
                    @Override
                    public void onStart(long total) {
                        RxLog.d("dsiner_th_onStart: " + Thread.currentThread().getId() + "--NAME--" + Thread.currentThread().getName());
                        RxLog.d("dsiner_down onStart: -->total: " + total);
                    }

                    @Override
                    public void onProgresss(long download, long total) {
                        RxLog.d("dsiner_th_onProgresss: " + Thread.currentThread().getId() + "--NAME--" + Thread.currentThread().getName());
                        RxLog.d("dsiner_down onProgresss: -->download: " + download + " total: " + total);
                    }

                    @Override
                    public void onComplete() {
                        RxLog.d("dsiner_th_onComplete: " + Thread.currentThread().getId() + "--NAME--" + Thread.currentThread().getName());
                        RxLog.d("dsiner_down onComplete:");
                    }

                    @Override
                    public void onError(ApiException e) {
                        RxLog.d("dsiner_th_onError: " + Thread.currentThread().getId() + "--NAME--" + Thread.currentThread().getName());
                        RxLog.d("dsiner_down onError " + e.getMessage());
                    }
                });
    }
}
