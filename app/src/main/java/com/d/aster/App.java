package com.d.aster;

import android.app.Application;
import android.os.Environment;

import com.d.aster.api.AppAsterModule;
import com.d.lib.aster.Aster;

/**
 * App
 * Created by D on 2017/10/27.
 */
public class App extends Application {
    public static final String PICTURE_PATH = Environment.getExternalStorageDirectory().getPath()
            + "/Aster/pictures/";
    public static final String DOWNLOAD_PATH = Environment.getExternalStorageDirectory().getPath()
            + "/Aster/download/";

    public static final String PIC_NAME = "young.jpg";

    @Override
    public void onCreate() {
        super.onCreate();
        // Init
        Aster.init(getApplicationContext(), new AppAsterModule());

        // Or
        // Aster.init(getApplicationContext(), HttpModule.factory());
        // Aster.init(getApplicationContext(), OkHttpModule.factory());
        // Aster.init(getApplicationContext(), VolleyModule.factory());
        // Aster.init(getApplicationContext(), RetrofitModule.factory());
    }
}
