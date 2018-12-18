package com.d.aster.api;

import com.d.lib.aster.base.AsterModule;
import com.d.lib.aster.base.Config;
import com.d.lib.aster.base.IClient;
import com.d.lib.aster.base.Params;
import com.d.lib.aster.integration.okhttp3.OkHttpClient;
import com.d.lib.aster.integration.okhttp3.interceptor.HeadersInterceptor;
import com.d.lib.aster.integration.okhttp3.request.DeleteRequest;
import com.d.lib.aster.integration.okhttp3.request.DownloadRequest;
import com.d.lib.aster.integration.okhttp3.request.GetRequest;
import com.d.lib.aster.integration.okhttp3.request.HeadRequest;
import com.d.lib.aster.integration.okhttp3.request.OptionRequest;
import com.d.lib.aster.integration.okhttp3.request.PatchRequest;
import com.d.lib.aster.integration.okhttp3.request.PostRequest;
import com.d.lib.aster.integration.okhttp3.request.PutRequest;
import com.d.lib.aster.integration.okhttp3.request.UploadRequest;
import com.d.lib.aster.utils.SSLUtil;

import java.util.Map;

/**
 * Client
 * Created by D on 2018/9/30.
 */
public class Client {
    public static AsterModule.Singleton getTypeA() {
        return TypeA.INSTANCE;
    }

    public static AsterModule.Singleton getTypeB() {
        return TypeB.INSTANCE;
    }

    private static class TypeA {
        private final static AsterModule.Singleton INSTANCE = new AsterModule.Singleton() {
            private OkHttpClient clientDefault = OkHttpClient.create(IClient.TYPE_NORMAL,
                    new Config().baseUrl("https://www.microsoft.com/")
                            .headers(new HeadersInterceptor.OnHeadInterceptor() {
                                @Override
                                public void intercept(Map<String, String> heads) {
                                    // Add a dynamic request header such as token
                                    heads.put("token", "008");
                                }
                            })
                            .connectTimeout(10 * 1000)
                            .readTimeout(10 * 1000)
                            .writeTimeout(10 * 1000)
                            .retryCount(3)
                            .retryDelayMillis(2 * 1000)
                            .sslSocketFactory(SSLUtil.getSslSocketFactory(null, null, null))
                            .log(true));
            private OkHttpClient clientTransfer = OkHttpClient.create(IClient.TYPE_NORMAL,
                    new Config().baseUrl("https://www.microsoft.com/")
                            .connectTimeout(10 * 1000)
                            .readTimeout(10 * 1000)
                            .writeTimeout(10 * 1000)
                            .retryCount(3)
                            .retryDelayMillis(2 * 1000)
                            .sslSocketFactory(SSLUtil.getSslSocketFactory(null, null, null))
                            .log(false));

            private OkHttpClient getClientDefault() {
                return clientDefault;
            }

            private OkHttpClient getClientTransfer() {
                return clientTransfer;
            }

            @Override
            public GetRequest.Singleton get(String url) {
                return new GetRequest.Singleton(url) {
                    @Override
                    protected OkHttpClient getClient() {
                        return getClientDefault();
                    }
                };
            }

            @Override
            public GetRequest.Singleton get(String url, Params params) {
                return new GetRequest.Singleton(url, params) {
                    @Override
                    protected OkHttpClient getClient() {
                        return getClientDefault();
                    }
                };
            }

            @Override
            public PostRequest.Singleton post(String url) {
                return new PostRequest.Singleton(url) {
                    @Override
                    protected OkHttpClient getClient() {
                        return getClientDefault();
                    }
                };
            }

            @Override
            public PostRequest.Singleton post(String url, Params params) {
                return new PostRequest.Singleton(url, params) {
                    @Override
                    protected OkHttpClient getClient() {
                        return getClientDefault();
                    }
                };
            }

            @Override
            public HeadRequest.Singleton head(String url, Params params) {
                return new HeadRequest.Singleton(url, params) {
                    @Override
                    protected OkHttpClient getClient() {
                        return getClientDefault();
                    }
                };
            }

            @Override
            public OptionRequest.Singleton options(String url, Params params) {
                return new OptionRequest.Singleton(url, params) {
                    @Override
                    protected OkHttpClient getClient() {
                        return getClientDefault();
                    }
                };
            }

            @Override
            public PutRequest.Singleton put(String url, Params params) {
                return new PutRequest.Singleton(url, params) {
                    @Override
                    protected OkHttpClient getClient() {
                        return getClientDefault();
                    }
                };
            }

            @Override
            public PatchRequest.Singleton patch(String url, Params params) {
                return new PatchRequest.Singleton(url, params) {
                    @Override
                    protected OkHttpClient getClient() {
                        return getClientDefault();
                    }
                };
            }

            @Override
            public DeleteRequest.Singleton delete(String url, Params params) {
                return new DeleteRequest.Singleton(url, params) {
                    @Override
                    protected OkHttpClient getClient() {
                        return getClientDefault();
                    }
                };
            }

            @Override
            public DownloadRequest.Singleton download(String url) {
                return new DownloadRequest.Singleton(url) {
                    @Override
                    protected OkHttpClient getClient() {
                        return getClientTransfer();
                    }
                };
            }

            @Override
            public DownloadRequest.Singleton download(String url, Params params) {
                return new DownloadRequest.Singleton(url, params) {
                    @Override
                    protected OkHttpClient getClient() {
                        return getClientTransfer();
                    }
                };
            }

            @Override
            public UploadRequest.Singleton upload(String url) {
                return new UploadRequest.Singleton(url) {
                    @Override
                    protected OkHttpClient getClient() {
                        return getClientTransfer();
                    }
                };
            }
        };
    }

