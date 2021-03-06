package com.d.lib.aster.integration.http;

import android.content.Context;
import android.support.annotation.NonNull;

import com.d.lib.aster.base.AsterModule;
import com.d.lib.aster.base.Config;
import com.d.lib.aster.base.IRequestManager;
import com.d.lib.aster.base.Params;
import com.d.lib.aster.integration.http.request.DeleteRequest;
import com.d.lib.aster.integration.http.request.DownloadRequest;
import com.d.lib.aster.integration.http.request.GetRequest;
import com.d.lib.aster.integration.http.request.HeadRequest;
import com.d.lib.aster.integration.http.request.OptionRequest;
import com.d.lib.aster.integration.http.request.PatchRequest;
import com.d.lib.aster.integration.http.request.PostRequest;
import com.d.lib.aster.integration.http.request.PutRequest;
import com.d.lib.aster.integration.http.request.UploadRequest;

/**
 * Created by D on 2017/10/24.
 */
public class HttpModule extends AsterModule {

    public static HttpModule factory() {
        return new HttpModule();
    }

    @Override
    public void applyOptions(@NonNull Context context, @NonNull Config.Builder builder) {
        builder.build();
    }

    @Override
    public IRequestManager getManager() {
        return RequestManagerImpl.getIns();
    }

    @Override
    public Singleton getDefault() {
        return Default.INSTANCE;
    }

    @Override
    public GetRequest get(String url) {
        return new GetRequest(url);
    }

    @Override
    public GetRequest get(String url, Params params) {
        return new GetRequest(url, params);
    }

    @Override
    public PostRequest post(String url) {
        return new PostRequest(url);
    }

    @Override
    public PostRequest post(String url, Params params) {
        return new PostRequest(url, params);
    }

    @Override
    public PutRequest put(String url) {
        return new PutRequest(url);
    }

    @Override
    public PutRequest put(String url, Params params) {
        return new PutRequest(url, params);
    }

    @Override
    public HeadRequest head(String url) {
        return new HeadRequest(url);
    }

    @Override
    public DeleteRequest delete(String url) {
        return new DeleteRequest(url);
    }

    @Override
    public DeleteRequest delete(String url, Params params) {
        return new DeleteRequest(url, params);
    }

    @Override
    public OptionRequest options(String url) {
        return new OptionRequest(url);
    }

    @Override
    public OptionRequest options(String url, Params params) {
        return new OptionRequest(url, params);
    }

    @Override
    public PatchRequest patch(String url) {
        return new PatchRequest(url);
    }

    @Override
    public PatchRequest patch(String url, Params params) {
        return new PatchRequest(url, params);
    }

    @Override
    public DownloadRequest download(String url) {
        return new DownloadRequest(url);
    }

    @Override
    public DownloadRequest download(String url, Params params) {
        return new DownloadRequest(url, params);
    }

    @Override
    public UploadRequest upload(String url) {
        return new UploadRequest(url);
    }

    private static class Default {
        private static final Singleton INSTANCE = new Singleton() {

            @Override
            public GetRequest.Singleton get(String url) {
                return new GetRequest.Singleton(url);
            }

            @Override
            public GetRequest.Singleton get(String url, Params params) {
                return new GetRequest.Singleton(url, params);
            }

            @Override
            public PostRequest.Singleton post(String url) {
                return new PostRequest.Singleton(url);
            }

            @Override
            public PostRequest.Singleton post(String url, Params params) {
                return new PostRequest.Singleton(url, params);
            }

            @Override
            public PutRequest.Singleton put(String url) {
                return new PutRequest.Singleton(url);
            }

            @Override
            public PutRequest.Singleton put(String url, Params params) {
                return new PutRequest.Singleton(url, params);
            }

            @Override
            public HeadRequest.Singleton head(String url) {
                return new HeadRequest.Singleton(url);
            }

            @Override
            public DeleteRequest.Singleton delete(String url) {
                return new DeleteRequest.Singleton(url);
            }

            @Override
            public DeleteRequest.Singleton delete(String url, Params params) {
                return new DeleteRequest.Singleton(url, params);
            }

            @Override
            public OptionRequest.Singleton options(String url) {
                return new OptionRequest.Singleton(url);
            }

            @Override
            public OptionRequest.Singleton options(String url, Params params) {
                return new OptionRequest.Singleton(url, params);
            }

            @Override
            public PatchRequest.Singleton patch(String url) {
                return new PatchRequest.Singleton(url);
            }

            @Override
            public PatchRequest.Singleton patch(String url, Params params) {
                return new PatchRequest.Singleton(url, params);
            }

            @Override
            public DownloadRequest.Singleton download(String url) {
                return new DownloadRequest.Singleton(url);
            }

            @Override
            public DownloadRequest.Singleton download(String url, Params params) {
                return new DownloadRequest.Singleton(url, params);
            }

            @Override
            public UploadRequest.Singleton upload(String url) {
                return new UploadRequest.Singleton(url);
            }
        };
    }
}
