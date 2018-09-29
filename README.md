# RxNet for Android

[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![API](https://img.shields.io/badge/API-9%2B-green.svg?style=flat)](https://android-arsenal.com/api?level=9)
[![Download](https://api.bintray.com/packages/dsiner/maven/rxnet/images/download.svg) ](https://bintray.com/dsiner/maven/rxnet/_latestVersion)
[![Readme](https://img.shields.io/badge/README-%E4%B8%AD%E6%96%87-brightgreen.svg)](https://github.com/Dsiner/RxNet/blob/master/README-zh.md)

> A network request library based on `Retrofit2` + `Okhttp3` + `RxJava2`

## Features
- `1` chain, completely chained `.func0().func1().func2()...`, `Adaptive`, `Jane`
- `2` Retrofit forms (`Singleton` global configuration, ` New instance ` fully custom configuration)
- `3` chained form, fully expanded

## Support list
- [x] Supports Get, Post, Head, Options, Put, Patch, Delete Request Protocol
- [x] Support file download, progress callback
- [x] Support file upload, progress callback
- [x] Support for adding fixed header headers, dynamic header headers
- [x] Support failure retry mechanism, can specify retry times, retry interval
- [x] Support Tag, Cancel Data Request, Unsubscribe

## Use
Maven:
```xml
<dependency>
  <groupId>com.dsiner.lib</groupId>
  <artifactId>rxnet</artifactId>
  <version>1.1.1</version>
</dependency>
```
or Gradle:
```groovy
compile 'com.dsiner.lib:rxnet:1.1.1'
```

### Global configuration
```java
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //Global configuration
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

### Request parameters (wrapped in Params)
```java
        Params params = new Params(url);
        params.addParam("start", "0");
        params.addParam("count", "10");
```

### Retrofit form 1: Singleton (using global configuration)

#### Chained form 1-1: Callback simple callback
```java
        //1-1-1: SimpleCallback callback
        RxNet.getInstance().get(url, params)
                .request(new SimpleCallback<MovieInfo>() {
                    @Override
                    public void onSuccess(MovieInfo info) {
                        ...do something in main thread
                    }

                    @Override
                    public void onError(Throwable e) {
                        ...do something in main thread
                    }
                });
                
        //1-1-2: AsyncCallback callback
        RxNet.getInstance().get(url, params)
                .request(new AsyncCallback<MovieInfo, String>() {
                    @Override
                    public String apply(@NonNull MovieInfo info) throws Exception {
                        ...Success step-1 -->
                        ...do something in asynchronous thread, such as time-consuming, map conversion, etc.
                        int size = info.subjects.size();
                        return "" + size;
                    }

                    @Override
                    public void onSuccess(String response) {
                        ...Success step-2 -->
                        ...do something in main thread
                    }

                    @Override
                    public void onError(Throwable e) {
                        ...Error -->
                        ...do something in main thread
                    }
                });
```

#### Chained form 1-2: .observable(T) specifies a generic T-specific return type, calls Retrofit's observer instead of the Callback interface
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

#### Chained form 1-3: RxNet.getRetrofit() gets Retrofit, fully customizable .create()
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

### Retrofit form 2: New instance (supports new custom configuration, supports the above three chain forms)
```java
        RxNet.get(url, params)
                .baseUrl(url)
                .connectTimeout(5 * 1000)
                .readTimeout(5 * 1000)
                .writeTimeout(5 * 1000)
                .request(new AsyncCallback<MovieInfo, String>() {
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

### Download
```java
        RxNet.download(url)
                .connectTimeout(60 * 1000)
                .readTimeout(60 * 1000)
                .writeTimeout(60 * 1000)
                .retryCount(3)
                .retryDelayMillis(1000)
                .tag("download")
                .request(path, filename, new DownloadCallback() {

                    @Override
                    public void onProgress(long currentLength, long totalLength) {
                        ...do something in main thread
                    }

                    @Override
                    public void onError(Throwable e) {
                        ...do something in main thread
                    }

                    @Override
                    public void onComplete() {
                        ...do something in main thread
                    }
                });
```

### Upload
```java
        RxNet.upload(url)
                .connectTimeout(60 * 1000)
                .readTimeout(60 * 1000)
                .writeTimeout(60 * 1000)
                .retryCount(3)
                .retryDelayMillis(1000)
                .addFile("File", file)
                .tag("upload")
                .request(new UploadCallback() {
                    @Override
                    public void onProgress(long currentLength, long totalLength) {
                        ...do something in main thread
                    }

                    @Override
                    public void onError(Throwable e) {
                        ...do something in main thread
                    }

                    @Override
                    public void onComplete() {
                        ...do something in main thread
                    }
                });
```

### Unsubscribe
```java
        ApiManager.get().cancel("upload");
```

#### Difference between `New instance` and `Singleton` usage
- `New`    - Starts with `RxNet` instead of `RxNet.getInstance()`
- `Config` - Custom configuration, support for all configuration parameters such as `.connectTimeout(), .baseUrl(), .headers()`, only for this request.

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
