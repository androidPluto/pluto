## Integrate Logger Plugin in your application


### Add Gradle Dependencies
Pluto Logger is distributed through [***mavenCentral***](https://search.maven.org/artifact/com.plutolib.plugins/logger). To use it, you need to add the following Gradle dependency to your build.gradle file of you android app module.

> Note: add the `no-op` variant to isolate the plugin from release builds.
```groovy
dependencies {
  debugImplementation 'com.plutolib.plugins:logger:2.1.2'
  releaseImplementation 'com.plutolib.plugins:logger-no-op:2.1.2'
}
```
<br>

### Install plugin to Pluto

Now to start using the plugin, add it to Pluto
```kotlin
Pluto.Installer(this)
  .addPlugin(PlutoLoggerPlugin("logger"))
  .install()
```
<br>

###  Add Pluto Logs

Pluto allows you to log and persist the user journey through the app, and help debug them without any need to connect to Logcat.

- **with PlutoLog**
```kotlin
PlutoLog.event("analytics", eventName, HashMap(attributes))
PlutoLog.d("debug_log", "button clicked")
PlutoLog.e("error_log", "api call failed with http_status 400")
PlutoLog.w("warning_log", "warning log")
PlutoLog.i("info_log", "api call completed")
```

- **with Timber**
```kotlin
Timber.tag("analytics").event(eventName, HashMap(attributes))
Timber.tag("debug_log").d("button clicked")
Timber.tag("error_log").e(NullPointerException("demo"), "api call failed with http_status 400")
Timber.tag("warning_log").w(NullPointerException("demo"), "warning log")
Timber.i("api call completed")
```
Install Pluto as a Tree for Timber:
```
Timber.plant(PlutoTimberTree());
```

But if you are connected to Logcat, PlutoLogs behave similar to Log class, with an improvement to tag the method and file name also. In Logcat, PlutoLogs will look like the following.
```
D/onClick(MainActivity.kt:40) | debug_log: button clicked
E/onFailure(NetworkManager.kt:17) | error_log: api call falied with http_status 400
```
<br>

🎉 &nbsp;You are all done!

Now re-build and run your app and open Pluto, you will see the Logger plugin installed.
