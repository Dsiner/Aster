package com.d.rxnet.activity;

import android.app.ProgressDialog;

import com.d.lib.rxnet.RxNet;
import com.d.lib.rxnet.callback.DownloadCallback;
import com.d.lib.rxnet.utils.ULog;
import com.d.lib.rxnet.utils.Util;
import com.d.rxnet.App;

import java.io.File;

/**
 * Request --> Download
 * Created by D on 2017/10/26.
 */
public class Download extends Request {
    private final String mUrl1 = "http://imtt.dd.qq.com/16891/4EA3DBDFC3F34E43C1D76CEE67593D67.apk?fsname=com.d.music_1.0.1_2.apk&csr=1bbd";
    private final String mUrl2 = "http://imtt.dd.qq.com/16891/D44E78C914AA4D70CD4422401A7E7E5C.apk?fsname=com.tencent.mobileqq_7.2.5_744.apk&csr=1bbd";

    private ProgressDialog mDialog;

    @Override
    protected void init() {
        mUrl = mUrl1;
        etUrl.setText(mUrl);
        etUrl.setSelection(etUrl.getText().toString().length());

        mDialog = new ProgressDialog(this);
        mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mDialog.setMax(100);
    }

    @Override
    protected void request() {
        Util.deleteFile(new File(App.mPath));
        setDialogProgress(0, 1, false);
        testIns();
        // testNew();
    }

    private void testIns() {
        RxNet.getDefault().download(mUrl1)
                .tag("downloadIns")
                .request(App.mPath, "" + System.currentTimeMillis() + ".mp3", new DownloadCallback() {

                    @Override
                    public void onProgress(long currentLength, long totalLength) {
                        Util.printThread("dsiner_theard onProgresss: ");
                        ULog.d("dsiner_request onProgresss: -->download: " + currentLength + " total: " + totalLength);
                        setDialogProgress(currentLength, totalLength, false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Util.printThread("dsiner_theard onError: ");
                        ULog.d("dsiner_request onError " + e.getMessage());
                        mDialog.dismiss();
                    }

                    @Override
                    public void onComplete() {
                        Util.printThread("dsiner_theard onComplete: ");
                        ULog.d("dsiner_request onComplete:");
                        setDialogProgress(1, 1, true);
                    }
                });
    }

    private void testNew() {
        RxNet.download(mUrl2)
                .connectTimeout(60 * 1000)
                .readTimeout(60 * 1000)
                .writeTimeout(60 * 1000)
                .retryCount(3)
                .retryDelayMillis(1000)
                .tag("downloadNew")
                .request(App.mPath, "" + System.currentTimeMillis() + ".mp3", new DownloadCallback() {

                    @Override
                    public void onProgress(long currentLength, long totalLength) {
                        Util.printThread("dsiner_theard onProgresss: ");
                        ULog.d("dsiner_request onProgresss: -->download: " + currentLength + " total: " + totalLength);
                        setDialogProgress(currentLength, totalLength, false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Util.printThread("dsiner_theard onError: ");
                        ULog.d("dsiner_request onError " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Util.printThread("dsiner_theard onComplete: ");
                        ULog.d("dsiner_request onComplete:");
                        setDialogProgress(1, 1, true);
                    }
                });
    }

    private void showDialog() {
        if (!mDialog.isShowing() && !isFinishing()) {
            mDialog.setMessage("正在下载...");
            mDialog.show();
        }
    }

    private void setDialogProgress(long currentLength, long totalLength, boolean finish) {
        showDialog();
        mDialog.setMessage(!finish ? "正在下载..." : "下载完成!");
        mDialog.setProgress((int) (currentLength * 100f / totalLength));
    }
}
