package com.d.rxnet.request;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;

import com.d.lib.rxnet.RxNet;
import com.d.lib.rxnet.listener.UploadCallBack;
import com.d.lib.rxnet.util.RxLog;
import com.d.lib.rxnet.util.RxUtil;

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
 * Request Test --> Upload
 * Created by D on 2017/11/15.
 */
public class Upload {
    private final String url = "http://www.qq.com/";
    private Context appContext;
    private ProgressDialog dialog;
    private boolean isRunning;

    public Upload(Activity activity) {
        appContext = activity.getApplicationContext();
        dialog = new ProgressDialog(activity);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置进度条的形式为圆形转动的进度条
        dialog.setMax(100);
    }

    public void testAll() {
        doTask("1.jpg", new Runnable() {
            @Override
            public void run() {
                testIns();
//                testNew();
            }
        });
    }

    private void testIns() {
        File file = getFile("1.jpg");
        RxNet.getInstance().upload(url)
                .addParam("token", "8888")
                .addParam("user", "0")
                .addParam("password", "0")
                .addFile("androidPicFile", file)
                .tag("uploadIns")
                .request(new UploadCallBack() {
                    @Override
                    public void onProgress(long currentLength, long totalLength) {
                        RxUtil.printThread("dsiner_theard onProgresss: ");
                        RxLog.d("dsiner_request onProgresss: -->upload: " + currentLength + " total: " + totalLength);
                        if (!dialog.isShowing()) {
                            dialog.setMessage("正在上传...");
                            dialog.show();
                        }
                        dialog.setProgress((int) (currentLength * 100 / totalLength));
                    }

                    @Override
                    public void onError(Throwable e) {
                        RxUtil.printThread("dsiner_theard onError: ");
                        RxLog.d("dsiner_request onError: " + e.getMessage());
                        dialog.dismiss();
                    }

                    @Override
                    public void onComplete() {
                        RxUtil.printThread("dsiner_theard onComplete: ");
                        RxLog.d("dsiner_request onComplete");
                        dialog.setProgress(100);
                        dialog.setMessage("上传完成");
                    }
                });
    }

    private void testNew() {
        File file = getFile("1.jpg");
        RxNet.upload(url)
                .connectTimeout(60 * 1000)
                .readTimeout(60 * 1000)
                .writeTimeout(60 * 1000)
                .retryCount(3)
                .retryDelayMillis(1000)
                .addImageFile("androidPicFile", file)
                .tag("uploadNew")
                .request(new UploadCallBack() {
                    @Override
                    public void onProgress(long currentLength, long totalLength) {
                        RxUtil.printThread("dsiner_theard onProgresss: ");
                        RxLog.d("dsiner_request onProgresss: -->upload: " + currentLength + " total: " + totalLength);
                    }

                    @Override
                    public void onError(Throwable e) {
                        RxUtil.printThread("dsiner_theard onError: ");
                        RxLog.d("dsiner_request onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        RxUtil.printThread("dsiner_theard onComplete: ");
                        RxLog.d("dsiner_request onComplete");
                    }
                });
    }

    private void doTask(final String name, final Runnable runnable) {
        if (isRunning) {
            return;
        }
        isRunning = true;
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> emitter) throws Exception {
                InputStream in = null;
                FileOutputStream out = null;
                File file = getFile(name);
                boolean success = false;
                if (!file.exists()) {
                    try {
                        in = appContext.getAssets().open(name);//从assets目录下复制
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
                isRunning = false;
                emitter.onNext(success);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean success) throws Exception {
                        if (runnable != null) {
                            runnable.run();
                        }
                    }
                });
    }

    private File getFile(String name) {
        File dir = new File(Environment.getExternalStorageDirectory().getPath() + "/test/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return new File(dir.getPath() + File.separator + name);
    }
}
