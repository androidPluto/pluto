## Integrate Network Plugin in your application


### Add Gradle Dependencies
Pluto Network is distributed through [***mavenCentral***](https://central.sonatype.com/artifact/com.plutolib.plugins/network). To use it, you need to add the following Gradle dependency to your build.gradle file of you android app module.

> Note: add the `no-op` variant to isolate the plugin from release builds.
```groovy
dependencies {
  debugImplementation "com.plutolib.plugins:network:$plutoVersion"
  releaseImplementation "com.plutolib.plugins:network-no-op:$plutoVersion"
}
```
<br>

### Install plugin to Pluto

Now to start using the plugin, add it to Pluto
```kotlin
Pluto.Installer(this)
  .addPlugin(PlutoNetworkPlugin())
  .install()
```
<br>

> Note: If you are already using Network plugin, you need to do some code changes to migrate to version`2.1.7+`. Please [read here](https://github.com/androidPluto/pluto/wiki/Migrating-Network-plugin-to-version-2.1.7) for migration.

###  Add interceptors

#### 1. Okhttp Interceptor
Add okhttp-interceptor dependency
```groovy
dependencies {
  debugImplementation "com.plutolib.plugins:network-interceptor-okhttp:$plutoVersion"
  releaseImplementation "com.plutolib.plugins:network-interceptor-okhttp-no-op:$plutoVersion"
}
```

Add interceptor in your OkHttp Client Builder
```kotlin
val client = OkHttpClient.Builder()
  .addInterceptor(PlutoOkhttpInterceptor)
  .build()
```
<br>

#### 2. Ktor Interceptor
Add ktor-interceptor dependency
```groovy
dependencies {
  debugImplementation "com.plutolib.plugins:network-interceptor-ktor:$plutoVersion"
  releaseImplementation "com.plutolib.plugins:network-interceptor-ktor-no-op:$plutoVersion"
}
```

Add interceptor in your HttpClient
```kotlin
val client = HttpClient {
    install(PlutoKtorInterceptor)
}
```
<br>

#### 3. Custom Interceptor
If you wish to use any interceptor, other than Okhttp or Ktor, Pluto provides way to capture network logs.
```kotlin
/* create interceptor */
val networkInterceptor = NetworkInterceptor.intercept(
  NetworkData.Request(....),
  NetworkInterceptor.Option()
)

/**
 * wait for the network call to complete
**/

/* if error */
networkInterceptor.onError(exception)

/* if success */
networkInterceptor.onResponse(
  NetworkData.Response(....)
)
```

🎉 &nbsp;You are all done!

Now re-build and run your app and open Pluto, you will see the Network plugin installed.

<br>

### Open Plugin view programmatically
To open Network plugin screen via code, use this
```kotlin
Pluto.open(PlutoNetworkPlugin.ID)
```
<br>
