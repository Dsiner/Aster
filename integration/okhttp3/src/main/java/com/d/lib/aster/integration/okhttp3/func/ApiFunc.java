package com.d.lib.aster.integration.okhttp3.func;

import com.d.lib.aster.scheduler.callback.Function;
import com.d.lib.aster.utils.Util;
import com.google.gson.Gson;

import java.lang.reflect.Type;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Response to T
 */
public class ApiFunc<T> implements Function<Response, T> {
    private Type mType;

    public ApiFunc(Type type) {
        this.mType = type;
    }

    @Override
    public T apply(Response response) throws Exception {
        Util.printThread("Aster_thread class conversion");
        try {
            if (mType.equals(Response.class)) {
                return (T) response;
            } else if (mType.equals(ResponseBody.class)) {
                return (T) response.body();
            } else if (mType.equals(String.class)) {
                return (T) response.body().string();
            } else {
                Gson gson = new Gson();
                return gson.fromJson(response.body().string(), mType);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (!Response.class.equals(mType) && !ResponseBody.class.equals(mType)
                    && response != null && response.body() != null) {
                response.body().close();
            }
        }
    }
}
