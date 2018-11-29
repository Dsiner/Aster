# Aster for Android

[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![API](https://img.shields.io/badge/API-9%2B-green.svg?style=flat)](https://android-arsenal.com/api?level=9)
[![Download](https://api.bintray.com/packages/dsiner/maven/aster/images/download.svg) ](https://bintray.com/dsiner/maven/aster/_latestVersion)
[![Wiki-Guide](https://img.shields.io/badge/Wiki-Guide-brightgreen.svg)](https://github.com/Dsiner/Aster/wiki)

> 基于 `Okhttp3` + `Retrofit2` + `RxJava2` 实现的 `网络请求库`

## 特点
-  `1` 条链，完全链式调用 `.func0().func1().func2()...` ， `自适应` 、 `简`
-  `2` 种Client形式（`单例` 全局配置、`新的实例` 完全自定义配置）
-  `3` 种链式形式，完全扩展

## 支持列表
- [x] 支持Get、Post、Head、Options、Put、Patch、Delete请求协议
- [x] 支持文件下载、进度回调
- [x] 支持文件上传、进度回调
- [x] 支持添加固定header头、动态header头
- [x] 支持失败重试机制，可以指定重试次数、重试间隔
- [x] 支持Tag、取消请求，取消订阅

## 使用
Maven:
```xml
<dependency>
  <groupId>com.dsiner.lib</groupId>
  <artifactId>aster</artifactId>
  <version>1.2.0</version>
</dependency>
```
or Gradle:
```groovy
compile 'com.dsiner.lib:aster:1.2.1'
```

or If you need or would prefer to use a different version of the library you should exclude "xx.xx.xx" from your dependency in your build.gradle file.
</br></br>For example:

```groovy
dependencies {
    implementation('com.dsiner.lib:aster:1.2.1', {
        exclude group: 'com.google.code.gson', module: 'gson'
        exclude group: 'com.squareup.okhttp3', module: 'okhttp'
        exclude group: 'com.squareup.okhttp3', module: 'logging-interceptor'
        exclude group: 'io.reactivex.rxjava2', module: 'rxjava'
        exclude group: 'io.reactivex.rxjava2', module: 'rxandroid'
        exclude group: 'com.squareup.retrofit2', module: 'retrofit'
        exclude group: 'com.squareup.retrofit2', module: 'adapter-rxjava2'
        exclude group: 'com.squareup.retrofit2', module: 'converter-gson'
        exclude group: 'com.squareup.retrofit2', module: 'converter-scalars'
    })
    implementation 'com.google.code.gson:gson:2.7'
    implementation 'com.squareup.okhttp3:okhttp:3.8.0'
    implementation 'com.squareup.okhttp3:logging-interceptor:3.8.0'
    implementation 'io.reactivex.rxjava2:rxjava:2.1.0'
    implementation 'io.reactivex.rxjava2:rxandroid:2.0.1'
    implementation 'com.squareup.retrofit2:retrofit:2.3.0'
    implementation 'com.squareup.retrofit2:adapter-rxjava2:2.3.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.3.0'
    implementation 'com.squareup.retrofit2:converter-scalars:2.3.0'
}
```

### How do I use Aster?

See the [wiki](https://github.com/Dsiner/Aster/wiki).

Simple use cases will look something like this:
```java
        Params params = new Params("https://api.douban.com/v2/movie/top250");
        params.addParam("start", "0");
        params.addParam("count", "10");
        Aster.get("https://api.douban.com/v2/movie/top250", params)
                .request(new SimpleCallback<MovieInfo>() {
                    @Override
                    public void onSuccess(MovieInfo response) {
                        ...do something in main thread
                    }

                    @Override
                    public void onError(Throwable e) {
                        ...do something in main thread
                    }
                });
```

More usage see [Demo](app/src/main/java/com/d/aster/MainActivity.java)

## Latest Changes
- [Changelog.md](CHANGELOG.md)

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
