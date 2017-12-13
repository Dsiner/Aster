# RxNet for Android

[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[ ![Download](https://api.bintray.com/packages/dsiner/maven/rxnet/images/download.svg) ](https://bintray.com/dsiner/maven/rxnet/_latestVersion)

RxNet 是一个基于 `Retrofit2` + `RxJava2` 实现的 `网络请求库`

## 特点
-  `1` 条链，完全链式调用 `.func0().func1().func2()...` ， `自适应` 、 `简`
-  `2` 种Retrofit形式（全局配置 `单例` 、完全自定义配置 `新的实例` ）
-  `3` 种链式形式，完全扩展

## 支持列表
- [x] 支持Get、Post、Head、Options、Put、Patch、Delete请求协议
- [x] 支持文件下载、进度回调
- [x] 支持文件上传、进度回调
- [x] 支持添加固定header头、动态header头
- [x] 支持失败重试机制，可以指定重试次数、重试间隔
- [x] 支持Tag、取消数据请求，取消订阅

## 使用
Maven:
```xml
<dependency>
  <groupId>com.dsiner.lib</groupId>
  <artifactId>rxnet</artifactId>
  <version>1.1.0</version>
</dependency>
```
or Gradle:
```groovy
compile 'com.dsiner.lib:rxnet:1.1.0'
```

### 添加manifest权限
```xml
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS" />
```

### 全局配置
```java
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //全局配置
        RxNet.init()
                .baseUrl(API.API_BASE)
                .headers(headers)
                .connectTimeout(10 * 1000)
                .readTimeout(10 * 1000)
                .writeTimeout(10 * 1000)
                .retryCount(3)
                .retryDelayMillis(2 * 1000)
                .sslSocketFactory(SSLUtil.getSslSocketFactory(null, null, null))
                .setLog("RetrofitLog Back = ", HttpLoggingInterceptor.Level.BODY)
                .build();
    }
}
```

### 请求参数（以Params包装）
```java
        Params params = new Params(url);
        params.addParam("start", "0");
        params.addParam("count", "10");
```

### Retrofit形式一： 单例（使用全局配置）

#### 链式形式 1：    ——（CallBack简洁回调）
```java
        //1-1：SimpleCallBack回调
        RxNet.getInstance().get(url, params)
                .request(new SimpleCallBack<MovieInfo>() {
                    @Override
                    public void onSuccess(MovieInfo info) {
                        ...请求成功 -->主线程
                    }

                    @Override
                    public void onError(Throwable e) {
                        ...请求失败 -->主线程
                    }
                });
                
        //1-2：AsyncCallBack回调
        RxNet.getInstance().get(url, params)
                .request(new AsyncCallBack<MovieInfo, String>() {
                    @Override
                    public String apply(@NonNull MovieInfo info) throws Exception {
                        ...请求成功 step-1 -->子线程（耗时、map转换等）
                        int size = info.subjects.size();
                        return "" + size;
                    }

                    @Override
                    public void onSuccess(String response) {
                        ...请求成功 step-2 -->主线程
                    }

                    @Override
                    public void onError(Throwable e) {
                        ...请求失败 -->主线程
                    }
                });
```

#### 链式形式 2：    ——（.observable(T)指定泛型T特定返回类型，调用Retrofit的观察者，而非CallBack接口）
```java
        RxNet.getInstance().get(url, params)
                .observable(MovieInfo.class)
                .map(new Function<MovieInfo, MovieInfo>() {
                    @Override
                    public MovieInfo apply(@NonNull MovieInfo info) throws Exception {
                        return info;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<MovieInfo>() {
                    @Override
                    public void onNext(@NonNull MovieInfo info) {
                        ...
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        ...
                    }

                    @Override
                    public void onComplete() {
                        ...
                    }
                });
```

#### 链式形式 3：    ——（RxNet.getRetrofit()获取Retrofit，完全自定义.create()）
```java
        RxNet.getRetrofit().create(SubAPI.class)
                .get(url)
                .subscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, ArrayList<Boolean>>() {
                    @Override
                    public ArrayList<Boolean> apply(@NonNull ResponseBody info) throws Exception {
                        return new ArrayList<>();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ArrayList<Boolean>>() {
                    @Override
                    public void onNext(ArrayList<Boolean> booleans) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
```

### Retrofit形式二：  新的实例（支持全新的自定义配置、支持上述3种链式形式）
```java
        RxNet.get(url, params)
                .baseUrl(url)
                .connectTimeout(5 * 1000)
                .readTimeout(5 * 1000)
                .writeTimeout(5 * 1000)
                .request(new AsyncCallBack<MovieInfo, String>() {
                    @Override
                    public String apply(@NonNull MovieInfo info) throws Exception {
                        ...
                    }

                    @Override
                    public void onSuccess(String response) {
                        ...
                    }

                    @Override
                    public void onError(Throwable e) {
                        ...
                    }
                });
```

#### `新的实例` 与 `单例` 的使用区别
- `New`    - 开头 `RxNet` 代替 `RxNet.getInstance()`
- `Config` - 自定义配置，支持`.connectTimeout()、.baseUrl()、.headers()` 等所有参数配置，仅作用于此次请求.

### 文件下载
```java
        RxNet.download(url)
                .connectTimeout(60 * 1000)
                .readTimeout(60 * 1000)
                .writeTimeout(60 * 1000)
                .retryCount(3)
                .retryDelayMillis(1000)
                .tag("download")
                .request(path, filename, new DownloadCallBack() {

                    @Override
                    public void onProgress(long currentLength, long totalLength) {
                        ...-->主线程
                    }

                    @Override
                    public void onError(Throwable e) {
                        ...-->主线程
                    }

                    @Override
                    public void onComplete() {
                        ...-->主线程
                    }
                });
```

### 文件上传
```java
        RxNet.upload(url)
                .connectTimeout(60 * 1000)
                .readTimeout(60 * 1000)
                .writeTimeout(60 * 1000)
                .retryCount(3)
                .retryDelayMillis(1000)
                .addFile("File", file)
                .tag("upload")
                .request(new UploadCallBack() {
                    @Override
                    public void onProgress(long currentLength, long totalLength) {
                        ...-->主线程
                    }

                    @Override
                    public void onError(Throwable e) {
                        ...-->主线程
                    }

                    @Override
                    public void onComplete() {
                        ...-->主线程
                    }
                });
```

### 取消订阅
```java
        ApiManager.get().cancel(tag);
```

### ProGuard
If you are using ProGuard you might need to add the following options:
```java
# Gson
# OkHttp3
# Retrofit
# RxJava & RxAndroid
```

More usage see [Demo](app/src/main/java/com/d/rxnet/MainActivity.java)

## Licence

```txt
Copyright 2017 D

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
