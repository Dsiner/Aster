package com.d.rxnet.request;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;

import com.d.lib.rxnet.RxNet;
import com.d.lib.rxnet.exception.ApiException;
import com.d.lib.rxnet.listener.DownloadCallBack;
import com.d.lib.rxnet.util.RxLog;
import com.d.lib.rxnet.util.RxUtil;

import java.io.File;

/**
 * Request Test --> Down
 * Created by D on 2017/10/26.
 */
public class Down {
    private String url = "http://imtt.dd.qq.com/16891/4EA3DBDFC3F34E43C1D76CEE67593D67.apk?fsname=com.d.music_1.0.1_2.apk&csr=1bbd";
    private String url1 = "http://imtt.dd.qq.com/16891/D44E78C914AA4D70CD4422401A7E7E5C.apk?fsname=com.tencent.mobileqq_7.2.5_744.apk&csr=1bbd";
    private Context appContext;

    private ProgressDialog dialog;

    public Down(Activity activity) {
        appContext = activity.getApplicationContext();
        dialog = new ProgressDialog(activity);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置进度条的形式为圆形转动的进度条
        dialog.setMax(100);
    }

    public void testAll() {
        RxUtil.deleteFile(new File(Environment.getExternalStorageDirectory().getPath() + "/test/"));
        testIns();
        testNew();
    }

    public void testIns() {
        RxNet.getInstance(appContext).download(url)
                .request(Environment.getExternalStorageDirectory().getPath() + "/test/", "" + System.currentTimeMillis() + ".mp3", new DownloadCallBack() {

                    @Override
                    public void onProgresss(long download, long total) {
//                        RxLog.d("dsiner_th_onProgresss: " + Thread.currentThread().getId() + "--NAME--" + Thread.currentThread().getName());
//                        RxLog.d("dsiner_down onProgresss: -->download: " + download + " total: " + total);
                        if (!dialog.isShowing()) {
                            dialog.setMessage("正在下载...");
                            dialog.show();
                        }
                        dialog.setProgress((int) (download * 100 / total));
                    }

                    @Override
                    public void onError(ApiException e) {
//                        RxLog.d("dsiner_th_onError: " + Thread.currentThread().getId() + "--NAME--" + Thread.currentThread().getName());
//                        RxLog.d("dsiner_down onError " + e.getMessage());
                        dialog.dismiss();
                    }

                    @Override
                    public void onComplete() {
//                        RxLog.d("dsiner_th_onComplete: " + Thread.currentThread().getId() + "--NAME--" + Thread.currentThread().getName());
//                        RxLog.d("dsiner_down onComplete:");
                        dialog.setProgress(100);
                        dialog.setMessage("下载完成");
                    }
                });
    }

    public void testNew() {
        new RxNet(appContext).download(url)
                .connectTimeout(60 * 1000)
                .readTimeout(60 * 1000)
                .writeTimeout(60 * 1000)
                .retryCount(3)
                .retryDelayMillis(1000)
                .request(Environment.getExternalStorageDirectory().getPath() + "/test/", "" + System.currentTimeMillis() + ".mp3", new DownloadCallBack() {

                    @Override
                    public void onProgresss(long download, long total) {
                        RxLog.d("dsiner_th_onProgresss: " + Thread.currentThread().getId() + "--NAME--" + Thread.currentThread().getName());
                        RxLog.d("dsiner_down onProgresss: -->download: " + download + " total: " + total);
                    }

                    @Override
                    public void onError(ApiException e) {
                        RxLog.d("dsiner_th_onError: " + Thread.currentThread().getId() + "--NAME--" + Thread.currentThread().getName());
                        RxLog.d("dsiner_down onError " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        RxLog.d("dsiner_th_onComplete: " + Thread.currentThread().getId() + "--NAME--" + Thread.currentThread().getName());
                        RxLog.d("dsiner_down onComplete:");
                    }
                });
    }
}
