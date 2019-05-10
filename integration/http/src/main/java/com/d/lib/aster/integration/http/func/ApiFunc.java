package com.d.lib.aster.integration.http.func;

import com.d.lib.aster.integration.http.client.ResponseBody;
import com.d.lib.aster.scheduler.callback.Function;
import com.d.lib.aster.utils.Util;
import com.google.gson.Gson;

import java.lang.reflect.Type;

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
        Util.printThread("Aster_thread gsonFormat");
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
            if (!mType.equals(ResponseBody.class) && responseBody != null) {
                responseBody.close();
            }
        }
    }
}
