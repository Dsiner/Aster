package com.d.lib.aster.integration.http.body;

/**
 * MultipartBody
 * Created by D on 2018/12/10.
 **/
public class MultipartBody {

    public static class Part {
        public String key, value;
        public RequestBody requestBody;

        private Part(String key, String value, RequestBody requestBody) {
            this.key = key;
            this.value = value;
            this.requestBody = requestBody;
        }

        public static Part createFormData(String key, String name, RequestBody requestBody) {
            return new Part(key, name, requestBody);
        }

        public static Part createFormData(String key, String value) {
            return new Part(key, value, null);
        }
    }
}
