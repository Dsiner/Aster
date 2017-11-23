package com.d.lib.rxnet.func;

import com.d.lib.rxnet.util.RxLog;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.lang.reflect.Type;

import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

/**
 * ResponseBody转T
 */
public class ApiFunc<T> implements Function<ResponseBody, T> {
    private Type type;

    public ApiFunc(Type type) {
        this.type = type;
    }

    @Override
    public T apply(ResponseBody responseBody) throws Exception {
        RxLog.d("dsiner_th_gsonFormat: " + Thread.currentThread().getId() + "--NAME--" + Thread.currentThread().getName());
        Gson gson = new Gson();
        String json;
        try {
            json = responseBody.string();
            responseBody.close();
            if (type.equals(String.class)) {
                return (T) json;
            } else {
                return gson.fromJson(json, type);
            }
        } catch (IOException e) {
            if (responseBody != null) {
                responseBody.close();
            }
            e.printStackTrace();
            throw new JsonParseException("JSON PARSE ERROR!");
        }
    }
}
