# RxNet for Android

[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)

RxNet 是一个基于 `Retrofit2` + `RxJava2` 实现的 `网络请求框架`

## 特点
-  `1` 条链，完全链式调用，自适应链 `.func0().func1()...` ，以良好的封装性保证 `简`
-  `2` 种Retrofit形式（全局配置一致 `单例`： 、完全自定义配置 `新的实例`）
-  `3` 种链式形式，高扩展性

## 支持列表（开发中...）
- [x] Get
- [x] Post
- [x] Head（未测试）
- [x] Options（未测试）
- [x] Put（未测试）
- [x] Patch（未测试）
- [x] Delete（未测试）
- [ ] Download
- [ ] Upload

## 使用

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

### Retrofit形式1 `单例` （使用全局配置）

#### 链式形式1
```java
        RxNet.getInstance(appContext).get(API.MovieTop.rtpType, params)
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
                
        RxNet.getInstance(appContext).get(API.MovieTop.rtpType, params)
                .request(new AsyncCallBack<MovieInfo, String>() {
                    @Override
                    public String apply(@NonNull MovieInfo info) throws Exception {
                        ...请求成功step-1 -->子线程 （做耗时、map转换操作）
                        int size = info.subjects.size();
                        return "" + size;//调用onSuccess
                    }

                    @Override
                    public void onSuccess(String response) {
                        ...请求成功step-2  -->主线程
                    }

                    @Override
                    public void onError(ApiException e) {
                        ...请求失败 -->主线程
                    }
                });
```

#### 链式形式2
```java
        //.observable（T）T为特定返回类型
        Observable<Info> observable = RxNet.getInstance(appContext).get(url)
                .observable(Info.class);

        //observable 调用Retrofit的观察者，而非CallBack接口
        observable.subscribe(new DisposableObserver<Info>() {
            @Override
            public void onNext(@NonNull Info info) {

            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
```

#### 链式形式3
```java
        //RxNet.getRetrofit(appContext)获取Retrofit，自行.create()
        RxNet.getRetrofit(appContext).create(SubAPI.class)
                .get(url)
                .subscribeOn(Schedulers.io())
                .map(new Function<Info, ArrayList<Boolean>>() {
                    @Override
                    public ArrayList<Boolean> apply(@NonNull Info info) throws Exception {
                        return new ArrayList<Boolean>();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ArrayList<Boolean>>() {
                    @Override
                    protected void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onNext(@NonNull ArrayList<Boolean> list) {
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
```

### Retrofit形式2 `新的实例` （支持全新的自定义配置、支持上述3种链式形式）
```java
        new RxNet(appContext).get("top250", params)
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

#### 与单例的使用区别
- `New`   - 以new实例的形式 `new RxNet(appContext)` 替换 `RxNet.getInstance(appContext)`
- `Config` - 支持自定义配置，同 `Application RxNet.init()...`，链式形式，其中 `某些值未配置`，将使用其 `全局的配置值`

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
