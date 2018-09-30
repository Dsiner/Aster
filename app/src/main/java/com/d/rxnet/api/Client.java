package com.d.rxnet.api;

import com.d.lib.rxnet.RxNet;
import com.d.lib.rxnet.base.HttpConfig;
import com.d.lib.rxnet.base.RetrofitClient;
import com.d.lib.rxnet.interceptor.HeadersInterceptor;
import com.d.lib.rxnet.request.DeleteRequest;
import com.d.lib.rxnet.request.DownloadRequest;
import com.d.lib.rxnet.request.GetRequest;
import com.d.lib.rxnet.request.HeadRequest;
import com.d.lib.rxnet.request.OptionRequest;
import com.d.lib.rxnet.request.PatchRequest;
import com.d.lib.rxnet.request.PostRequest;
import com.d.lib.rxnet.request.PutRequest;
import com.d.lib.rxnet.request.UploadRequest;
import com.d.lib.rxnet.utils.SSLUtil;

import java.util.Map;

import okhttp3.Request;
import retrofit2.Retrofit;

/**
 * Client
 * Created by D on 2018/9/30.
 */
public class Client {
    public static RxNet.Singleton getTypeA() {
        return TypeA.INSTANCE;
    }

    public static RxNet.Singleton getTypeB() {
        return TypeB.INSTANCE;
    }

    private static class TypeA {
        private final static RxNet.Singleton INSTANCE = new RxNet.Singleton() {
            private Retrofit clientDefault = RetrofitClient.getRetrofit(new HttpConfig()
                    .baseUrl("https://www.microsoft.com/")
                    .headers(new HeadersInterceptor.OnHeadInterceptor() {
                        @Override
                        public void intercept(Request.Builder builder) {
                            // Add a dynamic request header such as token
                            builder.addHeader("token", "008");
                        }
                    })
                    .connectTimeout(10 * 1000)
                    .readTimeout(10 * 1000)
                    .writeTimeout(10 * 1000)
                    .retryCount(3)
                    .retryDelayMillis(2 * 1000)
                    .sslSocketFactory(SSLUtil.getSslSocketFactory(null, null, null)), true);
            private Retrofit clientTransfer = RetrofitClient.getRetrofit(new HttpConfig()
                    .baseUrl("https://www.microsoft.com/")
                    .connectTimeout(10 * 1000)
                    .readTimeout(10 * 1000)
                    .writeTimeout(10 * 1000)
                    .retryCount(3)
                    .retryDelayMillis(2 * 1000)
                    .sslSocketFactory(SSLUtil.getSslSocketFactory(null, null, null)), false);

            private Retrofit getClientDefault() {
                return clientDefault;
            }

            private Retrofit getClientTransfer() {
                return clientTransfer;
            }

            @Override
            public GetRequest.Singleton get(String url) {
                return new GetRequest.Singleton(url) {
                    @Override
                    protected Retrofit getClient() {
                        return getClientDefault();
                    }
                };
            }

            @Override
            public GetRequest.Singleton get(String url, Map<String, String> params) {
                return new GetRequest.Singleton(url, params) {
                    @Override
                    protected Retrofit getClient() {
                        return getClientDefault();
                    }
                };
            }

            @Override
            public PostRequest.Singleton post(String url) {
                return new PostRequest.Singleton(url) {
                    @Override
                    protected Retrofit getClient() {
                        return getClientDefault();
                    }
                };
            }

            @Override
            public PostRequest.Singleton post(String url, Map<String, String> params) {
                return new PostRequest.Singleton(url, params) {
                    @Override
                    protected Retrofit getClient() {
                        return getClientDefault();
                    }
                };
            }

            @Override
            public HeadRequest.Singleton head(String url, Map<String, String> params) {
                return new HeadRequest.Singleton(url, params) {
                    @Override
                    protected Retrofit getClient() {
                        return getClientDefault();
                    }
                };
            }

            @Override
            public OptionRequest.Singleton options(String url, Map<String, String> params) {
                return new OptionRequest.Singleton(url, params) {
                    @Override
                    protected Retrofit getClient() {
                        return getClientDefault();
                    }
                };
            }

            @Override
            public PutRequest.Singleton put(String url, Map<String, String> params) {
                return new PutRequest.Singleton(url, params) {
                    @Override
                    protected Retrofit getClient() {
                        return getClientDefault();
                    }
                };
            }

            @Override
            public PatchRequest.Singleton patch(String url, Map<String, String> params) {
                return new PatchRequest.Singleton(url, params) {
                    @Override
                    protected Retrofit getClient() {
                        return getClientDefault();
                    }
                };
            }

            @Override
            public DeleteRequest.Singleton delete(String url, Map<String, String> params) {
                return new DeleteRequest.Singleton(url, params) {
                    @Override
                    protected Retrofit getClient() {
                        return getClientDefault();
                    }
                };
            }

            @Override
            public DownloadRequest.Singleton download(String url) {
                return new DownloadRequest.Singleton(url) {
                    @Override
                    protected Retrofit getClient() {
                        return getClientTransfer();
                    }
                };
            }

            @Override
            public DownloadRequest.Singleton download(String url, Map<String, String> params) {
                return new DownloadRequest.Singleton(url, params) {
                    @Override
                    protected Retrofit getClient() {
                        return getClientTransfer();
                    }
                };
            }

            @Override
            public UploadRequest.Singleton upload(String url) {
                return new UploadRequest.Singleton(url) {
                    @Override
                    protected Retrofit getClient() {
                        return getClientTransfer();
                    }
                };
            }
        };
    }

