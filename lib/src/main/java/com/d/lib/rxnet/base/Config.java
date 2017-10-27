package com.d.lib.rxnet.base;

import com.d.lib.rxnet.api.API;

/**
 * Config
 * Created by D on 2017/10/24.
 */
public class Config {
    static String TAG_LOG = "RetrofitLog Back = ";

    /****************** Default ******************/
    public static final String BASE_URL = API.API_BASE;
    public static final long CONNECT_TIMEOUT = 10 * 1000;
    public static final long READ_TIMEOUT = 10 * 1000;
    public static final long WRITE_TIMEOUT = 10 * 1000;
    public static final int RETRY_COUNT = 3;//默认重试次数
    public static final long RETRY_DELAY_MILLIS = 3 * 1000;//默认重试间隔时间（毫秒）
}
