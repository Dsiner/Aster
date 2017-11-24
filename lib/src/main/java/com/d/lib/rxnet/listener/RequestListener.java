package com.d.lib.rxnet.listener;

import com.d.lib.rxnet.request.BaseRequest;

import java.util.Map;

/**
 * RequestListener
 * Created by D on 2017/10/26.
 */
public interface RequestListener {
    BaseRequest get(String url);

    BaseRequest get(String url, Map<String, String> params);
}
