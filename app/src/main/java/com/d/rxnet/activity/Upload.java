package com.d.rxnet.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;

import com.d.lib.rxnet.RxNet;
import com.d.lib.rxnet.callback.UploadCallback;
import com.d.lib.rxnet.utils.ULog;
import com.d.lib.rxnet.utils.Util;
import com.d.rxnet.App;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Request --> Upload
 * Created by D on 2017/11/15.
 */
public class Upload extends Request {
    private ProgressDialog mDialog;
    private boolean mIsRunning;

    @Override
    protected void init() {
        mUrl = "http://www.qq.com/";
        etUrl.setText(mUrl);
        etUrl.setSelection(etUrl.getText().toString().length());

        mDialog = new ProgressDialog(this);
        mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mDialog.setMax(100);
    }

    @Override
    protected void request() {
        doTask("1.jpg", new Runnable() {
            @Override
            public void run() {
                setDialogProgress(0, 1, false);
                testIns();
                // testNew();
            }
        });
    }

    private void testIns() {
        File file = getFile("1.jpg");
        RxNet.getDefault().upload(mUrl)
                .addParam("token", "008")
                .addParam("user", "0")
                .addParam("password", "0")
                .addFile("androidPicFile", file)
                .tag("uploadIns")
                .request(new UploadCallback() {
                    @Override
                    public void onProgress(long currentLength, long totalLength) {
                        Util.printThread("dsiner_theard onProgresss: ");
                        ULog.d("dsiner_request onProgresss--> upload: " + currentLength + " total: " + totalLength);
                        setDialogProgress(currentLength, totalLength, false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Util.printThread("dsiner_theard onError: ");
                        ULog.d("dsiner_request onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Util.printThread("dsiner_theard onComplete: ");
                        ULog.d("dsiner_request onComplete");
                        setDialogProgress(1, 1, true);
                    }
                });
    }

    private void testNew() {
        File file = getFile("1.jpg");
        RxNet.upload(mUrl)
                .connectTimeout(60 * 1000)
                .readTimeout(60 * 1000)
                .writeTimeout(60 * 1000)
                .retryCount(3)
                .retryDelayMillis(1000)
                .addImageFile("androidPicFile", file)
                .tag("uploadNew")
                .request(new UploadCallback() {
                    @Override
                    public void onProgress(long currentLength, long totalLength) {
                        Util.printThread("dsiner_theard onProgresss: ");
                        ULog.d("dsiner_request onProgresss--> upload: " + currentLength + " total: " + totalLength);
                        setDialogProgress(currentLength, totalLength, false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Util.printThread("dsiner_theard onError: ");
                        ULog.d("dsiner_request onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Util.printThread("dsiner_theard onComplete: ");
                        ULog.d("dsiner_request onComplete");
                        setDialogProgress(1, 1, true);
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void doTask(final String name, final Runnable runnable) {
        if (mIsRunning) {
            return;
        }
        mIsRunning = true;
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> emitter) throws Exception {
                boolean success = checkFile(name);
                emitter.onNext(success);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean success) throws Exception {
                        mIsRunning = false;
                        if (runnable != null) {
                            runnable.run();
                        }
                    }
                });
    }

    private boolean checkFile(String name) {
        InputStream in = null;
        FileOutputStream out = null;
        File file = getFile(name);
        boolean success = false;
        if (!file.exists()) {
            try {
                // Copy from the assets directory
                in = mContext.getAssets().open(name);
                out = new FileOutputStream(file);
                int length;
                byte[] buf = new byte[1024];
                while ((length = in.read(buf)) != -1) {
                    out.write(buf, 0, length);
                }
                out.flush();
                success = true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) out.close();
                    if (in != null) in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return success;
    }

    private File getFile(String name) {
        File dir = new File(App.mPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return new File(dir.getPath() + File.separator + name);
    }

    private void showDialog() {
        if (!mDialog.isShowing() && !isFinishing()) {
            mDialog.setMessage("正在上传...");
            mDialog.show();
        }
    }

    private void setDialogProgress(long currentLength, long totalLength, boolean finish) {
        showDialog();
        mDialog.setMessage(!finish ? "正在上传..." : "上传完成!");
        mDialog.setProgress((int) (currentLength * 100f / totalLength));
    }
}
