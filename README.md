# RxNet for Android

[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[ ![Download](https://api.bintray.com/packages/dsiner/maven/rxnet/images/download.svg) ](https://bintray.com/dsiner/maven/rxnet/_latestVersion)

RxNet 是一个基于 `Retrofit2` + `RxJava2` 实现的 `网络请求库`

## 特点
-  `1` 条链，完全链式调用 `.func0().func1().func2()...` ， `自适应` 、 `简`
-  `2` 种Retrofit形式（`单例` 全局配置： 、`新的实例` 完全自定义配置）
-  `3` 种链式形式，完全扩展

## 支持列表
- [x] Get
- [x] Post
- [x] Head
- [x] Options
- [x] Put
- [x] Patch
- [x] Delete
- [x] Download
- [x] Upload

## 使用
Maven:
```xml
<dependency>
  <groupId>com.dsiner.lib</groupId>
  <artifactId>rxnet</artifactId>
  <version>1.0.0</version>
</dependency>
```
or Gradle:
```groovy
compile 'com.dsiner.lib:rxnet:1.0.0'
```

### 全局配置
```xml
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //全局配置
        RxNet.init(getApplicationContext())
                .baseUrl(API.API_BASE)
                .headers(null)
                .connectTimeout(10 * 1000)
                .readTimeout(10 * 1000)
                .readTimeout(10 * 1000)
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
        Params params = new Params(API.MovieTop.rtpType);
        params.addParam(API.MovieTop.start, "0");
        params.addParam(API.MovieTop.count, "10");
```

### Retrofit形式一： 单例（使用全局配置）

#### 链式形式 1：    ——（CallBack简洁回调）
```java
        //1-1：SimpleCallBack回调
        RxNet.getInstance(context).get(url, params)
                .request(new SimpleCallBack<MovieInfo>() {
                    @Override
                    public void onSuccess(MovieInfo response) {
                        ...请求成功 -->主线程
                    }

                    @Override
                    public void onError(ApiException e) {
                        ...请求失败 -->主线程
                    }
                });
                
        //1-2：AsyncCallBack回调
        RxNet.getInstance(context).get(url, params)
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
                    public void onError(ApiException e) {
                        ...请求失败 -->主线程
                    }
                });
```

#### 链式形式 2：    ——（.observable(T)指定泛型T特定返回类型，调用Retrofit的观察者，而非CallBack接口）
```java
        RxNet.getInstance(context).get(url, params)
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

#### 链式形式 3：    ——（RxNet.getRetrofit(context)获取Retrofit，完全自定义.create()）
```java
        RxNet.getRetrofit(context).create(SubAPI.class)
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
        new RxNet(context).get(url, params)
                .baseUrl("https://api.douban.com/v2/movie/")
                .connectTimeout(5 * 1000)
                .readTimeout(5 * 1000)
                .writeTimeout(5 * 1000)
                .request(new AsyncCallBack<MovieInfo, String>() {
                    @Override
                    public String apply(@NonNull MovieInfo movieTopModelInfo) throws Exception {
                        ...
                    }

                    @Override
                    public void onSuccess(String response) {
                        ...
                    }

                    @Override
                    public void onError(ApiException e) {
                        ...
                    }
                });
```

#### `新的实例` 与 `单例` 的使用区别
- `New`   - 以new实例的形式 `new RxNet(context)` 代替 `RxNet.getInstance(context)`
- `Config` - 自定义配置，支持`.connectTimeout()、.baseUrl()、.headers()` 等配置，仅作用于此次请求.

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
