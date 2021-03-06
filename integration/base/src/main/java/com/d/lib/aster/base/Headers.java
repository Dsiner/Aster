package com.d.lib.aster.base;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class Headers extends LinkedHashMap<String, String> {
    private static final String FORMAT_HTTP_DATA = "EEE, dd MMM y HH:mm:ss 'GMT'";
    private static final TimeZone GMT_TIME_ZONE = TimeZone.getTimeZone("GMT");

    public static final String HEAD_KEY_RESPONSE_CODE = "ResponseCode";
    public static final String HEAD_KEY_RESPONSE_MESSAGE = "ResponseMessage";
    public static final String HEAD_KEY_ACCEPT = "Accept";
    public static final String HEAD_KEY_ACCEPT_ENCODING = "Accept-Encoding";
    public static final String HEAD_VALUE_ACCEPT_ENCODING = "gzip, deflate";
    public static final String HEAD_KEY_ACCEPT_LANGUAGE = "Accept-Language";
    public static final String HEAD_KEY_CONTENT_TYPE = "Content-Type";
    public static final String HEAD_KEY_CONTENT_LENGTH = "Content-Length";
    public static final String HEAD_KEY_CONTENT_ENCODING = "Content-Encoding";
    public static final String HEAD_KEY_CONTENT_DISPOSITION = "Content-Disposition";
    public static final String HEAD_KEY_CONTENT_RANGE = "Content-Range";
    public static final String HEAD_KEY_CACHE_CONTROL = "Cache-Control";
    public static final String HEAD_KEY_CONNECTION = "Connection";
    public static final String HEAD_VALUE_CONNECTION_KEEP_ALIVE = "keep-alive";
    public static final String HEAD_VALUE_CONNECTION_CLOSE = "close";
    public static final String HEAD_KEY_DATE = "Date";
    public static final String HEAD_KEY_EXPIRES = "Expires";
    public static final String HEAD_KEY_E_TAG = "ETag";
    public static final String HEAD_KEY_PRAGMA = "Pragma";
    public static final String HEAD_KEY_IF_MODIFIED_SINCE = "If-Modified-Since";
    public static final String HEAD_KEY_IF_NONE_MATCH = "If-None-Match";
    public static final String HEAD_KEY_LAST_MODIFIED = "Last-Modified";
    public static final String HEAD_KEY_LOCATION = "Location";
    public static final String HEAD_KEY_USER_AGENT = "User-Agent";
    public static final String HEAD_KEY_COOKIE = "Cookie";
    public static final String HEAD_KEY_COOKIE2 = "Cookie2";
    public static final String HEAD_KEY_SET_COOKIE = "Set-Cookie";
    public static final String HEAD_KEY_SET_COOKIE2 = "Set-Cookie2";

    private static String acceptLanguage;

    public Headers() {
        super();
    }

    public Headers newBuild() {
        Headers headers = new Headers();
        headers.putAll(this);
        return headers;
    }

    public void addParam(String key, String value) {
        put(key, value == null ? "" : value);
    }

    public String getParam(String key) {
        return get(key);
    }

    public final String toJSONString() {
        JSONObject jsonObject = new JSONObject();
        try {
            for (Map.Entry<String, String> entry : entrySet()) {
                jsonObject.put(entry.getKey(), entry.getValue());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static long getDate(String gmtTime) {
        try {
            return parseGMTToMillis(gmtTime);
        } catch (ParseException e) {
            return 0;
        }
    }

    public static String getDate(long milliseconds) {
        return formatMillisToGMT(milliseconds);
    }

    public static long getExpiration(String expiresTime) {
        try {
            return parseGMTToMillis(expiresTime);
        } catch (ParseException e) {
            return -1;
        }
    }

    public static long getLastModified(String lastModified) {
        try {
            return parseGMTToMillis(lastModified);
        } catch (ParseException e) {
            return 0;
        }
    }

    public static String getCacheControl(String cacheControl, String pragma) {
        // First http1.1, second http1.0
        if (cacheControl != null) {
            return cacheControl;
        } else if (pragma != null) {
            return pragma;
        } else {
            return null;
        }
    }

    public static void setAcceptLanguage(String language) {
        acceptLanguage = language;
    }

    /**
     * Accept-Language: zh-CN,zh;q=0.8
     */
    public static String getAcceptLanguage() {
        if (TextUtils.isEmpty(acceptLanguage)) {
            Locale locale = Locale.getDefault();
            String language = locale.getLanguage();
            String country = locale.getCountry();
            StringBuilder acceptLanguageBuilder = new StringBuilder(language);
            if (!TextUtils.isEmpty(country)) {
                acceptLanguageBuilder.append('-').append(country)
                        .append(',').append(language).append(";q=0.8");
            }
            acceptLanguage = acceptLanguageBuilder.toString();
            return acceptLanguage;
        }
        return acceptLanguage;
    }

    private static long parseGMTToMillis(String gmtTime) throws ParseException {
        if (TextUtils.isEmpty(gmtTime)) {
            return 0;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(FORMAT_HTTP_DATA, Locale.US);
        formatter.setTimeZone(GMT_TIME_ZONE);
        Date date = formatter.parse(gmtTime);
        return date.getTime();
    }

    private static String formatMillisToGMT(long milliseconds) {
        Date date = new Date(milliseconds);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_HTTP_DATA, Locale.US);
        simpleDateFormat.setTimeZone(GMT_TIME_ZONE);
        return simpleDateFormat.format(date);
    }

    @Override
    public String toString() {
        return getParamsString();
    }

    private String getParamsString() {
        StringBuilder param = new StringBuilder();
        if (size() > 0) {
            Iterator ite = entrySet().iterator();
            while (ite.hasNext()) {
                Entry<String, String> entry = (Entry) ite.next();
                String key = entry.getKey();
                String value = entry.getValue();
                param.append(key + "=" + value + "&");
            }
            param.deleteCharAt(param.length() - 1);
        }
        return param.toString();
    }
}
