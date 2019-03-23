package com.d.lib.aster;

import android.content.Context;
import android.support.annotation.NonNull;

import com.d.lib.aster.base.AsterModule;
import com.d.lib.aster.base.AsterModule.Singleton;
import com.d.lib.aster.base.Config;
import com.d.lib.aster.base.IRequestManager;
import com.d.lib.aster.base.Params;
import com.d.lib.aster.request.IBodyRequest;
import com.d.lib.aster.request.IDownloadRequest;
import com.d.lib.aster.request.IHttpRequest;
import com.d.lib.aster.request.IUploadRequest;

/**
 * Created by D on 2017/10/24.
 */
public class Aster {
    private volatile static AsterModule mAster;

    public static AsterModule getAster() {
        if (mAster == null) {
            synchronized (Aster.class) {
                if (mAster == null) {
                    mAster = getDefaultAsterModule();
                }
            }
        }
        return mAster;
    }

    private static AsterModule getDefaultAsterModule() {
        AsterModule result = null;
        try {
            result = (AsterModule) Class.forName("com.d.lib.aster.integration.okhttp3.OkHttpModule")
                    .getDeclaredConstructor().newInstance();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if (result != null) {
            return result;
        }

        try {
            result = (AsterModule) Class.forName("com.d.lib.aster.integration.retrofit.RetrofitModule")
                    .getDeclaredConstructor().newInstance();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if (result != null) {
            return result;
        }

        try {
            result = (AsterModule) Class.forName("com.d.lib.aster.integration.http.HttpModule")
                    .getDeclaredConstructor().newInstance();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if (result != null) {
            return result;
        }

        throw new RuntimeException("Not find the default adapter, such as OkHttpModule,"
                + " you need to integrate one");
    }

    public static void init(Context context, AsterModule module) {
        context = context.getApplicationContext();
        module.applyOptions(context, new Config.Builder(context));
        module.registerComponents(context, new AsterModule.Registry(module));
        mAster = module;
    }

    public static Config.Builder init() {
        return new Config.Builder();
    }

    public static Config.Builder init(@NonNull Context context) {
        return new Config.Builder(context);
    }

    public static IRequestManager getManager() {
        return getAster().getManager();
    }

    public static Singleton getDefault() {
        return getAster().getDefault();
    }

    private Aster() {
    }

    public static IHttpRequest get(String url) {
        return getAster().get(url);
    }

    public static IHttpRequest get(String url, Params params) {
        return getAster().get(url, params);
    }

    public static IBodyRequest post(String url) {
        return getAster().post(url);
    }

    public static IBodyRequest post(String url, Params params) {
        return getAster().post(url, params);
    }

    public static IBodyRequest put(String url) {
        return getAster().put(url);
    }

    public static IBodyRequest put(String url, Params params) {
        return getAster().put(url, params);
    }

    public static IHttpRequest head(String url) {
        return getAster().head(url);
    }

    public static IBodyRequest delete(String url) {
        return getAster().delete(url);
    }

    public static IBodyRequest delete(String url, Params params) {
        return getAster().delete(url, params);
    }

    public static IBodyRequest options(String url) {
        return getAster().options(url);
    }

    public static IBodyRequest options(String url, Params params) {
        return getAster().options(url, params);
    }

    public static IBodyRequest patch(String url) {
        return getAster().patch(url);
    }

    public static IBodyRequest patch(String url, Params params) {
        return getAster().patch(url, params);
    }

    public static IDownloadRequest download(String url) {
        return getAster().download(url);
    }

    public static IDownloadRequest download(String url, Params params) {
        return getAster().download(url, params);
    }

    public static IUploadRequest upload(String url) {
        return getAster().upload(url);
    }
}
