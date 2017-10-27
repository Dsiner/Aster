package com.d.lib.rxnet;

import android.content.Context;

import com.d.lib.rxnet.base.HttpConfig;
import com.d.lib.rxnet.base.RetrofitClient;
import com.d.lib.rxnet.listener.RequestListener;
import com.d.lib.rxnet.request.DeleteRequest;
import com.d.lib.rxnet.request.DownloadRequest;
import com.d.lib.rxnet.request.GetRequest;
import com.d.lib.rxnet.request.HeadRequest;
import com.d.lib.rxnet.request.OptionRequest;
import com.d.lib.rxnet.request.PatchRequest;
import com.d.lib.rxnet.request.PostRequest;
import com.d.lib.rxnet.request.PutRequest;
import com.d.lib.rxnet.request.UploadRequest;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Retrofit;

/**
 * New
 * Created by D on 2017/10/24.
 */
public class RxNet implements RequestListener {
    private Context appContext;

    public static RxNetIns getInstance(Context context) {
        return new RxNetIns(context);
    }

    public static HttpConfig.Build init(Context context) {
        return new HttpConfig.Build(context.getApplicationContext());
    }

    public static Retrofit getRetrofit(Context context) {
        return RetrofitClient.getInstance(context);
    }

    public RxNet(Context context) {
        this.appContext = context.getApplicationContext();
    }

    public GetRequest.GetRequestF get(String url) {
        return new GetRequest.GetRequestF(appContext, url);
    }

    public GetRequest.GetRequestF get(String url, Map<String, String> params) {
        return new GetRequest.GetRequestF(appContext, url, params);
    }

    public PostRequest.PostRequestF post(String url) {
        return new PostRequest.PostRequestF(appContext, url);
    }

    public PostRequest.PostRequestF post(String url, Map<String, String> params) {
        return new PostRequest.PostRequestF(appContext, url, params);
    }

    public HeadRequest.HeadRequestF head(String url, Map<String, String> params) {
        return new HeadRequest.HeadRequestF(appContext, url, params);
    }

    public OptionRequest.OptionRequestF options(String url, Map<String, String> params) {
        return new OptionRequest.OptionRequestF(appContext, url, params);
    }

    public PutRequest.PutRequestF put(String url, Map<String, String> params) {
        return new PutRequest.PutRequestF(appContext, url, params);
    }

    public PatchRequest.PatchRequestF patch(String url, Map<String, String> params) {
        return new PatchRequest.PatchRequestF(appContext, url, params);
    }

    public DeleteRequest.DeleteRequestF delete(String url, Map<String, String> params) {
        return new DeleteRequest.DeleteRequestF(appContext, url, params);
    }

    public DownloadRequest.DownloadRequestF download(String url) {
        return new DownloadRequest.DownloadRequestF(appContext, url);
    }

    public DownloadRequest.DownloadRequestF download(String url, Map<String, String> params) {
        return new DownloadRequest.DownloadRequestF(appContext, url, params);
    }

    public UploadRequest.UploadRequestF upload(String url, List<MultipartBody.Part> parts) {
        return new UploadRequest.UploadRequestF(appContext, url, parts);
    }

    public static class RxNetIns implements RequestListener {
        Context appContext;

        RxNetIns(Context context) {
            this.appContext = context.getApplicationContext();
        }

        @Override
        public GetRequest get(String url) {
            return new GetRequest(appContext, url);
        }

        @Override
        public GetRequest get(String url, Map<String, String> params) {
            return new GetRequest(appContext, url, params);
        }

        public PostRequest post(String url) {
            return new PostRequest(appContext, url);
        }

        public PostRequest post(String url, Map<String, String> params) {
            return new PostRequest(appContext, url, params);
        }

        public HeadRequest head(String url, Map<String, String> params) {
            return new HeadRequest(appContext, url, params);
        }

        public OptionRequest options(String url, Map<String, String> params) {
            return new OptionRequest(appContext, url, params);
        }

        public PutRequest put(String url, Map<String, String> params) {
            return new PutRequest(appContext, url, params);
        }

        public PatchRequest patch(String url, Map<String, String> params) {
            return new PatchRequest(appContext, url, params);
        }

        public DeleteRequest delete(String url, Map<String, String> params) {
            return new DeleteRequest(appContext, url, params);
        }

        public DownloadRequest download(String url) {
            return new DownloadRequest(appContext, url);
        }

        public DownloadRequest download(String url, Map<String, String> params) {
            return new DownloadRequest(appContext, url, params);
        }

        public UploadRequest upload(String url, List<MultipartBody.Part> parts) {
            return new UploadRequest(appContext, url, parts);
        }
    }
}
