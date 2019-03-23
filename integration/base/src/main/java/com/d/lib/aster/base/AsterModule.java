package com.d.lib.aster.base;

import android.content.Context;
import android.support.annotation.NonNull;

import com.d.lib.aster.request.IBodyRequest;
import com.d.lib.aster.request.IDownloadRequest;
import com.d.lib.aster.request.IHttpRequest;
import com.d.lib.aster.request.IUploadRequest;

/**
 * AsterModule
 * Created by D on 2018/12/13.
 **/
public abstract class AsterModule {
    private static Registry REGISTRY;

    public abstract void applyOptions(@NonNull Context context, @NonNull Config.Builder builder);

    public void registerComponents(@NonNull Context context,
                                   @NonNull Registry registry) {

    }

    public abstract IRequestManager getManager();

    public static class Registry {
        AsterModule module;

        protected Registry() {
        }

        public Registry(@NonNull AsterModule module) {
            this.module = module;
        }

        @NonNull
        public void replace(Registry registry) {
            REGISTRY = registry;
        }
    }

    public static Registry getRegistry() {
        return REGISTRY;
    }

    public abstract Singleton getDefault();

    public abstract IHttpRequest get(String url);

    public abstract IHttpRequest get(String url, Params params);

    public abstract IBodyRequest post(String url);

    public abstract IBodyRequest post(String url, Params params);

    public abstract IBodyRequest put(String url);

    public abstract IBodyRequest put(String url, Params params);

    public abstract IHttpRequest head(String url);

    public abstract IBodyRequest delete(String url);

    public abstract IBodyRequest delete(String url, Params params);

    public abstract IBodyRequest options(String url);

    public abstract IBodyRequest options(String url, Params params);

    public abstract IBodyRequest patch(String url);

    public abstract IBodyRequest patch(String url, Params params);

    public abstract IDownloadRequest download(String url);

    public abstract IDownloadRequest download(String url, Params params);

    public abstract IUploadRequest upload(String url);

    /**
     * Singleton
     */
    public static abstract class Singleton {

        public abstract IHttpRequest.Singleton get(String url);

        public abstract IHttpRequest.Singleton get(String url, Params params);

        public abstract IBodyRequest.Singleton post(String url);

        public abstract IBodyRequest.Singleton post(String url, Params params);

        public abstract IBodyRequest.Singleton put(String url);

        public abstract IBodyRequest.Singleton put(String url, Params params);

        public abstract IHttpRequest.Singleton head(String url);

        public abstract IBodyRequest.Singleton delete(String url);

        public abstract IBodyRequest.Singleton delete(String url, Params params);

        public abstract IBodyRequest.Singleton options(String url);

        public abstract IBodyRequest.Singleton options(String url, Params params);

        public abstract IBodyRequest.Singleton patch(String url);

        public abstract IBodyRequest.Singleton patch(String url, Params params);

        public abstract IDownloadRequest.Singleton download(String url);

        public abstract IDownloadRequest.Singleton download(String url, Params params);

        public abstract IUploadRequest.Singleton upload(String url);
    }
}
