package com.d.lib.aster.integration.retrofit;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.OPTIONS;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * RetrofitAPI
 * Created by D on 2017/7/14.
 */
public interface RetrofitAPI {

    @GET()
    Observable<ResponseBody> get(@Url String url);

    @GET()
    Observable<ResponseBody> get(@Url String url, @QueryMap Map<String, String> params);

    @POST()
    Observable<ResponseBody> post(@Url String url);

    @FormUrlEncoded
    @POST()
    Observable<ResponseBody> post(@Url String url, @FieldMap Map<String, String> params);

    @POST()
    Observable<ResponseBody> postBody(@Url String url, @Body RequestBody requestBody);

    @PUT()
    Observable<ResponseBody> put(@Url String url);

    @FormUrlEncoded
    @PUT()
    Observable<ResponseBody> put(@Url String url, @FieldMap Map<String, String> params);

    @PUT()
    Observable<ResponseBody> putBody(@Url String url, @Body RequestBody requestBody);

    @HEAD()
    Observable<ResponseBody> head(@Url String url);

    @DELETE()
    Observable<ResponseBody> delete(@Url String url);

    @FormUrlEncoded
    @DELETE()
    Observable<ResponseBody> delete(@Url String url, @FieldMap Map<String, String> params);

    @HTTP(method = "DELETE", hasBody = true)
    Observable<ResponseBody> deleteBody(@Url String url, @Body RequestBody requestBody);

    @OPTIONS()
    Observable<ResponseBody> options(@Url String url);

    @FormUrlEncoded
    @OPTIONS()
    Observable<ResponseBody> options(@Url String url, @FieldMap Map<String, String> params);

    @HTTP(method = "OPTIONS", hasBody = true)
    Observable<ResponseBody> optionsBody(@Url String url, @Body RequestBody requestBody);

    @PATCH()
    Observable<ResponseBody> patch(@Url String url);

    @FormUrlEncoded
    @PATCH()
    Observable<ResponseBody> patch(@Url String url, @FieldMap Map<String, String> params);

    @PATCH()
    Observable<ResponseBody> patchBody(@Url String url, @Body RequestBody requestBody);

    @Streaming
    @GET()
    Observable<ResponseBody> download(@Url String url);

    @Streaming
    @GET()
    Observable<ResponseBody> download(@Url String url, @QueryMap Map<String, String> params);

    @Multipart
    @POST()
    Observable<ResponseBody> upload(@Url String url, @Part MultipartBody.Part part);

    @Multipart
    @POST()
    Observable<ResponseBody> upload(@Url String url, @Part() List<MultipartBody.Part> parts);
}