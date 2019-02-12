package com.d.aster;

import android.app.Application;
import android.os.Environment;

import com.d.lib.aster.Aster;

/**
 * App
 * Created by D on 2017/10/27.
 */
public class App extends Application {
    public final static String PICTURE_PATH = Environment.getExternalStorageDirectory().getPath()
            + "/Aster/pictures/";
    public final static String DOWNLOAD_PATH = Environment.getExternalStorageDirectory().getPath()
            + "/Aster/download/";
    public final static String PIC_NAME = "young.jpg";

    @Override
    public void onCreate() {
        super.onCreate();
        // Init Aster
        Aster.init(getApplicationContext(), new AppAsterModule());

        // Or
        // Aster.init(getApplicationContext(), HttpModule.factory());
        // Aster.init(getApplicationContext(), OkHttpModule.factory());
        // Aster.init(getApplicationContext(), VolleyModule.factory());
        // Aster.init(getApplicationContext(), RetrofitModule.factory());
    }
}
