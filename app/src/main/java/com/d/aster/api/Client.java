package com.d.aster.api;

import com.d.lib.aster.Aster;
import com.d.lib.aster.base.HttpClient;
import com.d.lib.aster.base.HttpConfig;
import com.d.lib.aster.interceptor.HeadersInterceptor;
import com.d.lib.aster.request.DeleteRequest;
import com.d.lib.aster.request.DownloadRequest;
import com.d.lib.aster.request.GetRequest;
import com.d.lib.aster.request.HeadRequest;
import com.d.lib.aster.request.OptionRequest;
import com.d.lib.aster.request.PatchRequest;
import com.d.lib.aster.request.PostRequest;
import com.d.lib.aster.request.PutRequest;
import com.d.lib.aster.request.UploadRequest;
import com.d.lib.aster.utils.SSLUtil;

import java.util.Map;

import okhttp3.Request;

/**
 * Client
 * Created by D on 2018/9/30.
 */
public class Client {
    public static Aster.Singleton getTypeA() {
        return TypeA.INSTANCE;
    }

    public static Aster.Singleton getTypeB() {
        return TypeB.INSTANCE;
    }

    private static class TypeA {
        private final static Aster.Singleton INSTANCE = new Aster.Singleton() {
            private HttpClient clientDefault = HttpClient.create(HttpClient.TYPE_NORMAL,
                    new HttpConfig().baseUrl("https://www.microsoft.com/")
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
                            .sslSocketFactory(SSLUtil.getSslSocketFactory(null, null, null))
                            .log(true));
            private HttpClient clientTransfer = HttpClient.create(HttpClient.TYPE_NORMAL,
                    new HttpConfig().baseUrl("https://www.microsoft.com/")
                            .connectTimeout(10 * 1000)
                            .readTimeout(10 * 1000)
                            .writeTimeout(10 * 1000)
                            .retryCount(3)
                            .retryDelayMillis(2 * 1000)
                            .sslSocketFactory(SSLUtil.getSslSocketFactory(null, null, null))
                            .log(false));

            private HttpClient getClientDefault() {
                return clientDefault;
            }

            private HttpClient getClientTransfer() {
                return clientTransfer;
            }

            @Override
            public GetRequest.Singleton get(String url) {
                return new GetRequest.Singleton(url) {
                    @Override
                    protected HttpClient getClient() {
                        return getClientDefault();
                    }
                };
            }

            @Override
            public GetRequest.Singleton get(String url, Map<String, String> params) {
                return new GetRequest.Singleton(url, params) {
                    @Override
                    protected HttpClient getClient() {
                        return getClientDefault();
                    }
                };
            }

            @Override
            public PostRequest.Singleton post(String url) {
                return new PostRequest.Singleton(url) {
                    @Override
                    protected HttpClient getClient() {
                        return getClientDefault();
                    }
                };
            }

            @Override
            public PostRequest.Singleton post(String url, Map<String, String> params) {
                return new PostRequest.Singleton(url, params) {
                    @Override
                    protected HttpClient getClient() {
                        return getClientDefault();
                    }
                };
            }

            @Override
            public HeadRequest.Singleton head(String url, Map<String, String> params) {
                return new HeadRequest.Singleton(url, params) {
                    @Override
                    protected HttpClient getClient() {
                        return getClientDefault();
                    }
                };
            }

            @Override
            public OptionRequest.Singleton options(String url, Map<String, String> params) {
                return new OptionRequest.Singleton(url, params) {
                    @Override
                    protected HttpClient getClient() {
                        return getClientDefault();
                    }
                };
            }

            @Override
            public PutRequest.Singleton put(String url, Map<String, String> params) {
                return new PutRequest.Singleton(url, params) {
                    @Override
                    protected HttpClient getClient() {
                        return getClientDefault();
                    }
                };
            }

            @Override
            public PatchRequest.Singleton patch(String url, Map<String, String> params) {
                return new PatchRequest.Singleton(url, params) {
                    @Override
                    protected HttpClient getClient() {
                        return getClientDefault();
                    }
                };
            }

            @Override
            public DeleteRequest.Singleton delete(String url, Map<String, String> params) {
                return new DeleteRequest.Singleton(url, params) {
                    @Override
                    protected HttpClient getClient() {
                        return getClientDefault();
                    }
                };
            }

            @Override
            public DownloadRequest.Singleton download(String url) {
                return new DownloadRequest.Singleton(url) {
                    @Override
                    protected HttpClient getClient() {
                        return getClientTransfer();
                    }
                };
            }

            @Override
            public DownloadRequest.Singleton download(String url, Map<String, String> params) {
                return new DownloadRequest.Singleton(url, params) {
                    @Override
                    protected HttpClient getClient() {
                        return getClientTransfer();
                    }
                };
            }

            @Override
            public UploadRequest.Singleton upload(String url) {
                return new UploadRequest.Singleton(url) {
                    @Override
                    protected HttpClient getClient() {
                        return getClientTransfer();
                    }
                };
            }
        };
    }

