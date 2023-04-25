## Integrate Network Plugin in your application


### Add Gradle Dependencies
Pluto Network is distributed through [***mavenCentral***](https://central.sonatype.com/artifact/com.plutolib.plugins/network). To use it, you need to add the following Gradle dependency to your build.gradle file of you android app module.

> Note: add the `no-op` variant to isolate the plugin from release builds.
```groovy
dependencies {
  debugImplementation 'com.plutolib.plugins:network:2.1.2'
  releaseImplementation 'com.plutolib.plugins:network-no-op:2.1.2'
}
```
<br>

### Install plugin to Pluto

Now to start using the plugin, add it to Pluto
```kotlin
Pluto.Installer(this)
  .addPlugin(PlutoNetworkPlugin("network"))
  .install()
```
<br>

###  Add interceptor

To debug HTTP requests/responses, plug the PlutoInterceptor in your OkHttp Client Builder
```kotlin
val client = OkHttpClient.Builder()
    .addInterceptor(PlutoInterceptor())
    .build()
```
<br>

🎉 &nbsp;You are all done!

Now re-build and run your app and open Pluto, you will see the Network plugin installed.

<br>


### Log Custom Network traces
PlutoInterceptor works with OkHttp based interceptors, but to allow non-OkHttp based interceptors to track network calls Pluto provides utility to log network calls manually.
```kotlin
PlutoNetwork.logCustomTrace(
    request = CustomRequest(),
    response = CustomResponse()
)
```
Once logged successfully, the network call trace will appear in the network call list.
