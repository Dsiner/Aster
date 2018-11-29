package com.d.lib.aster;

import com.d.lib.aster.base.HttpClient;
import com.d.lib.aster.base.HttpConfig;
import com.d.lib.aster.request.DeleteRequest;
import com.d.lib.aster.request.DownloadRequest;
import com.d.lib.aster.request.GetRequest;
import com.d.lib.aster.request.HeadRequest;
import com.d.lib.aster.request.OptionRequest;
import com.d.lib.aster.request.PatchRequest;
import com.d.lib.aster.request.PostRequest;
import com.d.lib.aster.request.PutRequest;
import com.d.lib.aster.request.UploadRequest;

import java.util.Map;

import retrofit2.Retrofit;

/**
 * Created by D on 2017/10/24.
 */
public class Aster {

    public static HttpConfig.Builder init() {
        return new HttpConfig.Builder();
    }

    public static Singleton getDefault() {
        return Default.INSTANCE;
    }

    public static Retrofit getRetrofit() {
        return HttpClient.getDefault(HttpClient.TYPE_NORMAL).getRetrofitClient();
    }

    private Aster() {
    }

    public static GetRequest get(String url) {
        return new GetRequest(url);
    }

    public static GetRequest get(String url, Map<String, String> params) {
        return new GetRequest(url, params);
    }

    public static PostRequest post(String url) {
        return new PostRequest(url);
    }

    public static PostRequest post(String url, Map<String, String> params) {
        return new PostRequest(url, params);
    }

    public static HeadRequest head(String url, Map<String, String> params) {
        return new HeadRequest(url, params);
    }

    public static OptionRequest options(String url, Map<String, String> params) {
        return new OptionRequest(url, params);
    }

    public static PutRequest put(String url, Map<String, String> params) {
        return new PutRequest(url, params);
    }

    public static PatchRequest patch(String url, Map<String, String> params) {
        return new PatchRequest(url, params);
    }

    public static DeleteRequest delete(String url, Map<String, String> params) {
        return new DeleteRequest(url, params);
    }

    public static DownloadRequest download(String url) {
        return new DownloadRequest(url);
    }

    public static DownloadRequest download(String url, Map<String, String> params) {
        return new DownloadRequest(url, params);
    }

    public static UploadRequest upload(String url) {
        return new UploadRequest(url);
    }

    private static class Default {
        private final static Singleton INSTANCE = new Singleton() {

            @Override
            public GetRequest.Singleton get(String url) {
                return new GetRequest.Singleton(url);
            }

            @Override
            public GetRequest.Singleton get(String url, Map<String, String> params) {
                return new GetRequest.Singleton(url, params);
            }

            @Override
            public PostRequest.Singleton post(String url) {
                return new PostRequest.Singleton(url);
            }

            @Override
            public PostRequest.Singleton post(String url, Map<String, String> params) {
                return new PostRequest.Singleton(url, params);
            }

            @Override
            public HeadRequest.Singleton head(String url, Map<String, String> params) {
                return new HeadRequest.Singleton(url, params);
            }

            @Override
            public OptionRequest.Singleton options(String url, Map<String, String> params) {
                return new OptionRequest.Singleton(url, params);
            }

            @Override
            public PutRequest.Singleton put(String url, Map<String, String> params) {
                return new PutRequest.Singleton(url, params);
            }

            @Override
            public PatchRequest.Singleton patch(String url, Map<String, String> params) {
                return new PatchRequest.Singleton(url, params);
            }

            @Override
            public DeleteRequest.Singleton delete(String url, Map<String, String> params) {
                return new DeleteRequest.Singleton(url, params);
            }

            @Override
            public DownloadRequest.Singleton download(String url) {
                return new DownloadRequest.Singleton(url);
            }

            @Override
            public DownloadRequest.Singleton download(String url, Map<String, String> params) {
                return new DownloadRequest.Singleton(url, params);
            }

            @Override
            public UploadRequest.Singleton upload(String url) {
                return new UploadRequest.Singleton(url);
            }
        };
    }

    /**
     * Singleton
     */
    public static abstract class Singleton {

        public abstract GetRequest.Singleton get(String url);

        public abstract GetRequest.Singleton get(String url, Map<String, String> params);

        public abstract PostRequest.Singleton post(String url);

        public abstract PostRequest.Singleton post(String url, Map<String, String> params);

        public abstract HeadRequest.Singleton head(String url, Map<String, String> params);

        public abstract OptionRequest.Singleton options(String url, Map<String, String> params);

        public abstract PutRequest.Singleton put(String url, Map<String, String> params);

        public abstract PatchRequest.Singleton patch(String url, Map<String, String> params);

        public abstract DeleteRequest.Singleton delete(String url, Map<String, String> params);

        public abstract DownloadRequest.Singleton download(String url);

        public abstract DownloadRequest.Singleton download(String url, Map<String, String> params);

        public abstract UploadRequest.Singleton upload(String url);
    }
}
