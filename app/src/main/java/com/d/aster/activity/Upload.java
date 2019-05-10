package com.d.aster.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;

import com.d.aster.App;
import com.d.aster.R;
import com.d.lib.aster.Aster;
import com.d.lib.aster.callback.ProgressCallback;
import com.d.lib.aster.callback.SimpleCallback;
import com.d.lib.aster.scheduler.Observable;
import com.d.lib.aster.scheduler.callback.Observer;
import com.d.lib.aster.scheduler.callback.Task;
import com.d.lib.aster.scheduler.schedule.Schedulers;
import com.d.lib.aster.utils.ULog;
import com.d.lib.aster.utils.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
        doTask(App.PIC_NAME, new Runnable() {
            @Override
            public void run() {
                setDialogProgress(0, 1, false);
                requestImpl(TYPE_SINGLETON);
            }
        });
    }

    @Override
    protected void requestSingleton() {
        File file = getFile(App.PIC_NAME);
        Aster.getDefault().upload(mUrl)
                .tag(mUrl)
                .addParam("token", "008")
                .addParam("user", "0")
                .addParam("password", "0")
                .addFile("androidPicFile", file, new ProgressCallback() {

                    @Override
                    public void onStart() {
                        Util.printThread("dsiner_theard onStart");
                        ULog.d("dsiner_request--> onStart");
                    }

                    @Override
                    public void onProgress(long currentLength, long totalLength) {
                        Util.printThread("dsiner_theard onProgresss");
                        ULog.d("dsiner_request--> onProgresss upload: " + currentLength + " total: " + totalLength);
                        setDialogProgress(currentLength, totalLength, false);
                    }

                    @Override
                    public void onSuccess() {
                        Util.printThread("dsiner_theard onSuccess");
                        ULog.d("dsiner_request--> onSuccess");
                        setDialogProgress(1, 1, true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Util.printThread("dsiner_theard onError");
                        ULog.d("dsiner_request--> onError: " + e.getMessage());
                    }

                    @Override
                    public void onCancel() {
                        Util.printThread("dsiner_theard onCancel");
                        ULog.d("dsiner_request--> onCancel");
                    }
                })
                .request(new SimpleCallback<String>() {
                    @Override
                    public void onSuccess(String response) {
                        Util.printThread("dsiner_theard onComplete / All");
                        ULog.d("dsiner_request--> onComplete / All: " + response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Util.printThread("dsiner_theard onError / All");
                        ULog.d("dsiner_request--> onError / All: " + e.getMessage());
                    }
                });
    }

    @Override
    protected void requestNew() {
        File file = getFile(App.PIC_NAME);
        Aster.upload(mUrl)
                .connectTimeout(60 * 1000)
                .readTimeout(60 * 1000)
                .writeTimeout(60 * 1000)
                .retryCount(3)
                .retryDelayMillis(1000)
                .addImageFile("androidPicFile", file, new ProgressCallback() {
                    @Override
                    public void onStart() {
                        Util.printThread("dsiner_theard onStart");
                        ULog.d("dsiner_request--> onStart");
                    }

                    @Override
                    public void onProgress(long currentLength, long totalLength) {
                        Util.printThread("dsiner_theard onProgresss");
                        ULog.d("dsiner_request--> onProgresss upload: " + currentLength + " total: " + totalLength);
                        setDialogProgress(currentLength, totalLength, false);
                    }

                    @Override
                    public void onSuccess() {
                        Util.printThread("dsiner_theard onComplete");
                        ULog.d("dsiner_request--> onComplete");
                        setDialogProgress(1, 1, true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Util.printThread("dsiner_theard onError");
                        ULog.d("dsiner_request--> onError: " + e.getMessage());
                    }

                    @Override
                    public void onCancel() {
                        Util.printThread("dsiner_theard onCancel");
                        ULog.d("dsiner_request--> onCancel");
                    }
                })
                .request(new SimpleCallback<String>() {
                    @Override
                    public void onSuccess(String response) {
                        Util.printThread("dsiner_theard onComplete / All");
                        ULog.d("dsiner_request--> onComplete / All: " + response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Util.printThread("dsiner_theard onError / All");
                        ULog.d("dsiner_request--> onError / All: " + e.getMessage());
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void doTask(final String name, final Runnable runnable) {
        if (mIsRunning) {
            return;
        }
        mIsRunning = true;
        Observable.create(new Task<Boolean>() {
            @Override
            public Boolean run() throws Exception {
                return checkFile(name);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onNext(@NonNull Boolean success) {
                        mIsRunning = false;
                        if (runnable != null) {
                            runnable.run();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

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
        File dir = new File(App.PICTURE_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return new File(dir.getPath() + File.separator + name);
    }

    private void showDialog() {
        if (!mDialog.isShowing() && !isFinishing()) {
            mDialog.setMessage(getResources().getString(R.string.uploading));
            mDialog.show();
        }
    }

    private void setDialogProgress(long currentLength, long totalLength, boolean finish) {
        showDialog();
        mDialog.setMessage(!finish ? getResources().getString(R.string.uploading)
                : getResources().getString(R.string.uploaded));
        mDialog.setProgress((int) (currentLength * 100f / totalLength));
    }

    @Override
    protected void onDestroy() {
        Aster.getManager().cancel(mUrl);
        super.onDestroy();
    }
}
