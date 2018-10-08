# RxNet for Android

[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![API](https://img.shields.io/badge/API-9%2B-green.svg?style=flat)](https://android-arsenal.com/api?level=9)
[![Download](https://api.bintray.com/packages/dsiner/maven/rxnet/images/download.svg) ](https://bintray.com/dsiner/maven/rxnet/_latestVersion)
[![Wiki-Guide](https://img.shields.io/badge/Wiki-Guide-brightgreen.svg)](https://github.com/Dsiner/RxNet/wiki)
[![Readme](https://img.shields.io/badge/README-%E4%B8%AD%E6%96%87-brightgreen.svg)](https://github.com/Dsiner/RxNet/blob/master/README-zh.md)

> A network request library based on `Retrofit2` + `Okhttp3` + `RxJava2`

## Features
- `1` chain, completely chained `.func0().func1().func2()...`, `Adaptive`, `Jane`
- `2` Client forms (`Singleton` global configuration, ` New instance ` fully custom configuration)
- `3` chained form, fully expanded

## Support
- [x] Supports Get, Post, Head, Options, Put, Patch, Delete Request Protocol
- [x] Support file download, progress callback
- [x] Support file upload, progress callback
- [x] Support for adding fixed header headers, dynamic header headers
- [x] Support failure retry mechanism, can specify retry times, retry interval
- [x] Support Tag, Cancel Data Request, Unsubscribe

## Getting Started
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

or:
If you need or would prefer to use a different version of the library you should exclude "xx.xx.xx" from your dependency in your build.gradle file.
For example:

```groovy
dependencies {
    implementation('com.dsiner.lib:rxnet:1.1.1', {
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

### How do I use RxNet?

See the [wiki](app/src/main/java/com/d/rxnet/MainActivity.java).

Simple use cases will look something like this:
```java
        Params params = new Params("https://api.douban.com/v2/movie/top250");
        params.addParam("start", "0");
        params.addParam("count", "10");
        RxNet.get("https://api.douban.com/v2/movie/top250", params)
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
