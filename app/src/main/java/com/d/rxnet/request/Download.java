package com.d.rxnet.request;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Environment;

import com.d.lib.rxnet.RxNet;
import com.d.lib.rxnet.listener.DownloadCallBack;
import com.d.lib.rxnet.util.RxLog;
import com.d.lib.rxnet.util.RxUtil;

import java.io.File;

/**
 * Request Test --> Down
 * Created by D on 2017/10/26.
 */
public class Download {
    private ProgressDialog dialog;

    public Download(Activity activity) {
        dialog = new ProgressDialog(activity);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setMax(100);
    }

    public void testAll() {
        RxUtil.deleteFile(new File(Environment.getExternalStorageDirectory().getPath() + "/test/"));
        testIns();
//        testNew();
    }

    private void testIns() {
        String url = "http://imtt.dd.qq.com/16891/4EA3DBDFC3F34E43C1D76CEE67593D67.apk?fsname=com.d.music_1.0.1_2.apk&csr=1bbd";
        RxNet.getInstance().download(url)
                .tag("downloadIns")
                .request(Environment.getExternalStorageDirectory().getPath() + "/test/", "" + System.currentTimeMillis() + ".mp3", new DownloadCallBack() {

                    @Override
                    public void onProgress(long currentLength, long totalLength) {
                        RxUtil.printThread("dsiner_theard onProgresss: ");
                        RxLog.d("dsiner_request onProgresss: -->download: " + currentLength + " total: " + totalLength);
                        if (!dialog.isShowing()) {
                            dialog.setMessage("正在下载...");
                            dialog.show();
                        }
                        dialog.setProgress((int) (currentLength * 100 / totalLength));
                    }

                    @Override
                    public void onError(Throwable e) {
                        RxUtil.printThread("dsiner_theard onError: ");
                        RxLog.d("dsiner_request onError " + e.getMessage());
                        dialog.dismiss();
                    }

                    @Override
                    public void onComplete() {
                        RxUtil.printThread("dsiner_theard onComplete: ");
                        RxLog.d("dsiner_request onComplete:");
                        dialog.setProgress(100);
                        dialog.setMessage("下载完成");
                    }
                });
    }

    private void testNew() {
        String url = "http://imtt.dd.qq.com/16891/D44E78C914AA4D70CD4422401A7E7E5C.apk?fsname=com.tencent.mobileqq_7.2.5_744.apk&csr=1bbd";
        RxNet.download(url)
                .connectTimeout(60 * 1000)
                .readTimeout(60 * 1000)
                .writeTimeout(60 * 1000)
                .retryCount(3)
                .retryDelayMillis(1000)
                .tag("downloadNew")
                .request(Environment.getExternalStorageDirectory().getPath() + "/test/", "" + System.currentTimeMillis() + ".mp3", new DownloadCallBack() {

                    @Override
                    public void onProgress(long currentLength, long totalLength) {
                        RxUtil.printThread("dsiner_theard onProgresss: ");
                        RxLog.d("dsiner_request onProgresss: -->download: " + currentLength + " total: " + totalLength);
                    }

                    @Override
                    public void onError(Throwable e) {
                        RxUtil.printThread("dsiner_theard onError: ");
                        RxLog.d("dsiner_request onError " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        RxUtil.printThread("dsiner_theard onComplete: ");
                        RxLog.d("dsiner_request onComplete:");
                    }
                });
    }
}