    private static class TypeB {
        private final static RxNet.Singleton INSTANCE = new RxNet.Singleton() {
            private Retrofit clientDefault = RetrofitClient.getRetrofit(new HttpConfig()
                    .baseUrl("https://www.baidu.com/")
                    .connectTimeout(10 * 1000)
                    .readTimeout(10 * 1000)
                    .writeTimeout(10 * 1000)
                    .retryCount(3)
                    .retryDelayMillis(2 * 1000)
                    .sslSocketFactory(SSLUtil.getSslSocketFactory(null, null, null)), true);
            private Retrofit clientTransfer = RetrofitClient.getRetrofit(new HttpConfig()
                    .baseUrl("https://www.baidu.com/")
                    .connectTimeout(10 * 1000)
                    .readTimeout(10 * 1000)
                    .writeTimeout(10 * 1000)
                    .retryCount(3)
                    .retryDelayMillis(2 * 1000)
                    .sslSocketFactory(SSLUtil.getSslSocketFactory(null, null, null)), false);

            private Retrofit getClientDefault() {
                return clientDefault;
            }

            private Retrofit getClientTransfer() {
                return clientTransfer;
            }

            @Override
            public GetRequest.Singleton get(String url) {
                return new GetRequest.Singleton(url) {
                    @Override
                    protected Retrofit getClient() {
                        return getClientDefault();
                    }
                };
            }

            @Override
            public GetRequest.Singleton get(String url, Map<String, String> params) {
                return new GetRequest.Singleton(url, params) {
                    @Override
                    protected Retrofit getClient() {
                        return getClientDefault();
                    }
                };
            }

            @Override
            public PostRequest.Singleton post(String url) {
                return new PostRequest.Singleton(url) {
                    @Override
                    protected Retrofit getClient() {
                        return getClientDefault();
                    }
                };
            }

            @Override
            public PostRequest.Singleton post(String url, Map<String, String> params) {
                return new PostRequest.Singleton(url, params) {
                    @Override
                    protected Retrofit getClient() {
                        return getClientDefault();
                    }
                };
            }

            @Override
            public HeadRequest.Singleton head(String url, Map<String, String> params) {
                return new HeadRequest.Singleton(url, params) {
                    @Override
                    protected Retrofit getClient() {
                        return getClientDefault();
                    }
                };
            }

            @Override
            public OptionRequest.Singleton options(String url, Map<String, String> params) {
                return new OptionRequest.Singleton(url, params) {
                    @Override
                    protected Retrofit getClient() {
                        return getClientDefault();
                    }
                };
            }

            @Override
            public PutRequest.Singleton put(String url, Map<String, String> params) {
                return new PutRequest.Singleton(url, params) {
                    @Override
                    protected Retrofit getClient() {
                        return getClientDefault();
                    }
                };
            }

            @Override
            public PatchRequest.Singleton patch(String url, Map<String, String> params) {
                return new PatchRequest.Singleton(url, params) {
                    @Override
                    protected Retrofit getClient() {
                        return getClientDefault();
                    }
                };
            }

            @Override
            public DeleteRequest.Singleton delete(String url, Map<String, String> params) {
                return new DeleteRequest.Singleton(url, params) {
                    @Override
                    protected Retrofit getClient() {
                        return getClientDefault();
                    }
                };
            }

            @Override
            public DownloadRequest.Singleton download(String url) {
                return new DownloadRequest.Singleton(url) {
                    @Override
                    protected Retrofit getClient() {
                        return getClientTransfer();
                    }
                };
            }

            @Override
            public DownloadRequest.Singleton download(String url, Map<String, String> params) {
                return new DownloadRequest.Singleton(url, params) {
                    @Override
                    protected Retrofit getClient() {
                        return getClientTransfer();
                    }
                };
            }

            @Override
            public UploadRequest.Singleton upload(String url) {
                return new UploadRequest.Singleton(url) {
                    @Override
                    protected Retrofit getClient() {
                        return getClientTransfer();
                    }
                };
            }
        };
    }
}