    private static class TypeB {
        private final static AsterModule.Singleton INSTANCE = new AsterModule.Singleton() {
            private OkHttpClient clientDefault = OkHttpClient.create(IClient.TYPE_NORMAL,
                    new Config().baseUrl("https://www.baidu.com/")
                            .connectTimeout(10 * 1000)
                            .readTimeout(10 * 1000)
                            .writeTimeout(10 * 1000)
                            .retryCount(3)
                            .retryDelayMillis(2 * 1000)
                            .sslSocketFactory(SSLUtil.getSslSocketFactory(null, null, null))
                            .log(true));
            private OkHttpClient clientTransfer = OkHttpClient.create(IClient.TYPE_NORMAL,
                    new Config().baseUrl("https://www.baidu.com/")
                            .connectTimeout(10 * 1000)
                            .readTimeout(10 * 1000)
                            .writeTimeout(10 * 1000)
                            .retryCount(3)
                            .retryDelayMillis(2 * 1000)
                            .sslSocketFactory(SSLUtil.getSslSocketFactory(null, null, null))
                            .log(false));

            private OkHttpClient getClientDefault() {
                return clientDefault;
            }

            private OkHttpClient getClientTransfer() {
                return clientTransfer;
            }

            @Override
            public GetRequest.Singleton get(String url) {
                return new GetRequest.Singleton(url) {
                    @Override
                    protected OkHttpClient getClient() {
                        return getClientDefault();
                    }
                };
            }

            @Override
            public GetRequest.Singleton get(String url, Params params) {
                return new GetRequest.Singleton(url, params) {
                    @Override
                    protected OkHttpClient getClient() {
                        return getClientDefault();
                    }
                };
            }

            @Override
            public PostRequest.Singleton post(String url) {
                return new PostRequest.Singleton(url) {
                    @Override
                    protected OkHttpClient getClient() {
                        return getClientDefault();
                    }
                };
            }

            @Override
            public PostRequest.Singleton post(String url, Params params) {
                return new PostRequest.Singleton(url, params) {
                    @Override
                    protected OkHttpClient getClient() {
                        return getClientDefault();
                    }
                };
            }

            @Override
            public HeadRequest.Singleton head(String url, Params params) {
                return new HeadRequest.Singleton(url, params) {
                    @Override
                    protected OkHttpClient getClient() {
                        return getClientDefault();
                    }
                };
            }

            @Override
            public OptionRequest.Singleton options(String url, Params params) {
                return new OptionRequest.Singleton(url, params) {
                    @Override
                    protected OkHttpClient getClient() {
                        return getClientDefault();
                    }
                };
            }

            @Override
            public PutRequest.Singleton put(String url, Params params) {
                return new PutRequest.Singleton(url, params) {
                    @Override
                    protected OkHttpClient getClient() {
                        return getClientDefault();
                    }
                };
            }

            @Override
            public PatchRequest.Singleton patch(String url, Params params) {
                return new PatchRequest.Singleton(url, params) {
                    @Override
                    protected OkHttpClient getClient() {
                        return getClientDefault();
                    }
                };
            }

            @Override
            public DeleteRequest.Singleton delete(String url, Params params) {
                return new DeleteRequest.Singleton(url, params) {
                    @Override
                    protected OkHttpClient getClient() {
                        return getClientDefault();
                    }
                };
            }

            @Override
            public DownloadRequest.Singleton download(String url) {
                return new DownloadRequest.Singleton(url) {
                    @Override
                    protected OkHttpClient getClient() {
                        return getClientTransfer();
                    }
                };
            }

            @Override
            public DownloadRequest.Singleton download(String url, Params params) {
                return new DownloadRequest.Singleton(url, params) {
                    @Override
                    protected OkHttpClient getClient() {
                        return getClientTransfer();
                    }
                };
            }

            @Override
            public UploadRequest.Singleton upload(String url) {
                return new UploadRequest.Singleton(url) {
                    @Override
                    protected OkHttpClient getClient() {
                        return getClientTransfer();
                    }
                };
            }
        };
    }
}