    private static class TypeB {
        private final static Aster.Singleton INSTANCE = new Aster.Singleton() {
            private HttpClient clientDefault = HttpClient.create(HttpClient.TYPE_NORMAL,
                    new HttpConfig().baseUrl("https://www.baidu.com/")
                            .connectTimeout(10 * 1000)
                            .readTimeout(10 * 1000)
                            .writeTimeout(10 * 1000)
                            .retryCount(3)
                            .retryDelayMillis(2 * 1000)
                            .sslSocketFactory(SSLUtil.getSslSocketFactory(null, null, null))
                            .log(true));
            private HttpClient clientTransfer = HttpClient.create(HttpClient.TYPE_NORMAL,
                    new HttpConfig().baseUrl("https://www.baidu.com/")
                            .connectTimeout(10 * 1000)
                            .readTimeout(10 * 1000)
                            .writeTimeout(10 * 1000)
                            .retryCount(3)
                            .retryDelayMillis(2 * 1000)
                            .sslSocketFactory(SSLUtil.getSslSocketFactory(null, null, null))
                            .log(false));

            private HttpClient getClientDefault() {
                return clientDefault;
            }

            private HttpClient getClientTransfer() {
                return clientTransfer;
            }

            @Override
            public GetRequest.Singleton get(String url) {
                return new GetRequest.Singleton(url) {
                    @Override
                    protected HttpClient getClient() {
                        return getClientDefault();
                    }
                };
            }

            @Override
            public GetRequest.Singleton get(String url, Map<String, String> params) {
                return new GetRequest.Singleton(url, params) {
                    @Override
                    protected HttpClient getClient() {
                        return getClientDefault();
                    }
                };
            }

            @Override
            public PostRequest.Singleton post(String url) {
                return new PostRequest.Singleton(url) {
                    @Override
                    protected HttpClient getClient() {
                        return getClientDefault();
                    }
                };
            }

            @Override
            public PostRequest.Singleton post(String url, Map<String, String> params) {
                return new PostRequest.Singleton(url, params) {
                    @Override
                    protected HttpClient getClient() {
                        return getClientDefault();
                    }
                };
            }

            @Override
            public HeadRequest.Singleton head(String url, Map<String, String> params) {
                return new HeadRequest.Singleton(url, params) {
                    @Override
                    protected HttpClient getClient() {
                        return getClientDefault();
                    }
                };
            }

            @Override
            public OptionRequest.Singleton options(String url, Map<String, String> params) {
                return new OptionRequest.Singleton(url, params) {
                    @Override
                    protected HttpClient getClient() {
                        return getClientDefault();
                    }
                };
            }

            @Override
            public PutRequest.Singleton put(String url, Map<String, String> params) {
                return new PutRequest.Singleton(url, params) {
                    @Override
                    protected HttpClient getClient() {
                        return getClientDefault();
                    }
                };
            }

            @Override
            public PatchRequest.Singleton patch(String url, Map<String, String> params) {
                return new PatchRequest.Singleton(url, params) {
                    @Override
                    protected HttpClient getClient() {
                        return getClientDefault();
                    }
                };
            }

            @Override
            public DeleteRequest.Singleton delete(String url, Map<String, String> params) {
                return new DeleteRequest.Singleton(url, params) {
                    @Override
                    protected HttpClient getClient() {
                        return getClientDefault();
                    }
                };
            }

            @Override
            public DownloadRequest.Singleton download(String url) {
                return new DownloadRequest.Singleton(url) {
                    @Override
                    protected HttpClient getClient() {
                        return getClientTransfer();
                    }
                };
            }

            @Override
            public DownloadRequest.Singleton download(String url, Map<String, String> params) {
                return new DownloadRequest.Singleton(url, params) {
                    @Override
                    protected HttpClient getClient() {
                        return getClientTransfer();
                    }
                };
            }

            @Override
            public UploadRequest.Singleton upload(String url) {
                return new UploadRequest.Singleton(url) {
                    @Override
                    protected HttpClient getClient() {
                        return getClientTransfer();
                    }
                };
            }
        };
    }
}
