package com.d.lib.rxnet;

import com.d.lib.rxnet.base.HttpConfig;
import com.d.lib.rxnet.base.RetrofitClient;
import com.d.lib.rxnet.request.DeleteRequest;
import com.d.lib.rxnet.request.DownloadRequest;
import com.d.lib.rxnet.request.GetRequest;
import com.d.lib.rxnet.request.HeadRequest;
import com.d.lib.rxnet.request.OptionRequest;
import com.d.lib.rxnet.request.PatchRequest;
import com.d.lib.rxnet.request.PostRequest;
import com.d.lib.rxnet.request.PutRequest;
import com.d.lib.rxnet.request.UploadRequest;

import java.util.Map;

import retrofit2.Retrofit;

/**
 * Created by D on 2017/10/24.
 */
public class RxNet {

    private static class Singleton {
        private final static Instance INSTANCE = new Instance();
    }

    public static Instance getIns() {
        return Singleton.INSTANCE;
    }

    public static HttpConfig.Build init() {
        return new HttpConfig.Build();
    }

    public static Retrofit getRetrofit() {
        return RetrofitClient.getIns();
    }

    private RxNet() {
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

    /**
     * Singleton
     */
    public static class Instance {

        Instance() {
        }

        public GetRequest.Singleton get(String url) {
            return new GetRequest.Singleton(url);
        }

        public GetRequest.Singleton get(String url, Map<String, String> params) {
            return new GetRequest.Singleton(url, params);
        }

        public PostRequest.Singleton post(String url) {
            return new PostRequest.Singleton(url);
        }

        public PostRequest.Singleton post(String url, Map<String, String> params) {
            return new PostRequest.Singleton(url, params);
        }

        public HeadRequest.Singleton head(String url, Map<String, String> params) {
            return new HeadRequest.Singleton(url, params);
        }

        public OptionRequest.Singleton options(String url, Map<String, String> params) {
            return new OptionRequest.Singleton(url, params);
        }

        public PutRequest.Singleton put(String url, Map<String, String> params) {
            return new PutRequest.Singleton(url, params);
        }

        public PatchRequest.Singleton patch(String url, Map<String, String> params) {
            return new PatchRequest.Singleton(url, params);
        }

        public DeleteRequest.Singleton delete(String url, Map<String, String> params) {
            return new DeleteRequest.Singleton(url, params);
        }

        public DownloadRequest.Singleton download(String url) {
            return new DownloadRequest.Singleton(url);
        }

        public DownloadRequest.Singleton download(String url, Map<String, String> params) {
            return new DownloadRequest.Singleton(url, params);
        }

        public UploadRequest.Singleton upload(String url) {
            return new UploadRequest.Singleton(url);
        }
    }
}
