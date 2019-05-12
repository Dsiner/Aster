package com.d.lib.aster.integration.retrofit.func;

import com.d.lib.aster.utils.Util;
import com.google.gson.Gson;

import java.lang.reflect.Type;

import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

/**
 * ResponseBody to T
 */
public class ApiFunc<T> implements Function<ResponseBody, T> {
    private Type mType;

    public ApiFunc(Type type) {
        this.mType = type;
    }

    @Override
    public T apply(ResponseBody responseBody) throws Exception {
        Util.printThread("Aster_thread class conversion");
        try {
            if (mType.equals(ResponseBody.class)) {
                return (T) responseBody;
            } else if (mType.equals(String.class)) {
                return (T) responseBody.string();
            } else {
                Gson gson = new Gson();
                return gson.fromJson(responseBody.string(), mType);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (!ResponseBody.class.equals(mType) && responseBody != null) {
                responseBody.close();
            }
        }
    }
}
